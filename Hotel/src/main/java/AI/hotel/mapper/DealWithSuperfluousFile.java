package AI.hotel.mapper;

import AI.hotel.bean.IdCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface DealWithSuperfluousFile {

    @Select("SELECT path FROM `hotel_imgs`")
    ArrayList<String> getHotelImgPath();

    @Select("SELECT path FROM menu")
    ArrayList<String> getMenuImgPath();

    @Select("SELECT front,back,recent FROM `id_cards`")
    ArrayList<IdCard> getIdCardPath();



}
