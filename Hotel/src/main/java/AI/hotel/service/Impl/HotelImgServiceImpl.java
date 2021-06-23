package AI.hotel.service.Impl;

import AI.hotel.bean.HotelImg;
import AI.hotel.mapper.HotelImgMapper;
import AI.hotel.service.HotelImgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static AI.hotel.statusCode.HouseTypeCode.*;
import static AI.hotel.utils.FileUtils.deleteFile;


@Service
@Slf4j
public class HotelImgServiceImpl implements HotelImgService {

    @Autowired
    private HotelImgMapper mapper;

    @Override
    public Boolean addHotelImg(HotelImg hotelImg) {
        hotelImg.setCreate_at(new Date());
        return mapper.addHotelImg(hotelImg);
    }

    @Override
    public Boolean deleteHotelImg(int id) {
        deleteFile("imgs", mapper.getPathById(id));
        return mapper.deleteHotelImg(id);
    }

    @Override
    public List<HotelImg> getHotelImg(int type) {
        return mapper.getHotelImg(type);
    }


    @Override
    public Boolean updateMenuItemImg(HotelImg hotelImg) {
        deleteFile("imgs", mapper.getPathById(hotelImg.getId()));
        return mapper.updateMenuItemImg(hotelImg);
    }


    @Override
    public Map<String, Object> getAllTypeImg() {
        Map<String, Object> path = new HashMap<>();

        for (int i = HotelEnvironment; i <= Rest; i++) {
            Map<String, Object> tmp = new HashMap<>();
            List<HotelImg> img = mapper.getHotelImg(i);
            tmp.put("name", getName(i));
            tmp.put("house_type", i);
            tmp.put("data", img);
            path.put(String.valueOf(i), tmp);
        }
        return path;
    }

    private String getName(int i) {
        String name = "未知";
        switch (i) {
            case HotelEnvironment:
                name = "酒店环境";
                break;
            case KingBedRoom:
                name = "大床房";
                break;
            case DoubleRoom:
                name = "双人房";
                break;
            case Suite:
                name = "套房";
                break;
            case ThemeRoom:
                name = "主题房";
                break;
            case SwimmingPool:
                name = "游泳池";
                break;
            case MeetingRoom:
                name = "会议室";
                break;
            case KTV:
                name = "KTV";
                break;
            case ChessRoom:
                name = "棋牌室";
                break;
            case Gym:
                name = "健身房";
                break;
            case Rest:
                name = "餐厅";
                break;
            default:
                break;
        }
        return name;
    }

}
