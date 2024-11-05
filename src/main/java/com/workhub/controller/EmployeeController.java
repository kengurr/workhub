package com.workhub.controller;

import com.workhub.dto.ChangePasswordRequest;
import com.workhub.entity.Employee;
import com.workhub.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/get/{employeeId}")
    public Employee getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        employeeService.createEmployee(employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/update/{employeeId}")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee employee,
                                            @PathVariable(name = "employeeId") Long employeeId) {
        employeeService.updateEmployee(employeeId, employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/create-employee-for-project/{projectId}")
    public ResponseEntity<?> createEmployeeForProject(@RequestBody Employee employee,
                                                   @PathVariable(name = "projectId") Long projectId) {
        employeeService.createEmployeeForProject(employee, projectId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/remove-employee-for-project/{projectId}/{employeeId}")
    public ResponseEntity<?> removeEmployeeForProject(@PathVariable(name = "projectId") Long projectId,
                                                       @PathVariable(name = "employeeId") Long employeeId) {
        employeeService.removeEmployeeForProject(projectId, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/assign-employee/{employeeId}/{projectId}")
    public ResponseEntity<String> assignEmployeeToProject(
            @PathVariable Long employeeId, @PathVariable Long projectId) {
        employeeService.assignEmployeeToProject(employeeId, projectId);
        return ResponseEntity.ok("Employee assigned to project successfully");
    }

    @GetMapping("/search-by-name")
    public List<Employee> searchEmployeesByName(@RequestParam(name = "name") String name) {
        return employeeService.searchEmployeesByName(name);
    }

    @GetMapping("/by-project/{projectId}")
    public List<Employee> getEmployeesByProject(@PathVariable(name = "projectId") Long projectId) {
        return employeeService.getEmployeesByProject(projectId);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {
        employeeService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

}
