package AI.hotel.service;


import AI.hotel.bean.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface OrderService {

    List<Object> getAllNotUsedOrders(String id_number);

    List<Object> getRecentOrders(String id_number);

    List<Object> getAllOrders(String id_number);

    Object getOrderDetail(int id, int type);

    Boolean updateOrderStatus(int id, int status, int type);

    ArrayList<OrderIdAndImgName> getOccupants();

    ArrayList<OrderIdAndImgName> getTDepartureInfo();

    ArrayList<OrderIdAndImgName> getTDiningInfo();

    Boolean updateRestOrderInfo(RestaurantOrder data);

    Boolean deleteRestOrder(Integer id);

    Map<String, Object> getRestOrders(Integer page);

    Map<String, Object> SearchRestOrdersById(String id, Integer page);

    /**
     * 分页查询订单信息
     * @param num
     * @param pageQuery
     * @return
     */
    PageResult findPage(String num, PageRequest pageQuery);


    Object getOrderInfo(Integer id,String type);


}
