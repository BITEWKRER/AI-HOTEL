package AI.hotel.conf;

import AI.hotel.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationProvider provider;  //注入我们自己的AuthenticationProvider


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
         */
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
        http.cors().and().csrf().disable()//禁用了 csrf 功能
                .authorizeRequests()//限定签名成功的请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/", "/static/**",
                        "/login/hello","/phone/openid/**",
                        "/login",
                        "/phone/updateNotPayOrdersStatus/**",
                        "/phone/getAllNotPayOrders/**",
                        "/phone/getToken",
                        "/login/failure","/web/getImg", "/401",
                        "/css/**", "/js/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")//对admin下的接口 需要ADMIN权限
//                .anyRequest().authenticated()
//                .antMatchers("/decision/**", "/govern/**", "/employee/*").hasAnyRole("EMPLOYEE", "ADMIN")//对decision和govern 下的接口 需要 USER 或者 ADMIN 权限
                .anyRequest().permitAll()//其他没有限定的请求，允许访问
//                .and().anonymous()//对于没有配置权限的其他请求允许匿名访问
                .and().formLogin().loginPage("/login/hello")
                .loginProcessingUrl("/login")
                .successForwardUrl("/login/success")
                .failureForwardUrl("/login/failure")//使用 spring security 默认登录页面
                .and().httpBasic()
                .and().headers()
                .cacheControl();//启用http 基础验证


    }

}