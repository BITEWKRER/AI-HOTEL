package AI.hotel.service.Impl;

import AI.hotel.bean.*;
import AI.hotel.mapper.DealExpiredOrderMapper;
import AI.hotel.mapper.DealWithSuperfluousFile;
import AI.hotel.statusCode.HouseOrdersCode;
import AI.hotel.statusCode.RestaurantOrdersCode;
import AI.hotel.utils.Excel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static AI.hotel.statusCode.HouseTypeCode.KingBedRoom;
import static AI.hotel.statusCode.HouseTypeCode.Rest;
import static AI.hotel.utils.FileUtils.deleteFile;
import static AI.hotel.utils.FileUtils.getStaticPath;


@Service
@Slf4j
public class ScheduledService {
    @Autowired
    EmployeeServiceImpl employeeService;

    @Autowired
    DataDealServiceImpl dataDealService;

    @Autowired
    private DealExpiredOrderMapper dealExpiredOrderMapper;

    @Autowired
    private DealWithSuperfluousFile dealWithSuperfluousFile;

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0 1,10,20,30,40,50 * * * ?")
    @Async
    public void dealWithExpiredOrders() {
        ArrayList<RestaurantOrder> restDestineOrders = dealExpiredOrderMapper.getAllRestDestineOrders(RestaurantOrdersCode.Ordered);
        ArrayList<RestaurantOrder> dealExpiredRestOrder = dealExpiredOrderMapper.dealExpiredRestOrder(RestaurantOrdersCode.Ordered);
        deal(restDestineOrders, null, 0);
        deal(dealExpiredRestOrder, null, 0);
        log.info("??????????????????????????????...");
    }

    @Scheduled(cron = "0 1,10,20,30,40,50 6 * * ?")
    @Async
    public void dealWithHouseExpiredOrders() {
        ArrayList<HouseOrder> allDestineOrders = dealExpiredOrderMapper.getAllDestineOrders(KingBedRoom, Rest, HouseOrdersCode.Ordered);
        ArrayList<HouseOrder> houseOrders = dealExpiredOrderMapper.dealExpiredOrder(KingBedRoom, Rest, HouseOrdersCode.Ordered);
        deal(null, allDestineOrders, 1);
        deal(null, houseOrders, 1);
        log.info("??????????????????????????????...");
    }


