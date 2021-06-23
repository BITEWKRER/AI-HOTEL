package AI.hotel.bean;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class HotelImg implements Serializable {

    private Integer id;
    private Integer house_type;
    private String path;
    private Date create_at;
    private Date update_at;


    @Override
    public String toString() {
        return "{ \"id\":" + id
                + ",\"house_type\":" + house_type
                + ",\"path\":\"" + path
                + "\",\"create_at\":\"" + create_at
                + "\",\"update_at\":\"" + update_at
                + "\"}";
    }
}
