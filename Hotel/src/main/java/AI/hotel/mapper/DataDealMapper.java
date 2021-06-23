package AI.hotel.mapper;


import AI.hotel.bean.DataResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DataDealMapper {


    @Select("select count(*) from house_orders where status = 3 and Date(check_in_time) = CURDATE()")
    int getCountReserve();

    @Select("select count(*) from house_orders where status = 4 and Date(check_in_time) = CURDATE()")
    int getCountCheckIn();

    @Select("select count(*) from houses where status not in (0,2,4,999)")
    int getLeisure();

    @Select("select count(*) from houses where status = 0")
    int getClear();

    @Select("select count(*) from houses where status = 4")
    int getRepair();


    @Select("select check_in_time as date,SUM(money) as money from house_orders  left join houses on houses.room_number = house_orders.room_number where " +
            "house_orders.status not in (1,2) and DATE_SUB(CURDATE(),INTERVAL 7 day) <= date(check_in_time) group by DATE_FORMAT(check_in_time,'%y%m%d') ")
    List<DataResult> getRecent7Data();

    @Select("select COALESCE(SUM(money),0) from house_orders  left join houses on houses.room_number = house_orders.room_number where " +
            "house_orders.status not in (1,2) and  CURDATE() = date(check_in_time) ")
    int getDayMoney();

    @Select("select COALESCE(SUM(money),0) from house_orders  left join houses on houses.room_number = house_orders.room_number where " +
            "house_orders.status not in (1,2) and YEARWEEK(date_format(check_in_time,'%Y-%m-%d')) = YEARWEEK(now()) ")
    int getWeekMoney();

    @Select("select COALESCE(SUM(money),0) from house_orders  left join houses on houses.room_number = house_orders.room_number where " +
            "house_orders.status not in (1,2) and date_format(now(),'%Y%m') = date_format(check_in_time,'%Y%m') ")
    int getMonthMoney();

    @Select("select b.name,count(*) as count from house_orders as a left join houses as b on b.type = a.house_type  where " +
            "a.status not in (1,2) and DATE_SUB(CURDATE(),INTERVAL 30 day) <= date(check_in_time) " +
            "GROUP BY house_type ")
    List<DataResult> getDataResult();

    @Select("select count(*) from house_orders as a where a.status not in (1,2) and DATE_SUB(CURDATE(),INTERVAL 30 day) <= date(check_in_time)")
    int getMonCount();

}
