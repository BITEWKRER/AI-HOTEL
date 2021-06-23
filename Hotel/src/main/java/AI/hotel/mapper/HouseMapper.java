package AI.hotel.mapper;

import AI.hotel.bean.Condition;
import AI.hotel.bean.House;
import AI.hotel.bean.HouseOrder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Mapper
@Repository
public interface HouseMapper {

    /**
     * 更新房间订单状态
     *
     * @param id
     * @param status
     * @return
     */
    @Update("update houses set status = #{status} where id  = #{id} and status != 999")
    Boolean updateHouseStatus(@Param("id") int id, @Param("status") int status);

    /**
     * 插入一条房间信息
     *
     * @param house
     * @return
     */
    @Insert("insert into houses (room_number,name,type,money,description,area,status,create_at) " +
            "values (#{room_number},#{name},#{type},#{money},#{description},#{area},#{status},#{create_at})")
    Boolean addHouse(House house);

    /**
     * 更新房间信息
     *
     * @param house
     * @return
     */
    @Update("update houses set room_number = #{room_number},name = #{name},type = #{type},money=#{money},area = #{area}," +
            "status = #{status},update_at = now() where id = #{id}")
    Boolean updateHouseInfo(House house);


    /**
     * 删除房间
     *
     * @param id
     * @return
     */
    @Delete("delete from houses where id = #{id}")
    Boolean deleteHouseInfo(@Param("id") int id);


    @Select("select id,room_number,name,type,money,area,status,description from houses where type = #{type}")
    ArrayList<House> getHouseByType(@Param("type") int type);


    @Select("select id,room_number,name,type,money,area,status,description from houses where id = #{id}")
    ArrayList<House> getHouseById(@Param("id") int id);


    @Select("<script>" +
            "select distinct houses.id,houses.room_number,name,money,houses.type,area,houses.status,houses.description,path " +
            "from houses left JOIN house_orders on houses.id = house_orders.house_Id left join (select house_type,path " +
            "from hotel_imgs group by house_type) a on houses.type = a.house_type " +
            "where " +
            "(((houses.status = 3 AND Date(check_in_time) &gt;= CURDATE() AND Date(check_out_time) &gt;= CURDATE() AND " +
            "((Date(check_in_time) &lt; #{start} AND  Date(check_out_time) &lt; #{start}) or (Date(check_in_time) &gt; #{end} AND " +
            "Date(check_out_time) &gt; #{end} ))) or " +
            "ISNULL(check_in_time)) or houses.status = 1 ) and (houses.status = 3 or houses.status = 1 ) " +
            "AND houses.type not in (106,107,108,109,110,111) " +
            "AND (money &gt;= #{moneyMin} and money &lt;= #{moneyMax}) " +
            "<if test='name!=null'> AND INSTR(name,#{name})</if> " +
            "<if test='fiter!=null'> " +
            "<foreach item='item' collection='fiter' open='AND (' close = ')' separator='AND'> " +
            "INSTR(description,#{item}) " +
            "</foreach> " +
            "</if> " +
            "<if test='order==\"up\"'>order by money ASC</if> " +
            "<if test='order==\"down\"'>order by money desc</if> " +
            "</script>")
    LinkedList<House> searchHouse(Condition condition);


    @Select("SELECT * FROM houses WHERE ID = #{id}")
    House getInfoById(@Param("id") int id);

    @Select("select a.id,a.name,a.type,b.path " +
            "from " +
            "houses as a left join hotel_imgs as b on a.type = b.house_type " +
            "where a.type in (106,107,108,109,110,111 ) GROUP BY a.type")
    List<House> getActiveHouse();


    @Select("SELECT\n" +
            "\tid,\n" +
            "\troom_number,\n" +
            "\ttype,\n" +
            "\t`status` \n" +
            "FROM\n" +
            "\t`houses` where status in (0,1,2,3,4)")
    ArrayList<House> getHouseStatus();

    @Select("select id from houses where room_number = #{param1}")
    Integer getHouseByRoomNumber(int roomNumber);




}
