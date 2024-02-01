package com.workhub.controller;

import com.workhub.entity.Employee;
import com.workhub.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/")
    public List<Employee> getEmployees() {
        return employeeServiceImpl.getEmployees();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/get/{employeeId}")
    public Employee getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        return employeeServiceImpl.getEmployee(employeeId);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        employeeServiceImpl.createEmployee(employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/update/{employeeId}")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee employee,
                                            @PathVariable(name = "employeeId") Long employeeId) {
        employeeServiceImpl.updateEmployee(employeeId, employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeServiceImpl.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/create-employee-for-project/{projectId}")
    public ResponseEntity<?> createEmployeeForProject(@RequestBody Employee employee,
                                                   @PathVariable(name = "projectId") Long projectId) {
        employeeServiceImpl.createEmployeeForProject(employee, projectId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/remove-employee-for-project/{projectId}/{employeeId}")
    public ResponseEntity<?> removeEmployeeForProject(@PathVariable(name = "projectId") Long projectId,
                                                       @PathVariable(name = "employeeId") Long employeeId) {
        employeeServiceImpl.removeEmployeeForProject(projectId, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/assign-employee/{employeeId}/{projectId}")
    public ResponseEntity<String> assignEmployeeToProject(
            @PathVariable Long employeeId, @PathVariable Long projectId) {
        employeeServiceImpl.assignEmployeeToProject(employeeId, projectId);
        return ResponseEntity.ok("Employee assigned to project successfully");
    }

}
