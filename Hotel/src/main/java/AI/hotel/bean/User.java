package AI.hotel.bean;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class User implements Serializable {

    private Integer id;
    private String real_name;
    private String id_number;
    private String open_id;
    private Date create_at;
    private Date update_at;

    @Override
    public String toString() {
        return "{ \"id\":" + id
                + ",\"real_name\":\"" + real_name
                + "\",\"id_number\":\"" + id_number
                + "\",\"open_id\":\"" + open_id
                + "\",\"create_at\":\"" + create_at
                + "\",\"update_at\":\"" + update_at
                + "\"}";
    }

}
