package AI.hotel.service;

import AI.hotel.bean.Condition;
import AI.hotel.bean.House;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface HouseService {

    boolean updateHouseStatus(int id, int status);

    boolean addHouse(House house);

    boolean updateHouseInfo(House house);

    boolean deleteHouseInfo(int id);

    ArrayList<House> getHouseByType(int type);

    ArrayList<House> getHouseById(int id);

    List<House> searchHouse(Condition condition);

    List<House> getActiveHouse();

    Map<String, ArrayList<House>> getHouseStatus();


}
