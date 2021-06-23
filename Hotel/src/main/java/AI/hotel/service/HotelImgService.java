package AI.hotel.service;

import AI.hotel.bean.HotelImg;

import java.util.List;
import java.util.Map;

public interface HotelImgService {

    Boolean addHotelImg(HotelImg hotelImg);

    Boolean deleteHotelImg(int id);

    List getHotelImg(int type);

    Boolean updateMenuItemImg(HotelImg hotelImg);

    Map<String, Object> getAllTypeImg();
}
