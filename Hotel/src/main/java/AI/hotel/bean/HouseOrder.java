package AI.hotel.bean;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Data
@Component
public class HouseOrder implements Serializable {

    private Integer id;
    private String id_number;
    private Integer house_type;
    private Integer house_id;
    private String open_id;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date check_in_time;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date check_out_time;
    private String phone;
    private Integer status;
    private Date create_at;
    private Date update_at;
    private Integer room_number;

    @Override
    public String toString() {
        return "{ \"id\":" + id
                + ",\"id_number\":\"" + id_number
                + "\",\"house_type\":" + house_type
                + ",\"house_id\":" + house_id
                + ",\"check_in_time\":\"" + check_in_time
                + "\",\"check_out_time\":\"" + check_out_time
                + "\",\"phone\":\"" + phone
                + "\",\"status\":" + status
                + ",\"create_at\":\"" + create_at
                + "\",\"update_at\":\"" + update_at
                + "\",\"room_number\":\"" + room_number
                + "\"}";
    }


}
