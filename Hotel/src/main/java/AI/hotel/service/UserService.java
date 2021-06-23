package AI.hotel.service;


import AI.hotel.bean.UserMessage;
import AI.hotel.bean.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {

    ArrayList<User> getAll();

    Boolean addUser(UserMessage userMessage);

    Boolean addIdCard(UserMessage userMessage);

    List<User> getUsersByOpen(String openId);

    String getIdByHouseID(int id);
}
