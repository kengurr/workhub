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
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{employeeId}")
    public Employee getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping(value = "/")
    public ResponseEntity<Void> createEmployee(@RequestBody Employee employee) {
        employeeService.createEmployee(employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{employeeId}")
    public ResponseEntity<Void> updateEmployee(@RequestBody Employee employee,
                                            @PathVariable(name = "employeeId") Long employeeId) {
        employeeService.updateEmployee(employeeId, employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{employeeId}/projects/{projectId}")
    public ResponseEntity<Void> assignEmployeeToProject(
            @PathVariable Long employeeId, @PathVariable Long projectId) {
        employeeService.assignEmployeeToProject(employeeId, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{employeeId}/projects/{projectId}")
    public ResponseEntity<Void> removeEmployeeFromProject(@PathVariable(name = "employeeId") Long employeeId,
                                                       @PathVariable(name = "projectId") Long projectId) {
        employeeService.removeEmployeeFromProject(employeeId, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
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
