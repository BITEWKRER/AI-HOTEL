package AI.hotel.bean;



import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class IdCard implements Serializable {

    private Integer id;
    private String id_number;
    private String front;
    private String back;
    private String recent;
    private Date create_at;
    private Date update_at;


    @Override
    public String toString() {
        return "{ \"id\":" + id
                + ",\"id_number\":\"" + id_number
                + "\",\"front\":\"" + front
                + "\",\"back\":\"" + back
                + ",\"recent\":\"" + recent
                + "\",\"create_at\":\"" + create_at
                + "\",\"update_at\":\"" + update_at
                + "\"}";
    }


}
