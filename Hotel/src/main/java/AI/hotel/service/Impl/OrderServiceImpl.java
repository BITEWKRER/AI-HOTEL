package AI.hotel.service.Impl;

import AI.hotel.bean.*;
import AI.hotel.mapper.ConsumeRecordsMapper;
import AI.hotel.mapper.HouseMapper;
import AI.hotel.mapper.OrderMapper;
import AI.hotel.service.OrderService;
import AI.hotel.statusCode.HouseOrdersCode;
import AI.hotel.statusCode.HousesCode;
import AI.hotel.statusCode.RestaurantOrdersCode;
import AI.hotel.utils.PageUtils;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private HouseMapper mapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ConsumeRecordsMapper consumeRecordsMapper;


    @Override
    public List<Object> getAllNotUsedOrders(String openid) {
        ArrayList<HOrder> orders = orderMapper.getAllNotUsedHouseOrders(openid, HouseOrdersCode.Ordered);
        ArrayList<RestaurantOrder> restOrders = orderMapper.getAllNotUsedRestOrders(openid, HouseOrdersCode.Ordered);
        restOrders = setMoney(restOrders);

        return ObjectContact(orders, restOrders);
    }

    @Override
    public List<Object> getRecentOrders(String openid) {
        ArrayList<HOrder> orders = orderMapper.getRecentHouseOrders(openid);
        ArrayList<RestaurantOrder> restOrders = orderMapper.getRecentRestOrders(openid);
        restOrders = setMoney(restOrders);
        return ObjectContact(orders, restOrders);
    }

    @Override
    public List<Object> getAllOrders(String openid) {
        ArrayList<HOrder> orders = orderMapper.getAllHouseOrders(openid);
        ArrayList<RestaurantOrder> restOrders = orderMapper.getAllRestOrders(openid);
        restOrders = setMoney(restOrders);
        return ObjectContact(orders, restOrders);
    }


    private ArrayList<RestaurantOrder> setMoney(ArrayList<RestaurantOrder> orders) {
        for (RestaurantOrder res : orders) {
            List<Menu> menus = json2Map(res.getItems());
            int tmp = 0;
            for (Menu menu : menus) {
                tmp += menu.getMoney();
            }
            res.setId_number(null);
            res.setItems(null);
            res.setOpen_id(null);
            res.setMoney(tmp);
        }
        return orders;
    }


    @Override
    public Object getOrderDetail(int id, int type) {
        List<Object> list = new ArrayList<>();
        Object obj = null;
        switch (type) {
            case 0:
                obj = orderMapper.getHouseOrderDetail(id);

                break;
            case 1:
                obj = orderMapper.getRestOrderDetail(id);
                break;
            default:
                break;
        }
        list.add(obj);
        return list;
    }

    @Override
    public Boolean updateOrderStatus(int id, int status, int type) {
        Boolean res = false;
        switch (type) {
            case 0:
                HouseOrder houseOrder = new HouseOrder();
                houseOrder.setId(id);

                /**
                 * ????????????
                 */
                if (status == HouseOrdersCode.Canceled) {
                    HouseOrder infoById = orderMapper.getInfoById(id);
                    House info = mapper.getInfoById(infoById.getHouse_id());
                    if (info.getStatus() == HousesCode.Ordered && isCancel(infoById.getHouse_id())) {
                        info.setStatus(HousesCode.NotUse);
                        mapper.updateHouseInfo(info);
                    }
                }
                houseOrder.setStatus(status);
                houseOrder.setUpdate_at(new Date());
                res = orderMapper.updateHouseOrders(houseOrder);
                if (res) redisTemplate.opsForValue().increment("haveNewOrder", 1);
                break;
            case 1:
                RestaurantOrder restaurantOrder = new RestaurantOrder();
                restaurantOrder.setId(id);
                restaurantOrder.setStatus(status);
                restaurantOrder.setUpdate_at(new Date());
                res = orderMapper.updateRestOrders(restaurantOrder);
                if (res) redisTemplate.opsForValue().increment("haveNewOrder", 1);
                break;
            default:
                break;
        }
        return res;
    }

    @RabbitListener(queues = "house_orders")
    public void addHouseOrder(Map<String, Object> map, Channel channel, Message message) {

        HouseOrder orders = null;
        try {
            try {
                Object o = map.get("houseOrder");
                JSON a = (JSON) JSON.toJSON(o);
                orders = JSON.toJavaObject(a, HouseOrder.class);
                String uuid = (String) map.get("uuid");
                orders.setCreate_at(new Date());
                if(orders.getCheck_in_time().equals(orders.getCheck_out_time())){
                    throw new Exception("????????????????????????????????????");
                }
                List<House> houses = orderMapper.getInHouse(orders.getCheck_in_time(), orders.getCheck_out_time(), orders.getHouse_type());
                //???????????????????????????
                if (!houses.isEmpty()) {
                    House house = houses.get(0);
                    orders.setHouse_id(house.getId());
                    orders.setRoom_number(house.getRoom_number());
                    orders.setStatus(HouseOrdersCode.Ordered);
                    mapper.updateHouseStatus(house.getId(), HousesCode.Ordered);

                    /**
                     * ????????????
                     */
                    ConsumeRecord records = new ConsumeRecord();
                    records.setCreate_at(new Date());
                    records.setItem_name(house.getName());
                    records.setItem_money(house.getMoney());
                    records.setIs_pay('0');
                    records.setDescription(house.getDescription());
                    records.setId_number(orders.getId_number());

                    consumeRecordsMapper.addRecord(records);
                } else {
                    orders.setStatus(HouseOrdersCode.Failed);
                    orders.setHouse_id(1);
                    orders.setRoom_number(999);
                }
                orderMapper.addHouseOrder(orders);
                // ??????uuid-id????????????

                redisTemplate.opsForValue().set(uuid, orders.getId(), 60 * 10, TimeUnit.SECONDS);
                redisTemplate.opsForValue().increment("haveNewOrder", 1);
                log.info("haveNewOrder");
            } catch (Exception e) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.error("????????????????????????????????????-house_orders-???????????????" + e.getMessage());
                return;
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("?????????????????????????????????id???" + orders.getId());

        } catch (IOException e) {
            log.error("??????????????????????????????????????????" + e.getMessage());
        }

    }


    @RabbitListener(queues = "restaurant_orders")
    public void addRestOrder(Map<String, Object> map, Channel channel, Message message) {
        Object o = map.get("order");
        JSON json = (JSON) JSON.toJSON(o);
        RestaurantOrder orders = JSON.toJavaObject(json, RestaurantOrder.class);
        Object uuid = map.get("uuid");


        try {
            try {
                ConsumeRecord records = new ConsumeRecord();
                orders.setCreate_at(new Date());

                if (orders.getIs_delivery() == 1) {
                    records.setItem_name("????????????");
                    records.setDescription("??????????????????");
                    orders.setStatus(RestaurantOrdersCode.Ordered);
                } else {
                    //?????????????????????????????????30
                    if (orderMapper.getRestOrderCount(RestaurantOrdersCode.Ordered, RestaurantOrdersCode.Used, orders.getCheck_in_time()) <= 30) {
                        records.setItem_name("????????????");
                        records.setDescription("??????????????????");
                        orders.setStatus(RestaurantOrdersCode.Ordered);
                    } else {
                        orders.setStatus(RestaurantOrdersCode.Failed);
                    }
                }
                /**
                 * ????????????
                 */
                records.setId_number(orders.getId_number());
                records.setCreate_at(new Date());
                records.setIs_pay('0');

                String items = orders.getItems();
                List<Menu> menuList = json2Map(items);
                int sum = 0;
                for (int i = 0; i < menuList.size(); i++) {
                    sum += menuList.get(i).getMoney();
                }
                records.setItem_money(sum);
                consumeRecordsMapper.addRecord(records);
                orderMapper.addRestOrder(orders);
                // ??????uuid-id????????????

                redisTemplate.opsForValue().set(uuid, orders.getId(), 60 * 10, TimeUnit.SECONDS);

            } catch (Exception e) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.error("?????????????????????????????????-restaurant_orders-???????????????: " + e.getMessage());
                return;
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("?????????????????????????????????id???" + orders.getId());
        } catch (IOException e) {
            log.error("??????????????????????????????????????????" + e.getMessage());
        }
    }

    private static java.util.List<Menu> json2Map(String json) {
        return JSON.parseArray(json, Menu.class);
    }


    private List<Object> ObjectContact(ArrayList<HOrder> obj1, ArrayList<RestaurantOrder> obj2) {
        List<Object> list = new ArrayList<>();
        list.addAll(obj1);
        list.addAll(obj2);
        return list;
    }

    @Override
    public ArrayList<OrderIdAndImgName> getOccupants() {
        return orderMapper.getOccupants(RestaurantOrdersCode.Ordered);
    }

    @Override
    public ArrayList<OrderIdAndImgName> getTDepartureInfo() {
        return orderMapper.getTDepartureInfo(RestaurantOrdersCode.Used);
    }

    @Override
    public ArrayList<OrderIdAndImgName> getTDiningInfo() {
        return orderMapper.getTDiningInfo(RestaurantOrdersCode.Ordered);
    }

    @Override
    public Boolean updateRestOrderInfo(RestaurantOrder data) {
        RestaurantOrder order = orderMapper.getRestOrderById(data.getId());
        // ?????????
        order.setPhone(data.getPhone());
        //????????????
        order.setStatus(data.getStatus());
        //????????????
        //????????????
        if (data.getIs_delivery() == 0) {
            if (orderMapper.getRestOrderCount(RestaurantOrdersCode.Ordered, RestaurantOrdersCode.Used, data.getCheck_in_time()) <= 30) {
                order.setIs_delivery(data.getIs_delivery());
                order.setCheck_in_time(data.getCheck_in_time());
            } else {
                return false;
            }
        } else {
            order.setIs_delivery(data.getIs_delivery());
        }
        order.setUpdate_at(new Date());
        return orderMapper.updateOrderInfoOfRes(order);
    }

    @Override
    public Boolean deleteRestOrder(Integer id) {
        return orderMapper.deleteRestOrder(id);
    }

    @Override
    public Map<String, Object> getRestOrders(Integer page) {
        return resultCollection(page, "all", null);
    }

    @Override
    public Map<String, Object> SearchRestOrdersById(String id, Integer page) {
        return resultCollection(page, "search", id);
    }

    private Map<String, Object> resultCollection(Integer page, String type, @Nullable String id) {
        page -= 1;
        int pageNumber = 10;
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("pageNumber", pageNumber);
        //????????????????????????????????????????????????
        switch (type) {
            case "search":
                map.put("total", orderMapper.SearchRestOrdersByIdCount(id));
                map.put("data", orderMapper.SearchRestOrdersById(id, page * pageNumber, (page * pageNumber) + pageNumber));
                break;
            case "all":
                map.put("total", orderMapper.getRestOrdersCount());
                map.put("data", orderMapper.getRestOrders(page * pageNumber, (page * pageNumber) + pageNumber));
                break;
            default:
                break;
        }
        return map;
    }

    public ArrayList<HouseOrder> searchOrder(String num) {
        return orderMapper.searchOrder(num);

    }

    public boolean deleteOrderById(int id) {
        return orderMapper.deleteOrderById(id);
    }

    /**
     * ??????????????????????????????
     *
     * @param pageRequest
     * @return
     */
    private PageInfo<HouseOrder> getPageInfo(PageRequest pageRequest, String num) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<HouseOrder> houseOrders = orderMapper.searchOrder(num);
        return new PageInfo<HouseOrder>(houseOrders);
    }

    public PageResult findPage(String num, PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest, num));
    }


    public boolean isCancel(Integer id) {
        if (orderMapper.getHouseOrderByHouseId(id) == null)
            return true;
        else return false;
    }

    @Override
    public Object getOrderInfo(Integer id, String type) {
        Object o = null;
        switch (type) {
            case "house":
                o = orderMapper.getInfoById(id);
                break;
            case "rest":
                o = orderMapper.getRestOrderById(id);
                break;
            default:
                break;
        }
        return o;
    }

    public boolean updateHouseId(int id){
        return orderMapper.updateHouseId(id);

    }

}