    // ?????????????????????????????????Excel????????????
    @Scheduled(cron = "0 0 0 1 * ? ")
    @Async
    public void getExcel() {
        Excel excel = new Excel();
        Map<Integer, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> item = new HashMap<>();
        //?????????
        item.put("bigTitle", "??????????????????");
        //??????
        ArrayList<String> strings = new ArrayList<>();
        strings.add("??????");
        strings.add("??????");
        item.put("header", strings);
        //??????
        ArrayList<ArrayList<String>> values = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<DataResult> message = dataDealService.getRecent7Data();
        for (int i = 0; i < message.size(); i++) {
            ArrayList<String> value = new ArrayList<>();
            value.add("" + sdf.format(message.get(i).getDate()));
            value.add("" + message.get(i).getMoney());
            values.add(value);
        }
        item.put("value", values);
        //????????????
        item.put("count", false);
        //????????????,????????????
        item.put("field", 5);
        map.put(1, item);
        Map<String, Object> item1 = new HashMap<>();
        //?????????
        item1.put("bigTitle", "??????????????????");
        //??????
        ArrayList<String> string = new ArrayList<>();
        string.add("????????????");
        string.add("????????????");
        string.add("????????????");
        item1.put("header", string);
        //??????
        ArrayList<ArrayList<String>> valueOne = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();
        value.add(dataDealService.getDayMoney().toString());
        value.add(dataDealService.getWeekMoney().toString());
        value.add(dataDealService.getMonMoney().toString());
        valueOne.add(value);
        item1.put("value", valueOne);
        //????????????
        item1.put("count", false);
        //????????????,????????????
        item1.put("field", 5);
        map.put(0, item1);
        String fileName = null;
        try {
            fileName = excel.init(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sendEmail(fileName);
    }

    public void sendEmail(String fileName) {
        if (fileName.equals("false")) return;
        List<Employee> list = employeeService.getEmployeeByRole("ADMIN");
        if (list.isEmpty()) return;
        for (int i = 0; i < list.size(); i++) {
            //???????????????
            String user = list.get(i).getName();
            //excel??????????????????excel?????????sendEmail??????????????????
            String filename = fileName;
            //????????????
            String toWho = list.get(i).getEmail();


            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                helper = new MimeMessageHelper(message, true);
                helper.setSubject("Ai????????????" + getCurrentMouth() + "???????????????????????????");

                helper.setText("<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "  <head>\n" +
                        "    <meta charset=\"UTF-8\" />\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                        "    <title>Document</title>\n" +
                        "    <style>\n" +
                        "      html,\n" +
                        "      body {\n" +
                        "        width: 100%;\n" +
                        "        height: 100%;\n" +
                        "      }\n" +
                        "\n" +
                        "      .main {\n" +
                        "        width: 100%;\n" +
                        "        height: 100%;\n" +
                        "        display: flex;\n" +
                        "        justify-content: center;\n" +
                        "        align-items: center;\n" +
                        "      }\n" +
                        "\n" +
                        "      .content {\n" +
                        "        width: 700px;\n" +
                        "        height: 500px;\n" +
                        "      }\n" +
                        "      .contentTitle {\n" +
                        "        border-top: 1px solid #ccc;\n" +
                        "        border-bottom: 1px solid #ccc;\n" +
                        "        width: 700px;\n" +
                        "        height: 270px;\n" +
                        "        box-sizing: border-box;\n" +
                        "        padding: 30px 10px;\n" +
                        "      }\n" +
                        "\n" +
                        "      .title1 {\n" +
                        "        font-size: 18px;\n" +
                        "        color: #333;\n" +
                        "        font-weight: 600;\n" +
                        "        margin: 30px 0;\n" +
                        "      }\n" +
                        "\n" +
                        "      .title2 {\n" +
                        "        margin: 10px 0;\n" +
                        "        font-size: 18px;\n" +
                        "        color: #333;\n" +
                        "        font-weight: 600;\n" +
                        "        margin: 30px 0;\n" +
                        "      }\n" +
                        "\n" +
                        "      .title2 span:nth-of-type(2) {\n" +
                        "        color: red;\n" +
                        "      }\n" +
                        "\n" +
                        "      .title2 span:nth-of-type(4) {\n" +
                        "        color: red;\n" +
                        "        font-size: 25px;\n" +
                        "        margin: 30px 0 60px 0;\n" +
                        "      }\n" +
                        "\n" +
                        "      .title3 {\n" +
                        "        margin: 30px 0;\n" +
                        "        font-size: 14px;\n" +
                        "        color: #333;\n" +
                        "        font-weight: 400;\n" +
                        "      }\n" +
                        "      .contentInfo {\n" +
                        "        width: 700px;\n" +
                        "        height: 200px;\n" +
                        "        box-sizing: border-box;\n" +
                        "        padding: 30px 10px;\n" +
                        "      }\n" +
                        "      .contentInfo1 div:nth-of-type(1) {\n" +
                        "        margin: 20px 0 10px 0;\n" +
                        "        font-size: 14px;\n" +
                        "        color: #333;\n" +
                        "        font-weight: 400;\n" +
                        "      }\n" +
                        "      .contentInfo1 div:nth-of-type(3) {\n" +
                        "        margin: 10px 0 0 0;\n" +
                        "        font-size: 14px;\n" +
                        "        color: #333;\n" +
                        "        font-weight: 400;\n" +
                        "      }\n" +
                        "    </style>\n" +
                        "  </head>\n" +
                        "\n" +
                        "  <body>\n" +
                        "    <div class=\"main\">\n" +
                        "      <div class=\"content\">\n" +
                        "        <div class=\"contentTitle\">\n" +
                        "          <div class=\"title1\">?????????" + user + "????????????</div>\n" +
                        "          <div class=\"title2\">\n" +
                        "            <span>" + getCurrentMouth() + "??????</span>\n" +
                        "            <span>??????????????????</span>\n" +
                        "            <span>??????????????????</span>\n" +
                        "            <span>????????????????????????</span>\n" +
                        "            <span>???????????????</span>\n" +
                        "          </div>\n" +
                        "          <div class=\"title3\">\n" +
                        "            ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????132xxxx7985\n" +
                        "          </div>\n" +
                        "        </div>\n" +
                        "        <div class=\"contentInfo\">\n" +
                        "          <div class=\"contentInfo1\">\n" +
                        "            <div>?????????????????????????????????</div>\n" +
                        "            <div>???????????????????????????????????????????????????</div>\n" +
                        "            <div>???????????????????????????</div>\n" +
                        "          </div>\n" +
                        "        </div>\n" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "  </body>\n" +
                        "</html>\n", true);

                helper.addAttachment(filename, new File(getStaticPath("passengerAnalysis/" + getCurrentYear()) + filename));
                //?????????
                helper.setFrom("3407892521@qq.com");
                //?????????
                helper.setTo(toWho);

                javaMailSender.send(message);
            } catch (org.springframework.messaging.MessagingException | javax.mail.MessagingException e) {
                e.printStackTrace();
            }

        }


    }


    // ????????????????????????????????????
    @Scheduled(cron = "0 0 0 1 * ? ")
    @Async
    public void dealWithSuperfluousFiles() {
        ArrayList<String> path = dealWithSuperfluousFile.getHotelImgPath();
        ArrayList<String> imgPath = dealWithSuperfluousFile.getMenuImgPath();
        ArrayList<IdCard> cardPath = dealWithSuperfluousFile.getIdCardPath();

        // ????????????
        isEqual("DishImgs", imgPath);
        // ????????????
        isEqual("imgs", path);
        // ?????????
        String[] list = new File(getStaticPath("faceImgs")).list();
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < cardPath.size(); j++) {
                if (cardPath.get(j).getFront().equals(list[i]) || cardPath.get(j).getBack().equals(list[i]) || cardPath.get(j).getRecent().equals(list[i])) {
                    list[i] = null;
                }
            }
        }
        delete("faceImgs", list);

