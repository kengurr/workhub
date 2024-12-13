package com.workhub.controller;

import com.workhub.dto.ChangePasswordRequest;
import com.workhub.dto.EmployeeDto;
import com.workhub.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getEmployees() {
        log.info("Received request to get list of employees");
        var employees = employeeService.getEmployees();

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long employeeId) {
        log.info("Received request to get employee - employeeId: {}", employeeId);
        var employee = employeeService.getEmployee(employeeId);

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<Void> createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
        employeeService.createEmployee(employeeDto);
            log.info("Received request to create employee");
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{employeeId}")
    public ResponseEntity<Void> updateEmployee(@RequestBody @Valid EmployeeDto employeeDto,
                                            @PathVariable(name = "employeeId") Long employeeId) {
        employeeService.updateEmployee(employeeId, employeeDto);
        log.info("Received request to update employee - employeeId: {}", employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
        log.info("Received request to delete employee - employeeId: {}", employeeId);
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{employeeId}/projects/{projectId}")
    public ResponseEntity<Void> assignEmployeeToProject(
            @PathVariable Long employeeId, @PathVariable Long projectId) {
        log.info("Received request to assign employee to project");
        employeeService.assignEmployeeToProject(employeeId, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{employeeId}/projects/{projectId}")
    public ResponseEntity<Void> removeEmployeeFromProject(@PathVariable(name = "employeeId") Long employeeId,
                                                       @PathVariable(name = "projectId") Long projectId) {
        log.info("Received request to remove employee from project");
        employeeService.removeEmployeeFromProject(employeeId, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search-by-name")
    public List<EmployeeDto> searchEmployeesByName(@RequestParam(name = "name") String name) {
            log.info("Received request to search for employee by name- name: {}", name);
            return employeeService.searchEmployeesByName(name);
    }

    @GetMapping("/by-project/{projectId}")
    public List<EmployeeDto> getEmployeesByProject(@PathVariable(name = "projectId") Long projectId) {
            log.info("Received request to get employee by project - projectId: {}", projectId);
            return employeeService.getEmployeesByProject(projectId);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request, Principal connectedUser) {
            log.info("Received request to change password for employee - connectedUser: {}", connectedUser.getName());
            employeeService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
