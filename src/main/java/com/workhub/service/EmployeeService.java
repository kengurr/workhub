package com.workhub.service;

import com.workhub.Utils.TechnicalSkillsValidator;
import com.workhub.dto.ChangePasswordRequest;
import com.workhub.dto.EmployeeDto;
import com.workhub.entity.Employee;
import com.workhub.exception.ExceptionUtil;
import com.workhub.exception.WorkhubException;
import com.workhub.mapper.EmployeeMapper;
import com.workhub.repository.EmployeeRepository;
import com.workhub.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Transactional
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ProjectRepository projectRepository;

    private final TechnicalSkillsValidator technicalSkillsValidator;
  
    private final PasswordEncoder passwordEncoder;

    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository,
                           ProjectRepository projectRepository, TechnicalSkillsValidator technicalSkillsValidator, PasswordEncoder passwordEncoder, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.technicalSkillsValidator = technicalSkillsValidator;
        this.passwordEncoder = passwordEncoder;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeDto> getEmployees() {
        var employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND);
        }
        return employees.stream()
                .map(employeeMapper::employeeToEmployeeDTO)
                .toList();
    }

    public EmployeeDto getEmployee(Long employeeId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));
        return employeeMapper.employeeToEmployeeDTO(employee);
    }

    public void createEmployee(EmployeeDto employeeDto) {
        if(checkEmployeeExists(employeeDto.getEmail())) {
            throw ExceptionUtil.logAndBuildException(WorkhubException.BAD_REQUEST);
        }
        var employee = employeeMapper.employeeDTOToEmployee(employeeDto);

        employeeRepository.save(employee);
    }

    public void updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        var existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        employeeMapper.updateEmployeeFromDTO(employeeDto, existingEmployee);

        employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long employeeId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        employeeRepository.delete(employee);
    }

    public void removeEmployeeFromProject(Long employeeId, Long projectId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        employee.getProjects().remove(project);
        project.getEmployees().remove(employee);

        employeeRepository.save(employee);
    }

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

    public List<EmployeeDto> searchEmployeesByName(String name) {
        var employees = employeeRepository.findEmployeesByName(name);
        return employeeMapper.employeesToEmployeeDTOs(employees);
    }

    public List<EmployeeDto> getEmployeesByProject(Long projectId) {
        var employees = employeeRepository.findEmployeesByProject(projectId);
        return employeeMapper.employeesToEmployeeDTOs(employees);
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
