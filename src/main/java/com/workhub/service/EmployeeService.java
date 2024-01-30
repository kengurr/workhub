package com.workhub.service;

import com.workhub.entity.Employee;

public interface EmployeeService {

    Employee getEmployee(Long employeeId);

    void createEmployee(Employee employee);

    void deleteEmployee(Long employeeId);

    void createEmployeeForProject(Employee employee, Long projectId);

    void removeEmployeeForProject(Long employeeId, Long projectId);

    void assignEmployeeToProject(Long employeeId, Long projectId);

}
