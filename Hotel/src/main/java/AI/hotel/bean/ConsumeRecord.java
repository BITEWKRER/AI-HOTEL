package AI.hotel.bean;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class ConsumeRecord implements Serializable {
    private Integer id;
    private String id_number;
    private String item_name;
    private Integer item_money;
    private String description;
    private char is_pay;
    private Date create_at;
    private Date update_at;

    @Override
    public String toString() {
        return "{ \"id\":" + id
                + ",\"id_number\":\"" + id_number
                + "\",\"item_name\":\"" + item_name
                + "\",\"item_money\":" + item_money
                + ",\"description\":" + description
                + ",\"is_pay\":" + is_pay
                + ",\"create_at\":\"" + create_at
                + "\",\"update_at\":\"" + update_at
                + "\"}";
    }
}
