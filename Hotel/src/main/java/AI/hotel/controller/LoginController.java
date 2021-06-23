package AI.hotel.controller;

import AI.hotel.bean.Employee;
import AI.hotel.bean.Role;
import AI.hotel.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import static AI.hotel.utils.toJson.toJsonArray;

@Controller
@RequestMapping("/login")
public class LoginController {


    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    /**
     * 登录失败返回 401 以及提示信息.
     *
     * @return the rest
     */
    @ResponseBody
    @PostMapping("/failure")
    public String loginFailure() {
        return toJsonArray(HttpStatus.UNAUTHORIZED.value(), "登录失败了，老哥", "false");
    }

    @GetMapping("hello")
    public String hello() {
        return "index";
    }


    /**
     * 登录成功后拿到个人信息.
     *
     * @return the rest
     */
    @PostMapping("/success")
    @ResponseBody
    public String loginSuccess() {
        // 登录成功后用户的认证信息 UserDetails会存在 安全上下文寄存器 SecurityContextHolder 中
        Employee userDetail = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwtTokenUtil.generateToken(userDetail);
        return toJsonArray(200, "登录成功了，老哥", token);
    }
}
