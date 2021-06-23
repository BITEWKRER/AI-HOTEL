package AI.hotel.mapper;

import AI.hotel.bean.HouseOrder;
import AI.hotel.bean.RestaurantOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
/**
 *  房间订单表
 *  房间预留3个小时
 *  餐厅座位预留1个小时
 */
public interface DealExpiredOrderMapper {

    /**
     * 获取过期订单
     *
     * @return
     */
    @Select("SELECT\n" +
            "\thouse_orders.id \n" +
            "FROM\n" +
            "\thouse_orders\n" +
            "\tRIGHT JOIN houses ON houses.id = house_orders.house_id \n" +
            "WHERE\n" +
            "\thouses.type BETWEEN #{start} \n" +
            "\tAND #{end} \n" +
            "\tAND house_orders.`status` = #{status} \n" +
            "\tAND DATE( check_in_time ) = CURDATE( )")
    ArrayList<HouseOrder> getAllDestineOrders(@Param("start") int start, @Param("end") int end, @Param("status") int status);


    /**
     * 处理今日之前过期订单
     *
     * @return
     */
    @Select("SELECT\n" +
            "\thouse_orders.id \n" +
            "FROM\n" +
            "\thouse_orders\n" +
            "\tRIGHT JOIN houses ON houses.id = house_orders.house_id \n" +
            "WHERE\n" +
            "\thouses.type BETWEEN #{start} \n" +
            "\tAND #{end} \n" +
            "\tAND house_orders.`status` = #{status} \n" +
            "\tAND DATE( house_orders.check_in_time ) < CURDATE( )\t")
    ArrayList<HouseOrder> dealExpiredOrder(@Param("start") int start, @Param("end") int end, @Param("status") int status);


    /**
     * 处理今日之前过期餐厅订单
     */
    @Select("SELECT\n" +
            "\tid\n" +
            "FROM\n" +
            "\trestaurant_orders\n" +
            "WHERE\n" +
            "\trestaurant_orders.`status` = #{status}\n" +
            "\tAND DATE( check_in_time ) < CURDATE( )")
    ArrayList<RestaurantOrder> dealExpiredRestOrder(@Param("status") int status);

    /**
     * 获取所有餐厅过期订单
     *
     * @return
     */
    @Select("SELECT\n" +
            "\tid\n" +
            "FROM\n" +
            "\trestaurant_orders\n" +
            "WHERE\n" +
            "\trestaurant_orders.`status` = #{status}\n" +
            "\tAND DATE( check_in_time ) = CURDATE( ) \n" +
            "\tAND HOUR ( check_in_time ) + 1 < HOUR ( NOW( ) )")
    ArrayList<RestaurantOrder> getAllRestDestineOrders(@Param("status") int status);


    /**
     * 更新订单状态
     *
     * @param houseOrder
     */
    @Update("update house_orders set `status` = #{status},update_at = #{update_at},house_id = #{house_id} WHERE id = #{id}")
    void updateDestineOrders(HouseOrder houseOrder);


    /**
     * 更新餐厅订单状态
     *
     * @param restaurantOrder
     */
    @Update("update restaurant_orders set `status` = #{status},update_at = #{update_at} WHERE id = #{id}")
    void updateRestDestineOrders(RestaurantOrder restaurantOrder);

}
