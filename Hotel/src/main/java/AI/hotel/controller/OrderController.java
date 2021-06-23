package AI.hotel.controller;

import AI.hotel.bean.*;
import AI.hotel.service.Impl.OrderServiceImpl;
import AI.hotel.utils.IDUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;

import static AI.hotel.utils.toJson.toJsonArray;
import static AI.hotel.utils.toJson.toJsonString;

@RestController
@Slf4j
@RequestMapping("/phone")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 获取所有未使用的订单，即已预定
     *
     * @param openid
     * @return
     */
    @GetMapping("/getNotUsedOrders/{openid}")
    public String getAllNotUsedOrders(@PathVariable String openid) {
        List<Object> orders = orderService.getAllNotUsedOrders(openid);
        if (orders != null) {
            return toJsonArray(200, "获取所有预定订单", orders);
        } else {
            return toJsonString(100, "获取所有预定订单失败", null);
        }
    }

    /**
     * 获取最近订单
     *
     * @param openid
     * @return
     */
    @GetMapping("/getRecentOrders/{openid}")
    public String getRecentOrders(@PathVariable String openid) {
        List<Object> recentOrders = orderService.getRecentOrders(openid);
        if (recentOrders != null) {
            return toJsonArray(200, "获取最近30天订单成功", recentOrders);
        } else {
            return toJsonString(100, "获取最近30天订单失败", null);
        }
    }

    /**
     * 获取所有订单
     *
     * @param openid
     * @return
     */
    @GetMapping("/getAllOrders/{openid}")
    public String getAllOrders(@PathVariable String openid) {


        List<Object> orders = orderService.getAllOrders(openid);
        if (orders != null) {
            return toJsonArray(200, "获取所有订单成功", orders);
        } else {
            return toJsonString(100, "获取所有订单失败", null);
        }
    }

    /**
     * 获取订单细节
     *
     * @param id
     * @param type
     * @return
     */
    @PostMapping("/getOrderDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "订单类型", required = true, dataType = "int", paramType = "query")
    })
    public String getOrderDetail(int id, int type) {
        if (type == 0 || type == 1) {
            Object detail = orderService.getOrderDetail(id, type);
            if (detail != null) {
                return toJsonArray(200, "获取订单信息成功", detail);
            } else {
                return toJsonString(100, "获取订单信息失败", null);
            }
        } else {
            return toJsonString(100, "type 范围错误", null);
        }
    }

    /**
     * 修改订单状态
     *
     * @param id
     * @param status
     * @param type
     * @return
     */

    @PutMapping("/updateOrderStatus")
    public String updateOrderStatus(int id, int status, int type) {
        if (orderService.updateOrderStatus(id, status, type)) {
            return toJsonString(200, "更新订单状态成功", null);
        }
        return toJsonString(100, "更新订单状态失败", null);
    }


    @DeleteMapping("/deleteOrderById")
    public String deleteOrderById(int id) {
        if (orderService.deleteOrderById(id)) {
            return toJsonString(200, "删除订单信息成功", true);
        }
        return toJsonString(100, "删除订单信息失败", false);
    }


    /**
     * 获取今天入住人照片信息
     *
     * @return
     */
    @GetMapping("/getOccupants")
    public String getOccupants() {
        if (redisTemplate.hasKey("haveNewOrder")) {
            int redisHavaNewOrder = (int) redisTemplate.opsForValue().get("haveNewOrder");
            if (redisHavaNewOrder >= 1) {
                ArrayList<OrderIdAndImgName> detail = orderService.getOccupants();
                redisTemplate.opsForValue().set("identityCard", detail);
                redisTemplate.opsForValue().set("haveNewOrder", 0);
                return toJsonArray(200, "获取今天入住人照片信息成功", detail);
            } else if (redisHavaNewOrder == 0) {
                Object detail = redisTemplate.opsForValue().get("identityCard");
                return toJsonArray(200, "获取今天入住人照片信息成功", detail);
            }
            return toJsonString(100, "获取今天入住人照片信息失败", null);
        }
        log.error("haveNewOrder is gone！！");
        redisTemplate.opsForValue().set("haveNewOrder", 1);
        return toJsonString(100, "获取今天入住人照片信息失败", null);
    }

    /**
     * 获取今日离店人信息
     */
    @GetMapping("/getTDepartureInfo")
    public String getTDepartureInfo() {
        ArrayList<OrderIdAndImgName> info = orderService.getTDepartureInfo();
        if (info != null) {
            return toJsonArray(200, "获取今日离店人信息成功", info);
        }
        return toJsonString(100, "获取今日离店人信息失败", null);
    }

    /**
     * 获取今日就餐人信息
     */
    @GetMapping("/getTDiningInfo")
    public String getTDiningInfo() {
        ArrayList<OrderIdAndImgName> info = orderService.getTDiningInfo();
        if (info != null) {
            return toJsonArray(200, "获取今日就餐人信息成功", info);
        }
        return toJsonString(100, "获取今日就餐人信息失败", null);
    }


    /**
     * 添加预定房间
     */
    @PostMapping("/addHouseOrder/{uuid}")
    public String addHouseOrder(@RequestBody List<HouseOrder> houseOrders, @PathVariable String uuid) {
        try {
            if (!houseOrders.isEmpty()) {
                for (int i = 0; i < houseOrders.size(); i++) {
                    HouseOrder houseOrder = houseOrders.get(i);
                    Map<String,Object> map = new HashMap();
                    map.put("houseOrder", houseOrder);
                    map.put("uuid", uuid);
                    rabbitTemplate.convertAndSend("hotel", "hotel.house_orders", map);
                }
                log.info("addHouseOrder");
                return toJsonArray(200, "添加订单队列成功", true);
            } else {
                return toJsonArray(100, "添加订单队列失败", false);
            }

        } catch (AmqpException e) {
            return toJsonArray(100, "添加订单队列失败", false);
        }
    }

    /**
     * 新增接口
     * @param uuid
     * @return
     */
    @GetMapping("/getIdByUuid/{uuid}")
    public String getIdByUuid(@PathVariable String uuid) {
        if (redisTemplate.hasKey(uuid)) {
            return toJsonString(200, "获取订单id成功", redisTemplate.opsForValue().get(uuid));
        }
        return toJsonArray(100, "获取订单id失败，可能已过期！", null);
    }

    /**
     * 新增接口
     * @param id
     * @param type
     * @return
     */
    @GetMapping("/getStatusById/{type}/{id}")
    public String getStatusById(@PathVariable Integer id, @PathVariable String type) {
        Object info = orderService.getOrderInfo(id, type);
        if (info != null) {
            return toJsonArray(200, "获取订单状态成功", info);
        }
        return toJsonArray(100, "获取订单状态失败！", null);
    }


    /**
     * 添加餐厅订单
     *
     * @return
     */

    @PostMapping("/addRestOrder/{uuid}")
    public String addRestOrder(RestaurantOrder restaurantOrder, @PathVariable String uuid) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("order", restaurantOrder);
        map.put("uuid", uuid);

        rabbitTemplate.convertAndSend("hotel", "hotel.restaurant_orders", map);


        return toJsonString(200, "添加订单队列成功", true);
    }

    @GetMapping("/searchOrder")
    public String searchOrder(String num) {
        if (num != null) {
            if (!IDUtils.isIDNumber(num)) return toJsonArray(100, "请输入正确的身份证", "false");
        }
        ArrayList<HouseOrder> orderInfo = orderService.searchOrder(num);
        if (!orderInfo.isEmpty()) {
            return toJsonArray(200, "获取订单信息成功", orderInfo);
        }
        return toJsonString(100, "获取订单信息失败", null);
    }


    @GetMapping("/searchOrderByPage")
    public String searchOrderByPage(String num, PageRequest pageQuery) {
        if (num != null) {
            if (!IDUtils.isIDNumber(num)) return toJsonArray(100, "请输入正确的身份证", "false");
        }
        PageResult result = orderService.findPage(num, pageQuery);
        if (result != null) {
            return toJsonArray(200, "获取订单信息成功", result);
        }
        return toJsonString(100, "获取订单信息失败", null);
    }


    /**
     * 三次开发，搜索，更新，获取，删除餐厅订单，获取餐厅订单状态
     * 搜索定单 分页
     * 获取 分页
     * 删除
     */
    @GetMapping("SearchRestOrdersById/{id}/{page}")
    public String SearchRestOrdersById(@PathVariable String id, @PathVariable Integer page) {
        Map<String, Object> orders = orderService.SearchRestOrdersById(id, page);
        if (orders.get("data") != null) {
            return toJsonArray(200, "搜索餐厅订单成功", orders);
        }
        return toJsonArray(100, "搜索餐厅订单失败", null);
    }

    @GetMapping("getRestOrders/{page}")
    public String getRestOrders(@PathVariable Integer page) {
        Map<String, Object> orders = orderService.getRestOrders(page);
        if (orders.get("data") != null) {
            return toJsonArray(200, "获取所有餐厅订单成功", orders);
        }
        return toJsonArray(100, "获取所有餐厅订单失败", null);
    }

    @DeleteMapping("/deleteRestOrder/{id}")
    public String deleteRestOrder(@PathVariable Integer id) {
        if (orderService.deleteRestOrder(id)) {
            log.info("删除餐厅订单 " + id);
            return toJsonArray(200, "删除餐厅订单成功", true);
        }
        return toJsonArray(100, "删除餐厅订单失败", false);
    }

    /**
     * 二次开发，更新订单信息
     *
     * @return
     */
    @PutMapping("/updateRestOrderInfo")
    public String updateRestOrderInfo(RestaurantOrder data) {
        if (orderService.updateRestOrderInfo(data)) {
            return toJsonString(200, "更新订单信息成功", null);
        }
        return toJsonString(100, "更新订单信息失败", null);
    }


}
