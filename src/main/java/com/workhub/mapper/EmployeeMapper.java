package com.workhub.mapper;

import com.workhub.dto.EmployeeDto;
import com.workhub.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto employeeToEmployeeDTO(Employee employee);

    Employee employeeDTOToEmployee(EmployeeDto employeeDTO);

    List<EmployeeDto> employeesToEmployeeDTOs(List<Employee> employees);

    void updateEmployeeFromDTO(EmployeeDto employeeDTO, @MappingTarget Employee employee);
}
