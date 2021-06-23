package AI.hotel.controller;

import AI.hotel.bean.HotelImg;
import AI.hotel.service.Impl.HotelImgServiceImpl;
import com.sun.istack.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static AI.hotel.utils.toJson.toJsonArray;
import static AI.hotel.utils.toJson.toJsonString;

@RestController
@RequestMapping("/web")
@Slf4j
public class HotelImgController {

    @Autowired
    private HotelImgServiceImpl hotelImgService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/addHotelImg")
    public String addHotelImg(HotelImg hotelImg) {
        hotelImg.setCreate_at(new Date());
        if (hotelImgService.addHotelImg(hotelImg)) {
            update(null);
            return toJsonArray(200, "添加酒店图片成功", hotelImg);
        }
        return toJsonString(100, "添加酒店图片失败", null);
    }

    /**
     * 获取相应房间照片 最新5张
     *
     * @param type 房间类型
     * @return
     */
    @GetMapping("/getImg")
    public String getHotelImg(int type) {
        List<HotelImg> img = hotelImgService.getHotelImg(type);
        if (!img.isEmpty()) {
            return toJsonArray(200, "获取酒店图片成功", img);
        } else {
            return toJsonString(100, "获取酒店图片失败", null);
        }
    }

    @GetMapping("/getAllTypeImg")
    public String getAllTypeImg() {
        if (redisTemplate.hasKey("AllTypeHousesImg")) {
            return toJsonArray(200, "获取全部类型房间图片成功", redisTemplate.opsForValue().get("AllTypeHousesImg"));
        }
        Map<String, Object> img = hotelImgService.getAllTypeImg();
        update(img);
        return toJsonArray(200, "获取全部类型房间图片成功", img);
    }

    @DeleteMapping("/deleteHotelImg")
    public String deleteHotelImg(int id) {
        if (hotelImgService.deleteHotelImg(id)) {
            update(null);
            return toJsonString(200, "删除酒店图片成功", null);
        } else {
            return toJsonString(100, "删除酒店图片失败", null);
        }
    }

    @PostMapping("/updateHotelItemImg")
    public String updateHotelItemImg(HotelImg hotelImg) {
        hotelImg.setUpdate_at(new Date());
        if (hotelImgService.updateMenuItemImg(hotelImg)) {
            update(null);
            return toJsonArray(200, "更新酒店图片成功", hotelImg.getPath());
        }
        return toJsonString(100, "更新酒店图片失败", null);
    }

    @Async
    void update(@Nullable Map<String, Object> img) {
        if (img == null) {
            img = hotelImgService.getAllTypeImg();
        }
        redisTemplate.opsForValue().set("AllTypeHousesImg", img);
    }


}
