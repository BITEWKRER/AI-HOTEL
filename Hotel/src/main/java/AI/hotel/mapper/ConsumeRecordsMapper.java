package AI.hotel.mapper;

import AI.hotel.bean.ConsumeRecord;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface ConsumeRecordsMapper {

    /**
     * 获取所有未支付消费记录
     *
     * @param id_number
     * @return
     */
    @Select("select * from consume_records WHERE is_pay = 0 AND id_number = #{id_number}")
    ArrayList<ConsumeRecord> getAllNotPayOrders(@Param("id_number") String id_number);

    /**
     * 获取所有消费记录
     *
     * @param id_number
     * @return
     */
    @Select("select * from consume_records WHERE id_number = #{id_number}")
    ArrayList<ConsumeRecord> getAllConsumeOrders(@Param("id_number") String id_number);

    /**
     * 模拟结账，一次结清
     *
     * @param id_number
     * @return
     */
    @Update("update consume_records set is_pay = 1,update_at = now()  where id_number = #{id_number}")
    Boolean updateNotPayOrdersStatus(@Param("id_number") String id_number);

    /**
     * 删除消费记录
     *
     * @param id
     * @return
     */
    @Delete("DELETE from consume_records where id = #{id}")
    Boolean deleteRecord(@Param("id") int id);

    /**
     * 插入消费记录
     *
     * @param consumeRecord
     * @return
     */
    @Insert("insert into consume_records (id_number,item_name,item_money,description,is_pay,create_at) " +
            "values (#{id_number},#{item_name},#{item_money},#{description},#{is_pay},#{create_at})")
    Boolean addRecord(ConsumeRecord consumeRecord);
}
