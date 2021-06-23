package AI.hotel.mapper;

import AI.hotel.bean.*;
import AI.hotel.bean.HOrder;
import AI.hotel.bean.OrderIdAndImgName;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface OrderMapper {

    /**
     * 获取所有未使用的订单
     *
     * @param openid
     * @param status
     * @return
     */
    @Select("SELECT\n" +
            "\thouse_orders.id,\n" +
            "\thouses.`name`,\n" +
            "\thouses.room_number,\n" +
            "\thouses.money,\n" +
            "\thouse_orders.`status`,\n" +
            "\thouse_orders.check_in_time,\n" +
            "\thouse_orders.check_out_time,\n" +
            "\thouse_orders.create_at \n" +
            "FROM\n" +
            "\thouse_orders\n" +
            "\tLEFT JOIN houses ON house_orders.house_Id = houses.id \n" +
            "WHERE\n" +
            "\thouse_orders.STATUS =  #{status}\n" +
            "\tAND open_id = #{openid}")
    ArrayList<HOrder> getAllNotUsedHouseOrders(@Param("openid") String openid, @Param("status") int status);

    /**
     * 获取所有未使用的餐厅订单
     *
     * @param openid
     * @param status
     * @return
     */
    @Select("select * from restaurant_orders where open_id = #{openid} and status = #{status}")
    ArrayList<RestaurantOrder> getAllNotUsedRestOrders(@Param("openid") String openid, @Param("status") int status);


    /**
     * 获取最近订单
     *
     * @param openid
     * @return
     */
    @Select("SELECT * FROM restaurant_orders where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= check_in_time and open_id =  #{openid}")
    ArrayList<RestaurantOrder> getRecentRestOrders(@Param("openid") String openid);

    /**
     * 获取最近房间订单
     *
     * @param openid
     * @return
     */
    @Select("SELECT\n" +
            "\thouse_orders.id,\n" +
            "\thouses.name,\n" +
            "\thouses.room_number,\n" +
            "\thouses.money,\n" +
            "\thouse_orders.`status`,\n" +
            "\thouse_orders.check_in_time,\n" +
            "\thouse_orders.check_out_time,\n" +
            "\thouse_orders.create_at \n" +
            "FROM\n" +
            "\thouse_orders\n" +
            "\tLEFT JOIN houses ON house_orders.house_Id = houses.id \n" +
            "WHERE\n" +
            "\thouse_orders.open_id = #{openid}\n" +
            "\tAND DATE_SUB( CURDATE( ), INTERVAL 30 DAY ) <= house_orders.check_in_time")
    ArrayList<HOrder> getRecentHouseOrders(@Param("openid") String openid);

    /**
     * 获取所有餐厅订单
     *
     * @param openid
     * @return
     */
    @Select("SELECT * FROM restaurant_orders  where open_id = #{openid}")
    ArrayList<RestaurantOrder> getAllRestOrders(@Param("openid") String openid);

    /**
     * 获取所有房间订单
     *
     * @param openid
     * @return
     */
    @Select("SELECT\n" +
            "\thouse_orders.id,\n" +
            "\thouses.`name`,\n" +
            "\thouse_orders.room_number,\n" +
            "\thouses.money,\n" +
            "\thouse_orders.`status`,\n" +
            "\thouse_orders.check_in_time,\n" +
            "\thouse_orders.check_out_time,\n" +
            "\thouse_orders.create_at \n" +
            "FROM\n" +
            "\thouse_orders\n" +
            "\tLEFT  JOIN houses ON house_orders.house_Id = houses.id \n" +
            "WHERE\n" +
            "\topen_id = #{openid}")
    ArrayList<HOrder> getAllHouseOrders(@Param("openid") String openid);

    /**
     * 获取房间订单细节
     *
     * @param id
     * @return
     */
    @Select("SELECT\n" +
            "\tusers.real_name,\n" +
            "\thouse_orders.house_Id,\n" +
            "\thouses.room_number,\n" +
            "\thouse_orders.house_type,\n" +
            "\thouse_orders.phone,\n" +
            "\thouse_orders.check_in_time,\n" +
            "\thouse_orders.check_out_time \n" +
            "FROM\n" +
            "\thouse_orders\n" +
            "\tLEFT JOIN users ON users.id_number = house_orders.id_number \n" +
            "\tLEFT JOIN houses ON houses.room_number = house_orders.room_number\n" +
            "WHERE\n" +
            "\thouse_orders.id = #{id}\n" +
            "\tLIMIT 1")
    HouseOrderDetail getHouseOrderDetail(@Param("id") Integer id);

    /**
     * 获取餐厅订单细节
     *
     * @param id
     * @return
     */
    @Select("SELECT\n" +
            "\trestaurant_orders.is_delivery,\n" +
            "\tusers.real_name,\n" +
            "\trestaurant_orders.create_at,\n" +
            "\trestaurant_orders.check_in_time,\n" +
            "\trestaurant_orders.phone,\n" +
            "\trestaurant_orders.items,\n" +
            "\trestaurant_orders.`status`\n" +
            "FROM\n" +
            "\trestaurant_orders\n" +
            "\tLEFT JOIN users ON users.id_number = restaurant_orders.id_number \n" +
            "WHERE\n" +
            "\trestaurant_orders.id = #{ids}\n" +
            "\tLIMIT 1")
    RestaurantOrderDetail getRestOrderDetail(@Param("id") Integer id);

    /**
     * 更新房间订单
     *
     * @param houseOrder
     * @return
     */
    @Update("update house_orders set `status` = #{status},update_at = #{update_at} WHERE id = #{id}")
    Boolean updateHouseOrders(HouseOrder houseOrder);

    /**
     * 更新餐厅订单
     *
     * @param restaurantOrder
     * @return
     */
    @Update("update restaurant_orders set `status` = #{status},update_at = #{update_at} WHERE id = #{id}")
    Boolean updateRestOrders(RestaurantOrder restaurantOrder);


    /**
     * private Integer status;
     *
     * @param orders
     * @return
     */
    @Update(("update house_orders set house_type = #{house_type},house_id = #{house_id},check_in_time = #{check_in_time}," +
            "check_out_time = #{check_out_time},phone = #{phone},status=#{status},update_at = #{update_at} where id = #{id}"))
    Boolean updateOrderInfoOfHouse(HouseOrder orders);


    @Update("update restaurant_orders set is_delivery = #{is_delivery},items = #{items},phone = #{phone},check_in_time = #{check_in_time}" +
            "status = #{status},update_at = #{update_at} where id = id")
    Boolean updateOrderInfoOfRes(RestaurantOrder orders);


    /**
     * 获取当天入住人订单和照片
     *
     * @return
     */
    @Select("SELECT\n" +
            "\thouse_orders.id,\n" +
            "\thouse_orders.house_Id,\n" +
            "\tid_cards.recent \n" +
            "FROM\n" +
            "\thouse_orders\n" +
            "\tRIGHT JOIN id_cards ON id_cards.id_number = house_orders.id_number \n" +
            "WHERE\n" +
            "\thouse_orders.`status` = #{status} \n" +
            "\tAND DATE( check_in_time ) = CURDATE( )")
    ArrayList<OrderIdAndImgName> getOccupants(@Param("status") int status);

    /**
     * 获取今日离店人信息
     *
     * @return
     */
    @Select("SELECT\n" +
            "\thouse_orders.id,\n" +
            "\thouse_orders.house_Id,\n" +
            "\tid_cards.recent \n" +
            "FROM\n" +
            "\thouse_orders\n" +
            "\tRIGHT JOIN id_cards ON id_cards.id_number = house_orders.id_number \n" +
            "WHERE\n" +
            "\thouse_orders.`status` = #{status} \n" +
            "\tAND DATE( check_out_time ) = CURDATE( )")
    ArrayList<OrderIdAndImgName> getTDepartureInfo(@Param("status") int status);

    /**
     * 获取今日就餐人信息
     *
     * @return
     */
    @Select("SELECT\n" +
            "\trestaurant_orders.id,\n" +
            "\tid_cards.recent \n" +
            "FROM\n" +
            "\trestaurant_orders\n" +
            "\tRIGHT JOIN id_cards ON id_cards.id_number = restaurant_orders.id_number \n" +
            "WHERE\n" +
            "\trestaurant_orders.`status` = #{status} \n" +
            "\tAND DATE( check_in_time ) = CURDATE( )\n")
    ArrayList<OrderIdAndImgName> getTDiningInfo(@Param("status") int status);

    /**
     * 插入餐厅订单
     *
     * @param orders
     */
    @Insert("insert into restaurant_orders (is_delivery,id_number,items,phone,check_in_time,status,create_at,open_id) " +
            "values (#{is_delivery},#{id_number},#{items},#{phone},#{check_in_time},#{status},#{create_at},#{open_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addRestOrder(RestaurantOrder orders);

    /**
     * 获取餐厅位置
     *
     * @return
     */
    @Select("SELECT\n" +
            "\tCOUNT( id ) \n" +
            "FROM\n" +
            "\trestaurant_orders \n" +
            "WHERE\n" +
            "\tis_delivery = 0 \n" +
            "\tAND STATUS = #{status1} \n" +
            "\tAND DATE ( check_in_time ) = DATE( #{check_in_time} ) \n" +
            "\tOR ( HOUR ( check_in_time ) + 2 > HOUR ( NOW( ) ) AND `status` = #{status2} )")
    Integer getRestOrderCount(@Param("status1") int status1, @Param("status2") int status2, @Param("check_in_time") Date check_in_time);


    /**
     * 预定酒店房间
     *
     * @param houseOrder
     * @return
     */

    @Insert("insert into house_orders (id_number,house_id,house_type,check_in_time,check_out_time,phone,status,create_at,open_id,room_number) " +
            "values (#{id_number},#{house_id},#{house_type},#{check_in_time},#{check_out_time},#{phone},#{status},#{create_at},#{open_id},#{room_number})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    boolean addHouseOrder(HouseOrder houseOrder);

    /**
     * 找到可预定房间
     *
     * @return
     */
    @Select("select distinct houses.id,houses.room_number,houses.name,houses.money,houses.description " +
            "from houses left JOIN house_orders on houses.id = house_orders.house_Id " +
            "where " +
            "(((houses.status = 3 AND Date(check_in_time) >= CURDATE() AND Date(check_out_time) >= CURDATE() AND " +
            "((Date(check_in_time) < #{start} AND  Date(check_out_time) < #{start}) or (Date(check_in_time) > #{end} AND " +
            "Date(check_out_time) > #{end} ))) or " +
            "ISNULL(check_in_time)) or houses.status = 1 ) and (houses.status = 3 or houses.status = 1 ) and houses.type = #{houseType} " +
            "limit 0,1"
    )
    List<House> getInHouse(@Param("start") Date start, @Param("end") Date end, @Param("houseType") int houseType);


    @Select("select * from house_orders where id = #{id}")
    HouseOrder getInfoById(@Param("id") int id);


    @Delete("delete from restaurant_orders where id = #{id}")
    Boolean deleteRestOrder(@Param("id") int id);

    @Select("select * from restaurant_orders where id = #{id}")
    RestaurantOrder getRestOrderById(@Param("id") int id);


    @Select("SELECT * from restaurant_orders LIMIT #{start},#{end}")
    ArrayList<RestaurantOrder> getRestOrders(@Param("start") int start, @Param("end") int end);

    @Select("SELECT * from restaurant_orders WHERE id_number = #{id} LIMIT  #{start},#{end}")
    ArrayList<RestaurantOrder> SearchRestOrdersById(@Param("id") String id, @Param("start") int start, @Param("end") int end);

    @Select("SELECT COUNT(id) FROM `restaurant_orders`")
    Integer getRestOrdersCount();

    @Select("SELECT COUNT(id) FROM `restaurant_orders` where id_number = #{id} ")
    Integer SearchRestOrdersByIdCount(@Param("id") String id);

    @Select("<script>" +
            "select id,id_number,house_type,check_in_time,check_out_time,phone,room_number from house_orders " +
            "where status in (0,3,4)" +
            "<if test='num !=null'> " +
            "and id_number = #{num} " +
            "</if> " +
            "</script>")
    ArrayList<HouseOrder> searchOrder(@Param("num") String num);

    @Delete("delete from house_orders where id = #{id}")
    boolean deleteOrderById(@Param("id") int id);


    @Select("select id from house_orders where house_id = #{id} and status in (3,4) limit 0,1")
    Integer getHouseOrderByHouseId(@Param("id") Integer id);

    @Update("update house_orders set house_id = 1 where id = #{param1}")
    boolean updateHouseId(int id);
}

