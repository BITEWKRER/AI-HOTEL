package AI.hotel.mapper;


import AI.hotel.bean.UserMessage;
import AI.hotel.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    @Select("select * from users")
    ArrayList<User> getAll();

    @Select("select * from users where open_id = #{openId}")
    List<User> getUsersByOpen(@Param("openId") String openId);

    @Insert("insert into id_cards (id_number,front,back,recent,create_at) " +
            "values (#{idNumber},#{front},#{back},#{recent},#{createAt})")
    Boolean addIdCard(UserMessage userMessage);

    @Insert("insert into users (real_name,id_number,open_id,create_at) " +
            "values (#{realName},#{idNumber},#{open_id},#{createAt})")
    Boolean addUser(UserMessage userMessage);

    @Select("select * from users where open_id = #{openId} and id_number = #{num}")
    List<User> getUserByOpenNum(@Param("openId") String openId, @Param("num") String num);

    /**
     * 通过id查找id_number
     *
     */
    @Select("select id_number from house_orders where id = #{id}")
    String getIdByHouseID(@Param("id") int id);
}
