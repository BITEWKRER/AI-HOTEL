package AI.hotel.service.Impl;

import AI.hotel.bean.Menu;
import AI.hotel.mapper.MenuMapper;
import AI.hotel.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static AI.hotel.utils.FileUtils.deleteFile;


@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper mapper;

    @Override
    public boolean addMenuItem(Menu menu) {
        menu.setCreate_at(new Date());
        return mapper.addMenuItem(menu);
    }

    @Override
    public boolean updateMenuItem(Menu menu) {
        deleteFile("DishImgs",mapper.getPathById(menu.getId()));
        return mapper.updateMenuItem(menu);
    }

    @Override
    public boolean deleteMenuItem(int id) {
        deleteFile("DishImgs",mapper.getPathById(id));
        return mapper.deleteMenuItem(id);

    }

    @Override
    public ArrayList<Menu> getMenuItems() {
        return mapper.getMenuItems();
    }

    @Override
    public Map<String, Object> getMenus(Integer page) {
        return resultCollection(page, "all", null);
    }

    @Override
    public Map<String, Object> searchMenuItemByName(String name, Integer page) {
        return resultCollection(page, "search", name);
    }

    private Map<String, Object> resultCollection(Integer page, String type, @Nullable String name) {
        page -= 1;
        int pageNumber = 10;
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("pageNumber", pageNumber);
        //数据总条数，当前页码。每页多少条
        switch (type) {
            case "search":
                map.put("total", mapper.SearchMenuByNameCount(name));
                map.put("data", mapper.SearchMenuByName(name, page * pageNumber, (page * pageNumber) + pageNumber));
                break;
            case "all":
                map.put("total", mapper.getMenusCount());
                map.put("data", mapper.getMenus(page * pageNumber, (page * pageNumber) + pageNumber));
                break;
            default:
                break;
        }
        return map;
    }



}
