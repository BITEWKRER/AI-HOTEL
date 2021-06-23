package AI.hotel.controller;

import AI.hotel.bean.ConsumeRecord;
import AI.hotel.service.Impl.ConsumeRecordsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import static AI.hotel.utils.toJson.toJsonArray;
import static AI.hotel.utils.toJson.toJsonString;


@RestController
@RequestMapping("/phone")
public class ConsumeRecordsController {

    @Autowired
    private ConsumeRecordsServiceImpl orderService;

    /**
     * 获取所有未支付订单
     *
     * @param id_number
     * @return
     */
    @GetMapping("/getAllNotPayOrders/{id_number}")
    public String getAllNotPayOrders(@PathVariable("id_number") String id_number) {
        ArrayList<ConsumeRecord> allNotPayOrders = orderService.getAllNotPayOrders(id_number);
        if (allNotPayOrders != null) {
            return toJsonArray(200, "获取所有未支付订单成功", allNotPayOrders);
        } else {
            return toJsonString(100, "获取所有未支付订单成功", null);
        }
    }

    /**
     * 更新未支付订单状态
     *
     * @param id_number
     * @return
     */
    @PutMapping("/updateNotPayOrdersStatus/{id_number}")
    public String updateNotPayOrdersStatus(@PathVariable("id_number") String id_number) {
        if (orderService.updateNotPayOrdersStatus(id_number)) {
            return toJsonString(200, "更新未支付账单状态成功", null);
        } else {
            return toJsonString(100, "更新未支付账单状态失败", null);
        }
    }

    /**
     * 删除订单记录
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteRecord/{id}")
    public String deleteRecord(@PathVariable("id") int id) {
        if (orderService.deleteRecord(id)) {
            return toJsonString(200, "删除消费记录成功", null);
        } else {
            return toJsonString(100, "删除消费记录失败", null);
        }
    }

    /**
     * 添加消费记录
     *
     * @param consumeRecord
     * @return
     */
    @PostMapping("/addRecord")
    public String addRecord(ConsumeRecord consumeRecord) {
        if (orderService.addRecord(consumeRecord)) {
            return toJsonString(200, "添加账单成功", null);
        } else {
            return toJsonString(100, "添加账单失败", null);
        }
    }


}
