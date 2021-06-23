package AI.hotel.mapper;


import AI.hotel.bean.Employee;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmployeeMapper {


    @Select("select  num , password , role from employee where num = #{num} limit 0,1")
    Employee loadEmployeeByNum(@Param("num") String num);


    @Insert("insert into employee (name,iphone,email,num,role,password,create_at,update_at) " +
            "values (#{name},#{iphone},#{email},#{num},#{role},#{password},#{createAt},#{updateAt}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    boolean addEmployee(Employee employee);


    @Delete("delete from employee where id = #{id}")
    boolean deleteEmployee(@Param("id") int id);

    @Update("update employee set name = #{name},iphone = #{iphone},email = #{email},num = #{num},role = #{role} where id = #{id} ")
    boolean updateEmployee(Employee employee);

    @Select("select id,role,name,iphone,email,num from employee where role !='ADMIN' and role!= 'APPLET' ")
    List<Employee> getEmployee();


    @Select("select id,name,iphone,email,num from employee where id = #{id}")
    List<Employee> getEmployeeById(@Param("id") int id);

    @Select("select id,name,iphone,email,num from employee where id = #{num}")
    List<Employee> getEmployeeByNum(@Param("num") String num);

    @Select("select id,name,iphone,email,num from employee where role = #{param1}")
    List<Employee> getEmployeeByRole(String role);
}
