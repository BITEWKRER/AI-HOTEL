package AI.hotel.bean;



import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class House implements Serializable {


    private Integer id;
    private Integer room_number;
    private String name;
    private Integer type;
    private Integer money;
    private Integer area;
    private Integer status;
    private String path;
    private String description;
    private Date create_at;
    private Date update_at;

    @Override
    public String toString() {
        return "{ \"id\":" + id
                + ",\"room_number\":" + room_number
                + ",\"name\":\"" + name
                + "\",\"type\":" + type
                + ",\"money\":" + money
                + ",\"area\":" + area
                + ",\"status\":" + status
                + ",\"description\":\"" + description
                + "\",\"create_at\":\"" + create_at
                + "\",\"update_at\":\"" + update_at
                + "\",\"path\":\"" + path
                + "\"}";
    }


}
