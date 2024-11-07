package com.workhub.service;

import com.workhub.dto.ChangePasswordRequest;
import com.workhub.entity.Employee;

import java.security.Principal;
import java.util.List;

public interface EmployeeService {

    List<Employee> getEmployees();

    Employee getEmployee(Long employeeId);

    void createEmployee(Employee employee);
    
    void updateEmployee(Long employeeId, Employee employee);

    void deleteEmployee(Long employeeId);

    void removeEmployeeFromProject(Long employeeId, Long projectId);

    void assignEmployeeToProject(Long employeeId, Long projectId);

    List<Employee> searchEmployeesByName(String name);

    List<Employee> getEmployeesByProject(Long projectId);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);

}
