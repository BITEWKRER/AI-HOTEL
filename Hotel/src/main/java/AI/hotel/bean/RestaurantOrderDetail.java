package AI.hotel.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class RestaurantOrderDetail implements Serializable {
    private char is_delivery;
    private String real_name;
    private Date create_at;
    private Date check_in_time;
    private String phone;
    private String items;
    private Integer status;

    @Override
    public String toString() {
        return "{ \"real_name\":\"" + real_name
                + "\",\"is_delivery\":" + is_delivery
                + ",\"items\":\"" + items
                + "\",\"check_in_time\":\"" + check_in_time
                + "\",\"phone\":\"" + phone
                + "\",\"status\":" + status
                + ",\"create_at\":\"" + create_at
                + "\"}";
    }


}
