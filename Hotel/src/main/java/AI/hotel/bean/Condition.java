package AI.hotel.bean;


import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Component
@Data
public class Condition implements Serializable {
    private int moneyMin = 0;
    private int moneyMax = Integer.MAX_VALUE;
    private String name;
    private String description;
    private int houseType;
    private Date start;
    private Date end;
    private String order;
    private List<String> fiter;

    @Override
    public String toString() {
        return "{ \"moneyMin\": " + moneyMin +
                ", \"moneyMax\": " + moneyMax +
                ", \"name\": \"" + name +
                "\", \"description\":\"" + description +
                "\", \"houseType\":" + houseType +
                ", \"start\":\"" + start +
                "\", \"end\":\"" + end +
                "\", \"order\":\"" + order +
                "\", \"fiter\": " + fiter +
                "}";
    }


}
