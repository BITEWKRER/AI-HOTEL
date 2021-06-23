package AI.hotel.bean;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Data
@Component
public class Role implements GrantedAuthority {
    private Integer id;
    private String role;


    @Override
    public String getAuthority() {
        return role;
    }

    public Role() {

    }

    public Role(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

}
