package AI.hotel.mapper;

import AI.hotel.bean.HotelImg;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HotelImgMapper {

    @Insert("insert into hotel_imgs (house_type,path,create_at) values(#{house_type},#{path},#{create_at}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Boolean addHotelImg(HotelImg hotelImg);

    @Delete("delete from hotel_imgs where id = #{id}")
    Boolean deleteHotelImg(@Param("id") int id);


    @Select("select id,house_type,path from hotel_imgs where house_type = #{type} order by  create_at desc limit 0,5")
    List<HotelImg> getHotelImg(@Param("type") int type);


    @Select("select path from hotel_imgs where id = #{id}")
    String getPathById(@Param("id") int id);

    @Update("update hotel_imgs set path = #{path},update_at = now() where id = #{id}")
    Boolean updateMenuItemImg(HotelImg hotelImg);



}
