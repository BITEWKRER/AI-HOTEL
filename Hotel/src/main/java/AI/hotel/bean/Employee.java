package AI.hotel.bean;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@Data
public class Employee implements UserDetails, Serializable {

    private Integer id;
    private String role;
    private String name;
    private String email;
    private String iphone;
    private String num;
    private String password;
    private Date createAt;
    private Date updateAt;


    private List<Role> roles;


    @JSONField(serialize = false)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getAuthority()));
        }
        return authorities;

    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return this.num;
    }

    /**
     * 验证账号是否过期
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }


    /**
     * 验证账号是否锁定
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 验证密码是否过期
     *
     * @return
     */

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否可用
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {

        return true;

    }

    @Override
    public String toString() {

        return "{" +
                "\"id\":" + id +
                ",\"role\":\"" + role +
                "\",\"name\":\"" + name +
                "\",\"email\":\"" + email +
                "\",\"iphone\":\"" + iphone +
                "\",\"num\":\"" + num +
                "\"}";
    }
}
