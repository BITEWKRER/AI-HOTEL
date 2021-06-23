package AI.hotel.utils;



import AI.hotel.bean.json.JsonArray;
import AI.hotel.bean.json.JsonString;
import com.alibaba.fastjson.JSON;



public class toJson {

    public static String toJsonString(int code, String msg, Object data) {
        return new JsonString(code, msg, JSON.toJSONString(data)).toString();
    }

    public static String toJsonArray(int code, String msg, Object data) {
        return new JsonArray(code, msg, JSON.toJSONString(data)).toString();
    }


}
