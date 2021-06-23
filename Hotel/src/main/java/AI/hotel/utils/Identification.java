package AI.hotel.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static AI.hotel.utils.FileUtils.getStaticPath;
import static org.apache.commons.codec.binary.Base64.encodeBase64;


@Slf4j
public class Identification {


    /*
     * 获取参数的json对象
     */
    public JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public boolean isPass(List<String> list) {

        for (String str : list) {
            if (str.contains("*")) return false;
            if (str.isEmpty()) return false;
        }
        return true;
    }

    public String getIdentification(String imgName, String side, String appcode) {
        log.info(appcode);
        String host = "http://dm-51.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_idcard.json";
        String imgFile = getStaticPath("faceImgs") + imgName;
        Boolean is_old_format = false;//如果文档的输入中含有inputs字段，设置为True， 否则设置为False
        //请根据线上文档修改configure字段
        JSONObject configObj = new JSONObject();
        configObj.put("side", side);
        String config_str = configObj.toString();
        //            configObj.put("min_size", 5);
        //            String config_str = "";

        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();

        // 对图像进行base64编码
        String imgBase64 = "";
        try {
            File file = new File(imgFile);
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            imgBase64 = new String(encodeBase64(content));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        try {
            if (is_old_format) {
                JSONObject obj = new JSONObject();
                obj.put("image", getParam(50, imgBase64));
                if (config_str.length() > 0) {
                    obj.put("configure", getParam(50, config_str));
                }
                JSONArray inputArray = new JSONArray();
                inputArray.add(obj);
                requestObj.put("inputs", inputArray);
            } else {
                requestObj.put("image", imgBase64);
                if (config_str.length() > 0) {
                    requestObj.put("configure", config_str);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        String bodys = requestObj.toString();
        try {

            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if (stat != 200) {
                log.error("Http code: " + stat);
                log.error("http header error msg: " + response.getFirstHeader("X-Ca-Error-Message"));
                log.error("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                return null;
            }
            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            if (is_old_format) {
                JSONArray outputArray = res_obj.getJSONArray("outputs");
                String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                JSONObject out = JSON.parseObject(output);
                return out.toJSONString();
            } else {
                return res_obj.toJSONString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

