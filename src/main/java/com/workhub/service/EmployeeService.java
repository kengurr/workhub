package com.workhub.service;

import com.workhub.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getEmployees();

    Employee getEmployee(Long employeeId);

    void createEmployee(Employee employee);
    
    void updateEmployee(Long employeeId, Employee employee);

    void deleteEmployee(Long employeeId);

    void createEmployeeForProject(Employee employee, Long projectId);

    void removeEmployeeForProject(Long employeeId, Long projectId);

    void assignEmployeeToProject(Long employeeId, Long projectId);

    List<Employee> searchEmployeesByName(String name);

    List<Employee> getEmployeesByProject(Long projectId);

}
