package AI.hotel.service.Impl;

import AI.hotel.bean.Employee;
import AI.hotel.bean.Role;
import AI.hotel.mapper.EmployeeMapper;
import AI.hotel.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("ForJWT")
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    EmployeeService employeeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = employeeService.loadEmployeeByNum(username);
        if (null != employee) {
            Role role = new Role(employee.getId(), employee.getRole());
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            employee.setRoles(roles);
            return employee;
        }

        throw new UsernameNotFoundException("the user not found :" + username);


    }
}