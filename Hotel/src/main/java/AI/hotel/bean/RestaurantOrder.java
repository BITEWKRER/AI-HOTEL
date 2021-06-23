package AI.hotel.bean;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class RestaurantOrder implements Serializable {

    private Integer id;
    private char is_delivery;
    private String id_number;
    private String items;
    private String open_id;
    private String phone;
    private Date check_in_time;
    private Integer money;
    private Integer status;
    private Date create_at;
    private Date update_at;




    @Override
    public String toString() {
        return "{ \"id\":" + id
                + ",\"id_number\":\"" + id_number
                + "\",\"is_delivery\":" + is_delivery
                + ",\"items\":\"" + items
                + "\",\"check_in_time\":\"" + check_in_time
                + "\",\"phone\":" + phone
                + ",\"money\":" + money
                + "\",\"status\":" + status
                + ",\"create_at\":\"" + create_at
                + "\",\"update_at\":\"" + update_at
                + "\"}";
    }
}
