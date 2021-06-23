package AI.hotel.controller;

import AI.hotel.bean.Menu;
import AI.hotel.service.Impl.MenuServiceImpl;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static AI.hotel.utils.toJson.toJsonArray;
import static AI.hotel.utils.toJson.toJsonString;

@RestController
@RequestMapping("/web")
public class MenuController {

    @Autowired
    private MenuServiceImpl menuService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/addMenuItem")
    public String addMenuItem(Menu menu) {
        menu.setCreate_at(new Date());
        if (menuService.addMenuItem(menu)) {
            update(null);
            return toJsonArray(200, "添加菜单条目成功", menu);
        }
        return toJsonString(100, "添加菜单条目失败", null);
    }

    /**
     * 二次开发，直接全部修改
     *
     * @return
     */
    @PutMapping("/updateMenuItem")
    public String updateMenuItem(Menu menu) {
        menu.setUpdate_at(new Date());
        if (menuService.updateMenuItem(menu)) {
            update(null);
            return toJsonArray(200, "更新菜单条目成功", menu.getPath());
        } else {
            return toJsonString(100, "更新菜单条目失败", null);
        }
    }


    @DeleteMapping("/deleteMenuItem")
    public String deleteMenuItem(int id) {
        if (menuService.deleteMenuItem(id)) {
            update(null);
            return toJsonString(200, "删除菜单条目成功", null);
        } else {
            return toJsonString(100, "删除菜单条目失败", null);
        }
    }

    @GetMapping("/getMenuItems")
    public String getMenuItems() {
        if (redisTemplate.hasKey("MenuDetail")) {
            return toJsonArray(200, "获取菜单成功", redisTemplate.opsForValue().get("MenuDetail"));
        }
        ArrayList<Menu> items = menuService.getMenuItems();
        update(items);
        return toJsonArray(200, "获取菜单成功", items);
    }

    // web端获取所有menu
    @GetMapping("getMenus/{page}")
    public String getMenus(@PathVariable Integer page) {
        Map<String, Object> menus = menuService.getMenus(page);
        if (menus.get("data") != null) {
            return toJsonArray(200, "获取菜单成功", menus);
        }
        return toJsonArray(100, "获取菜单失败", null);

    }

    //搜索 -name
    @GetMapping("searchMenuItemByName/{name}/{page}")
    public String searchMenuItemByName(@PathVariable String name, @PathVariable Integer page) {
        Map<String, Object> menus = menuService.searchMenuItemByName(name, page);
        if (menus.get("data") != null) {
            return toJsonArray(200, "搜索菜单成功", menus);
        }
        return toJsonArray(100, "搜索菜单失败", null);
    }


    @Async
    void update(@Nullable ArrayList<Menu> items) {
        if (items == null) {
            items = menuService.getMenuItems();
        }
        redisTemplate.opsForValue().set("MenuDetail", items);
    }

}

