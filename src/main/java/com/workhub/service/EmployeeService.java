package com.workhub.service;

import com.workhub.Utils.TechnicalSkillsValidator;
import com.workhub.dto.ChangePasswordRequest;
import com.workhub.entity.Employee;
import com.workhub.exception.ExceptionUtil;
import com.workhub.exception.WorkhubException;
import com.workhub.repository.EmployeeQueryDslRepository;
import com.workhub.repository.EmployeeRepository;
import com.workhub.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ProjectRepository projectRepository;

    private final TechnicalSkillsValidator technicalSkillsValidator;
  
    private final EmployeeQueryDslRepository employeeQueryDslRepository;

    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
                           ProjectRepository projectRepository, TechnicalSkillsValidator technicalSkillsValidator, EmployeeQueryDslRepository employeeQueryDslRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.technicalSkillsValidator = technicalSkillsValidator;
        this.employeeQueryDslRepository = employeeQueryDslRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));
    }

    @Transactional
    public void createEmployee(Employee employee) {
        if(checkEmployeeExists(employee.getEmail())) {
            throw ExceptionUtil.logAndBuildException(WorkhubException.BAD_REQUEST);
        }
        employeeRepository.save(employee);
    }

    @Transactional
    public void updateEmployee(Long employeeId, Employee employee) {
        boolean existingEmployee = employeeRepository.existsById(employeeId);
        if(!existingEmployee) {
            throw ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND);
        }
        employee.setId(employeeId);
        employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long employeeId) {
        boolean existingEmployee = employeeRepository.existsById(employeeId);
        if(!existingEmployee) {
            throw ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND);
        }
        employeeRepository.deleteById(employeeId);
    }

    @Transactional
    public void removeEmployeeFromProject(Long employeeId, Long projectId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        employee.getProjects().remove(project);
        project.getEmployees().remove(employee);

        employeeRepository.save(employee);
    }

    @Transactional
    public void assignEmployeeToProject(Long employeeId, Long projectId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        technicalSkillsValidator.validateTechnicalSkills(employee, project);
        if (!employee.getProjects().contains(project)) {
            employee.getProjects().add(project);
            project.getEmployees().add(employee);
        }

        employeeRepository.save(employee);
    }

    public List<Employee> searchEmployeesByName(String name) {
        return employeeQueryDslRepository.findEmployeesByName(name);
    }

    public List<Employee> getEmployeesByProject(Long projectId) {
        return employeeQueryDslRepository.findEmployeesByProject(projectId);
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var employee = (Employee) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), employee.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        employee.setPassword(passwordEncoder.encode(request.getNewPassword()));
        employeeRepository.save(employee);
    }

    private boolean checkEmployeeExists(String email) {
        long checkItemCounts = employeeRepository.countByEmail(email);
        return checkItemCounts > 0;
    }

}
