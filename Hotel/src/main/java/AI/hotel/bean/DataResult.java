package AI.hotel.bean;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class DataResult {
    private String name;
    private Integer count;
    private Double rate;
    private Integer money;
    private Date date;

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name +
                "\",\"count\":" + count +
                ",\"rate\":" + rate +
                ",\"money\":" + money +
                ",\"date\":\"" + date +
                "\"}";
    }
}
