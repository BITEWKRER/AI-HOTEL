package AI.hotel.controller;

import AI.hotel.bean.Condition;
import AI.hotel.bean.House;
import AI.hotel.mapper.OrderMapper;
import AI.hotel.service.Impl.HouseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static AI.hotel.utils.toJson.toJsonArray;
import static AI.hotel.utils.toJson.toJsonString;

@RestController
@RequestMapping("/web")
public class HouseController {

    @Autowired
    private HouseServiceImpl houseService;


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 更新房间状态
     *
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/updateHouseStatus")
    public String updateHouseStatus(int id, int status) {
        if (houseService.updateHouseStatus(id, status)) {
            update(null);
            return toJsonString(200, "更新酒店房屋状态成功", null);
        } else {
            return toJsonString(100, "更新酒店房屋状态失败", null);
        }
    }

    /**
     * 添加 房屋信息
     *
     * @param house
     * @return
     */

    @PostMapping("/addHouse")
    public String addHouse(House house) {
        if (!houseService.isTrueAdd(house))
            return toJsonArray(100, "房间号已存在", "false");
        if (houseService.addHouse(house)) {
            update(null);
            return toJsonString(200, "添加房屋成功", null);
        } else {
            return toJsonString(100, "添加房屋失败", null);
        }
    }

    /**
     * 二次开发，直接全部更新房屋信息
     *
     * @param house
     * @return
     */
    @PutMapping("/updateHouseInfo")
    public String updateHouseInfo(House house) {
        if (houseService.updateHouseInfo(house)) {
            update(null);
            return toJsonString(200, "更新房屋信息成功", null);
        } else {
            return toJsonString(100, "更新房屋信息失败", null);
        }
    }

    /**
     * 根据条件返回房屋信息
     *
     * @param condition
     * @return
     */
    @PostMapping("searchHouse")
    public String searchHouse(Condition condition) {
        List<House> houses = houseService.searchHouse(condition);
        if (!houses.isEmpty()) {
            return toJsonArray(200, "获取房屋信息成功", houses);
        } else {
            return toJsonArray(100, "获取房屋信息失败", false);
        }
    }

    /**
     * 返回活动详情页数据
     *
     * @return
     */
    @GetMapping("getActiveHouse")
    public String getActiveHouse() {
        List<House> houses = houseService.getActiveHouse();
        if (!houses.isEmpty()) {
            return toJsonArray(200, "获取活动场所信息成功", houses);
        } else {
            return toJsonArray(100, "获取活动场所信息成功", false);
        }
    }

    @GetMapping("/getHouseByType")
    public String getHouseByType(int type) {
        ArrayList<House> house = houseService.getHouseByType(type);
        if (!house.isEmpty()) {
            return toJsonArray(200, "获取指定房屋信息成功", house);
        } else {
            return toJsonString(100, "获取指定房屋信息失败", null);
        }
    }

    @GetMapping("/getHouseById")
    public String getHouseById(int id, Date start, Date end) {
        ArrayList<House> house = houseService.getHouseById(id);
        if (!house.isEmpty()) {
            House temp = house.get(0);
            if (!orderMapper.getInHouse(start, end, temp.getType()).isEmpty()) temp.setStatus(1);
            else temp.setStatus(0);
            return toJsonArray(200, "获取指定房屋信息成功", temp);
        } else {
            return toJsonString(100, "获取指定房屋信息失败", null);
        }
    }

    @GetMapping("/getHouseByIdForWeb")
    public String getHouseByIdForWeb(int id) {
        ArrayList<House> house = houseService.getHouseById(id);
        if (!house.isEmpty()) {
            House temp = house.get(0);
            return toJsonArray(200, "获取指定房屋信息成功", temp);
        } else {
            return toJsonString(100, "获取指定房屋信息失败", null);
        }
    }

    @DeleteMapping("/deleteHouseInfo")
    public String deleteHouseInfo(int id) {
        if (houseService.deleteHouseInfo(id)) {
            update(null);
            return toJsonString(200, "删除房屋信息成功", null);
        } else {
            return toJsonString(100, "删除房屋信息失败", null);
        }
    }

    /**
     * 二次开发，获取所有房间状态
     *
     * @return
     */
    @GetMapping("/getHouseStatus")
    public String getHouseStatus() {
        if (redisTemplate.hasKey("HouseStatus")) {
            return toJsonArray(200, "获取房间状态成功", redisTemplate.opsForValue().get("HouseStatus"));
        }
        Map<String, ArrayList<House>> houseStatus = houseService.getHouseStatus();
        update(houseStatus);
        if (houseStatus != null) {
            return toJsonArray(200, "获取房间状态成功", houseStatus);
        }
        return toJsonString(100, "获取房间状态失败", null);
    }

    @Async
    void update(Map<String, ArrayList<House>> houses) {
        if (houses == null) {
            houses = houseService.getHouseStatus();
        }
        redisTemplate.opsForValue().set("HouseStatus", houses);
    }


}
