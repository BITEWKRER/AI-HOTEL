package AI.hotel.controller;

import AI.hotel.bean.Employee;
import AI.hotel.utils.JwtTokenUtil;
import AI.hotel.utils.WeChatUtil;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static AI.hotel.utils.toJson.toJsonArray;

@RequestMapping("/phone")
@RestController
public class OpenIdController {

    // 微信小程序ID
    @Value("${weChat.appId}")
    String appid;
    // 微信小程序秘钥
    @Value("${weChat.secret}")
    String secret;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/getToken")
    public String getToken() {
        String token;
        Employee employee = new Employee();
        employee.setNum("421223199908220018");//522222222222222222
        employee.setRole("SINGLE");
        try {
             token = jwtTokenUtil.generateToken(employee);
        } catch (Exception e) {
            return "报错啦";
        }
        return token;
    }


    @GetMapping(value = "/openid/{code}")
    public String openid(@PathVariable("code") String code) {
        Employee employee = new Employee();
        employee.setNum("421223199908220018");
        employee.setRole("APPLET");
        String token = jwtTokenUtil.generateToken(employee);
        Map<String, String> map = new HashMap<>();

//         根据小程序穿过来的code想这个url发送请求
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
//         发送请求，返回Json字符串
        String str = WeChatUtil.httpRequest(url, "GET", null);
//         转成Json对象 获取openid
        JSONObject jsonObject = JSONObject.parseObject(str);

//         我们需要的openid，在一个小程序中，openid是唯一的
        String openid = jsonObject.get("openid").toString();

//         然后书写自己的处理逻辑即可
        map.put("token", token);
        map.put("openid", openid);
        return toJsonArray(200, "获取openid成功", map);
    }
}
