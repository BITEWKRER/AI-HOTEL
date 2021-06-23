package AI.hotel.controller;


import AI.hotel.bean.PageRequest;
import AI.hotel.bean.Employee;
import AI.hotel.bean.PageResult;
import AI.hotel.service.EmployeeService;
import AI.hotel.utils.IDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static AI.hotel.utils.toJson.toJsonArray;
import static AI.hotel.utils.toJson.toJsonString;


@RestController
@Slf4j
@RequestMapping("/admin")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping("/getEmployee")
    public String getEmployee() {
        List<Employee> employeeList = employeeService.getEmployee();
        if (!employeeList.isEmpty()) {
            return toJsonArray(200, "获取员工信息成功", employeeList);
        } else {
            return toJsonString(100, "获取员工信息失败", false);
        }
    }

    @GetMapping("/getEmployeeById")
    public String getEmployeeById(int id) {
        List<Employee> employeeList = employeeService.getEmployeeById(id);
        if (!employeeList.isEmpty()) {
            return toJsonArray(200, "获取指定员工信息成功", employeeList);
        } else {
            return toJsonString(100, "获取指定员工信息失败", false);
        }
    }

    @PostMapping("/addEmployee")
    public String addEmployee(Employee employee) {
        employee.setCreateAt(new Date());
        boolean flag = employeeService.addEmployee(employee);
        if (flag) return toJsonString(200, "添加员工信息成功", true);
        else return toJsonString(100, "获取员工信息失败", false);
    }

    @PutMapping("/updateEmployee")
    public String updateEmployee(Employee employee) {
        employee.setUpdateAt(new Date());
        boolean flag = employeeService.updateEmployee(employee);
        if (flag) return toJsonString(200, "修改员工信息成功", true);
        else return toJsonString(100, "修改员工信息失败", false);
    }

    @DeleteMapping("/deleteEmployee")
    public String deleteEmployee(int id) {
        boolean flag = employeeService.deleteEmployee(id);
        if (flag) return toJsonString(200, "删除员工信息成功", true);
        else return toJsonString(100, "删除员工信息失败", false);
    }

    /**
     * 分页查询
     *
     * @param pageQuery
     * @return
     */
    @GetMapping("/getEmplooyeByPage")
    public String findPage(PageRequest pageQuery) {
        PageResult temp = employeeService.findPage(pageQuery);
        if (temp != null) {
            return toJsonArray(200, "获取员工信息成功", temp);
        } else {
            return toJsonArray(100, "获取员工信息失败", "false");
        }
    }

    ///
    @GetMapping("/getEmplooyeBynum")
    public String getEmplooyeByNum(String num) {
        if (!IDUtils.isIDNumber(num)) return toJsonArray(100, "请输入正确的身份证", "false");
        List<Employee> employees = employeeService.getEmployeeByNum(num);
        if(!employees.isEmpty()){
            return toJsonArray(200, "获取指定员工信息成功", employees);
        }else {
            return toJsonArray(200, "获取指定员工信息失败", employees);
        }

    }


}
