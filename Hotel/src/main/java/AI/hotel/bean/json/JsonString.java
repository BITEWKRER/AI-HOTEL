package AI.hotel.bean.json;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回json字符串
 */
@Data
public class JsonString implements Serializable {
    private int code;
    private String msg;
    private String data = null;

    public JsonString(int code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{ \"code\":" + code
                + ",\"msg\":\"" + msg
                + "\",\"data\":\"" + data
                + "\"}";
    }


}