        log.info("??????????????????????????????...");
    }

    private void isEqual(String path, ArrayList<String> objects) {
        String[] list = new File(getStaticPath(path)).list();
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < objects.size(); j++) {
                if (objects.get(j).equals(list[i])) {
                    list[i] = null;
                }
            }
        }
        delete(path, list);
    }

    private void delete(String path, String[] list) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null) {
                log.info("???????????????" + list[i]);
                deleteFile(path, list[i]);
            }
        }
    }

    private void deal(ArrayList<RestaurantOrder> list, ArrayList<HouseOrder> house, int type) {

        switch (type) {
            case 0:
                for (RestaurantOrder orders : list) {
                    orders.setStatus(RestaurantOrdersCode.Expired);
                    orders.setUpdate_at(new Date());
                    dealExpiredOrderMapper.updateRestDestineOrders(orders);
                    log.info("????????????????????????---??????id???" + orders.getId());
                }
                break;
            case 1:
                for (HouseOrder orders : house) {
                    orders.setStatus(HouseOrdersCode.Expired);
                    orders.setHouse_id(1);
                    orders.setUpdate_at(new Date());
                    dealExpiredOrderMapper.updateDestineOrders(orders);
                    redisTemplate.opsForValue().increment("haveNewOrder", 1);
                    log.info("????????????????????????---??????id???" + orders.getId());
                }
                break;
            default:
                break;
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void scheduledTask() {
        redisTemplate.opsForValue().increment("haveNewOrder", 1);
    }


    private static String getCurrentYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
    }


    private static String getCurrentMouth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date date = new Date();
        return sdf.format(date);
    }


}

