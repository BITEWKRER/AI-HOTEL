package AI.hotel.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单状态
 * 0 已过期
 * 1 预定失败
 * 2 已预定
 * 3 已使用
 * 4 已取消
 */
@Component
@Data
public class HOrder implements Serializable {
    private Integer id;
    private String name;
    private Integer room_number;
    private Integer money;
    private Integer status;
    private Date check_in_time;
    private Date check_out_time;
    private Date create_at;

    @Override
    public String toString() {
        return "{ \"id\":" + id
                + ",\"name\":\"" + name
                + "\",\"room_number\":" + room_number
                + ",\"money\":" + money
                + ",\"status\":" + check_in_time
                + ",\"check_in_time\":\"" + check_out_time
                + "\",\"check_out_time\":\"" + check_out_time
                + ",\"create_at\":\"" + create_at
                + "\"}";
    }

}
