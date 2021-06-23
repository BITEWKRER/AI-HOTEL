package AI.hotel.controller;


import AI.hotel.bean.DataResult;
import AI.hotel.service.Impl.DataDealServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static AI.hotel.utils.toJson.toJsonArray;
import static AI.hotel.utils.toJson.toJsonString;

@RestController
public class DataDealController {


    @Autowired
    DataDealServiceImpl dataDealService;

    @GetMapping("/getRecent7Data")
    public String getEmployee() {
        List<DataResult> dataResultList = dataDealService.getRecent7Data();
        if (!dataResultList.isEmpty()) {
            return toJsonArray(200, "获取近七天的销售额详情成功", dataResultList);
        } else {
            return toJsonString(100, "获取信息失败", false);
        }
    }

    @GetMapping("/getMonRate")
    public String getMonRate() {
        List<DataResult> dataResultList = dataDealService.getMonRate();
        if (!dataResultList.isEmpty()) {
            return toJsonArray(200, "获取近30天的住房情况成功", dataResultList);
        } else {
            return toJsonString(100, "获取信息失败", false);
        }
    }

    @GetMapping("/getDataStatistic")
    public String getDataStatistic() {
        Map<String, Integer> map = new HashMap<>();
        map.put("reserve", dataDealService.getCountReserve());
        map.put("checkIn", dataDealService.getCountCheckIn());
        map.put("leisure", dataDealService.getLeisure());
        map.put("clear", dataDealService.getClear());
        map.put("repair", dataDealService.getRepair());
        map.put("daySale", dataDealService.getDayMoney());
        map.put("weekSale", dataDealService.getWeekMoney());
        map.put("monSale", dataDealService.getMonMoney());
        if (!map.isEmpty()) {
            return toJsonArray(200, "获取数据成功", map);
        } else {
            return toJsonString(100, "获取数据失败", false);
        }
    }


}
