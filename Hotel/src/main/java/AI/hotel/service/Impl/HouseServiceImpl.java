package AI.hotel.service.Impl;

import AI.hotel.bean.Condition;
import AI.hotel.bean.House;
import AI.hotel.mapper.HouseMapper;
import AI.hotel.service.HouseService;
import AI.hotel.statusCode.HousesCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper mapper;

    @Override
    public boolean updateHouseStatus(int id, int status) {
        return mapper.updateHouseStatus(id, status);
    }

    @Override
    public boolean addHouse(House house) {
        house.setStatus(HousesCode.NotUse);
        house.setCreate_at(new Date());
        return mapper.addHouse(house);
    }

    public boolean isTrueAdd(House houses) {
        return mapper.getHouseByRoomNumber(houses.getRoom_number()) == null;
    }

    @Override
    public boolean updateHouseInfo(House house) {
        return mapper.updateHouseInfo(house);
    }

    @Override
    public ArrayList<House> getHouseByType(int type) {
        return mapper.getHouseByType(type);
    }

    @Override
    public ArrayList<House> getHouseById(int id) {
        return mapper.getHouseById(id);
    }

    @Override
    public LinkedList<House> searchHouse(Condition condition) {
        return mapper.searchHouse(condition);
    }

    @Override
    public List<House> getActiveHouse() {
        return mapper.getActiveHouse();
    }

    @Override
    public Map<String, ArrayList<House>> getHouseStatus() {
        Map<String, ArrayList<House>> map = new HashMap<>();
        ArrayList<House> status = mapper.getHouseStatus();

        for (int i = 0; i < status.size(); i++) {
            if (status.get(i).getRoom_number() != null) {
                int floor = status.get(i).getRoom_number() / 100;
                ArrayList<House> tmp;
                if (map.get(String.valueOf(floor)) != null) {
                    tmp = map.get(String.valueOf(floor));
                } else {
                    tmp = new ArrayList<>();
                }
                tmp.add(status.get(i));
                map.put(String.valueOf(floor), tmp);
            }
        }
        return map;
    }


    @Override
    public boolean deleteHouseInfo(int id) {
        return mapper.deleteHouseInfo(id);
    }
}
