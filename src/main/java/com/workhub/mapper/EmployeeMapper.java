package com.workhub.mapper;

import com.workhub.dto.EmployeeDto;
import com.workhub.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDto employeeToEmployeeDTO(Employee employee);

    Employee employeeDTOToEmployee(EmployeeDto employeeDTO);

    List<EmployeeDto> employeesToEmployeeDTOs(List<Employee> employees);
}
