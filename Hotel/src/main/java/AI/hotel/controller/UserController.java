package AI.hotel.controller;


import AI.hotel.bean.OrderIdAndImgName;
import AI.hotel.bean.User;
import AI.hotel.bean.UserMessage;
import AI.hotel.service.Impl.ConsumeRecordsServiceImpl;
import AI.hotel.service.Impl.HouseServiceImpl;
import AI.hotel.service.Impl.OrderServiceImpl;
import AI.hotel.service.Impl.UserServiceImpl;
import AI.hotel.statusCode.HouseOrdersCode;
import AI.hotel.statusCode.HousesCode;
import AI.hotel.statusCode.RestaurantOrdersCode;
import AI.hotel.utils.FaceCompare;
import AI.hotel.utils.Identification;
import com.alibaba.fastjson.JSONObject;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.enums.CompareModel;
import com.arcsoft.face.toolkit.ImageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.*;

import static AI.hotel.utils.FileUtils.getStaticPath;
import static AI.hotel.utils.toJson.toJsonArray;
import static AI.hotel.utils.toJson.toJsonString;
import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

@RestController
@Slf4j
@RequestMapping("/phone")
public class UserController {

    @Value("${com.appcode}")
    private String appcode;

    @Autowired
    private FaceEngine faceEngine;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private HouseServiceImpl houseService;

    @Autowired
    private ConsumeRecordsServiceImpl recordsService;

    @Autowired
    private UserServiceImpl userService;


    /**
     * 办理入住 0 ,退房 1,就餐确认 2
     *
     * @param name
     * @return
     */
    @PostMapping("/isOne")
    public String isOne(String name, int occasion) {

        ArrayList<OrderIdAndImgName> data = null;
        switch (occasion) {
            case 0:
                data = orderService.getOccupants();
                break;
            case 1:
                data = orderService.getTDepartureInfo();
                break;
            case 2:
                data = orderService.getTDiningInfo();
                break;
            default:
                break;
        }

        int errorCode = 0;
        FaceCompare faceCompare = new FaceCompare();


        double score = 0;


        for (int i = 0; i < data.size(); i++) {
            String path1 = getStaticPath("faceImgs") + data.get(i).getImgName();
            String path2 = getStaticPath("faceImgs") + name;

            FaceFeature targetFaceFeature = faceCompare.getFeatureData(faceEngine, errorCode, path1);
            FaceFeature sourceFaceFeature = faceCompare.getFeatureData(faceEngine, errorCode, path2);

            FaceSimilar faceSimilar = new FaceSimilar();
            errorCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, CompareModel.LIFE_PHOTO, faceSimilar);
            score = faceSimilar.getScore();
            Object detail = null;


            score = faceSimilar.getScore();
            if (score >= 0.75) {

                switch (occasion) {
                    case 0:
                        log.info("入住订单id ：" + data.get(i).getId() + ",相似度：" + score);
                        orderService.updateOrderStatus(data.get(i).getId(), HouseOrdersCode.Used, 0);
                        houseService.updateHouseStatus(data.get(i).getHouse_Id(), HousesCode.Used);
                        detail = orderService.getOrderDetail(data.get(i).getId(), 0);
                        break;
                    case 1:
                        log.info("退房订单id ：" + data.get(i).getId() + ",相似度：" + score);
                        if (data.get(i).getHouse_Id() == 1) return toJsonArray(100, "此房间已经退过此订单", "false");
                        if (orderService.isCancel(data.get(i).getHouse_Id()))
                            houseService.updateHouseStatus(data.get(i).getHouse_Id(), HousesCode.NotUse);
                        else houseService.updateHouseStatus(data.get(i).getHouse_Id(), HousesCode.Ordered);
                        detail = recordsService.getAllNotPayOrders(userService.getIdByHouseID(data.get(i).getId()));
                        Map<String, Object> result = new HashMap<>();
                        result.put("info", orderService.getOrderDetail(data.get(i).getId(), 0));
                        result.put("detail", detail);
                        detail = result;
                        orderService.updateHouseId(data.get(i).getId());
                        break;
                    case 2:
                        log.info("就餐订单id ：" + data.get(i).getId() + ",相似度：" + score);
                        orderService.updateOrderStatus(data.get(i).getId(), RestaurantOrdersCode.Used, 1);
                        break;
                    default:
                        break;
                }
                return toJsonArray(200, "人脸比对成功", detail);
            }
        }

