package AI.hotel.bean.json;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 返回json数组
 */

@Data
public class JsonArray implements Serializable {
    private int code;
    private String msg;
    private String data = null;

    public JsonArray(int code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{ \"code\":" + code
                + ",\"msg\":\"" + msg
                + "\",\"data\":" + data
                + "}";
    }
}
