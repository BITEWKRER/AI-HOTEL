package AI.hotel.service.Impl;

import AI.hotel.bean.PageRequest;
import AI.hotel.bean.Employee;
import AI.hotel.bean.PageResult;
import AI.hotel.mapper.EmployeeMapper;
import AI.hotel.service.EmployeeService;
import AI.hotel.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;


    @Override
    public Employee loadEmployeeByNum(String num) {
        return employeeMapper.loadEmployeeByNum(num);
    }

    @Override
    public boolean addEmployee(Employee employee) {
        return employeeMapper.addEmployee(employee);
    }

    @Override
    public boolean deleteEmployee(int id) {
        return employeeMapper.deleteEmployee(id);
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        return employeeMapper.updateEmployee(employee);
    }

    @Override
    public List<Employee> getEmployee() {
        return employeeMapper.getEmployee();
    }

    @Override
    public List<Employee> getEmployeeById(int id) {
        return employeeMapper.getEmployeeById(id);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
    }

    @Override
    public List<Employee> getEmployeeByRole(String role) {
        return employeeMapper.getEmployeeByRole(role);
    }

    @Override
    public List<Employee> getEmployeeByNum(String num) {
        return employeeMapper.getEmployeeByNum(num);
    }


    /**
     * 调用分页插件完成分页
     *
     * @param pageRequest
     * @return
     */
    private PageInfo<Employee> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> employees = employeeMapper.getEmployee();
        return new PageInfo<Employee>(employees);
    }
}
