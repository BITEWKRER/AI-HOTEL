package AI.hotel.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Data
public class UserMessage implements Serializable {

    private Integer id;
    private String realName;
    private String idNumber;
    private String open_id;
    private String front;
    private String back;
    private String recent;
    private Date createAt;
    private Date updateAt;

    @Override
    public String toString() {
        return "{ \"id\":" + id
                + ",\"realName\":\"" + realName
                + "\",\"idNumber\":\"" + idNumber
                + "\",\"openId\":" + open_id
                + ",\"create_at\":\"" + createAt
                + "\",\"update_at\":\"" + updateAt
                + "\"}";
    }


}
