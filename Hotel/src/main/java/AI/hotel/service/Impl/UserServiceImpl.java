package AI.hotel.service.Impl;


import AI.hotel.bean.UserMessage;
import AI.hotel.bean.User;
import AI.hotel.mapper.UserMapper;
import AI.hotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper mapper;

    @Override
    public ArrayList<User> getAll() {
        return mapper.getAll();
    }

    @Override
    public Boolean addUser(UserMessage userMessage) {
        return mapper.addUser(userMessage);
    }

    @Override
    public Boolean addIdCard(UserMessage userMessage) {
        return mapper.addIdCard(userMessage);
    }

    @Override
    public List<User> getUsersByOpen(String open_id) {
        return mapper.getUsersByOpen(open_id);
    }

    @Override
    public String getIdByHouseID(int id) {
        return mapper.getIdByHouseID(id);
    }



    public List<User> getUsersByOpenNum(String openId, String idNumber) {
        return mapper.getUserByOpenNum(openId, idNumber);
    }
}