        return toJsonArray(100, "人脸比对失败,无订单或拍照不清晰！", score);
    }

    @Transactional
    @PostMapping("addUserMessage")
    public String addUserMessage(UserMessage userMessage) {
        userMessage.setCreateAt(new Date());
        if (userService.getUsersByOpenNum(userMessage.getOpen_id(), userMessage.getIdNumber()).isEmpty()) {
            boolean flag = userService.addUser(userMessage);
            if (flag) {
                userService.addIdCard(userMessage);
                return toJsonArray(200, "用户添加成功", true);
            }
            return toJsonArray(100, "用户添加失败", false);
        }
        return toJsonArray(100, "该账户已经添加过", false);
    }


    @PostMapping("checkInCard")
    public String checkInCard(@RequestParam("file") MultipartFile file, String side) {
        FileController fileController = new FileController();
        Identification identification = new Identification();
        String result = fileController.uploadImg(file, "faceImgs");
        JSONObject ojar = JSONObject.parseObject(result);
        int code = (int) ojar.get("code");
        String fileName = (String) ojar.get("data");

        if (code == 200 && side.equals("face")) {
            String detail = identification.getIdentification(fileName, side, appcode);
            if (detail != null) {
                JSONObject jsonD = JSONObject.parseObject(detail);
                JSONObject sendJson = new JSONObject();
                ArrayList<String> all = new ArrayList<>();
                sendJson.put("address", (String) jsonD.get("address"));
                sendJson.put("nationality", (String) jsonD.get("nationality"));
                sendJson.put("num", (String) jsonD.get("num"));
                sendJson.put("name", (String) jsonD.get("name"));
                sendJson.put("sex", (String) jsonD.get("sex"));
                sendJson.put("birth", (String) jsonD.get("birth"));
                sendJson.put("fileName",fileName);
                for (String key : sendJson.keySet()
                        ) {
                    all.add((String) sendJson.get(key));
                }
                if (identification.isPass(all)) return toJsonArray(200, "身份证正面识别成功", all);
                return toJsonArray(100, "身份证正面识别失败，请重新上传", "false");
            }
        } else if (code == 200 && side.equals("back")) {
            String detail = identification.getIdentification(fileName, side, appcode);
            if (detail != null) {
                JSONObject jsonD = JSONObject.parseObject(detail);
                JSONObject sendJson = new JSONObject();
                ArrayList<String> all = new ArrayList<>();
                sendJson.put("start_date", (String) jsonD.get("start_date"));
                sendJson.put("end_date", (String) jsonD.get("end_date"));
                sendJson.put("issue", (String) jsonD.get("issue"));
                sendJson.put("fileName",fileName);
                for (String key : sendJson.keySet()
                        ) {
                    all.add((String) sendJson.get(key));
                }
                if (identification.isPass(all)) return toJsonArray(200, "身份证反面识别成功", all);
                return toJsonArray(100, "身份证背面识别失败", "false");
            }
        } else if (code == 200 && side.equals("recent")) {
            int errorCode = 0;
            String path1 = getStaticPath("faceImgs") + fileName;
            File file1 = new File(path1);
            if (file1.exists()) {
                ImageInfo imageInfo = getRGBData(new File(path1));
                List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
                errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
                if (faceInfoList.isEmpty()) {
                    file1.delete();
                    return toJsonArray(100, "近照不符合要求，请重新上传", false);
                }
                return toJsonArray(200, "上传近照成功", fileName);
            }
        }
        return toJsonArray(100, "识别失败，请重新上传", false);
    }


    /**
     * 获取用户列表（一个vx号(openId)对应一个列表）
     *
     * @param openId
     * @return
     */
    @PostMapping("/getUsersByOpen")
    public String getUsersByOpen(String openId) {
        List<User> users = userService.getUsersByOpen(openId);
        if (users != null) {
            return toJsonArray(200, "获取用户信息成功", users);
        } else {
            return toJsonString(100, "获取用户信息失败", null);
        }
    }

    @PreDestroy
    public void faceEngineDestroy() {
        log.info("引擎销毁:" + faceEngine.unInit());
    }


}
