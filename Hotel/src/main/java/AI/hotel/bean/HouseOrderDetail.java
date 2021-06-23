package AI.hotel.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class HouseOrderDetail implements Serializable {

    private String real_name;
    private Integer house_Id;
    private Integer house_type;
    private String phone;
    private Date check_in_time;
    private Date check_out_time;
    private Integer room_number;


    @Override
    public String toString() {
        return "{ \"real_name\":\"" + real_name
                + "\",\"house_Id\":\"" + house_Id
                + "\",\"house_type\":" + house_type
                + ",\"check_in_time\":\"" + check_in_time
                + "\",\"check_out_time\":\"" + check_out_time
                + "\",\"phone\":\"" + phone
                + "\",\"room_number\":\"" + room_number
                + "\"}";
    }
}
