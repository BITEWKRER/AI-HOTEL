package AI.hotel.service;

import AI.hotel.bean.Menu;

import java.util.ArrayList;
import java.util.Map;

public interface MenuService {

    boolean addMenuItem(Menu menu);


    boolean updateMenuItem(Menu menu);


    boolean deleteMenuItem(int id);


    ArrayList<Menu> getMenuItems();

    Map<String, Object> getMenus(Integer page);

    Map<String, Object> searchMenuItemByName(String name, Integer page);
}
