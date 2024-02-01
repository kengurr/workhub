package com.workhub.service;

import com.workhub.Utils.ValidationUtils;
import com.workhub.entity.Employee;
import com.workhub.entity.Project;
import com.workhub.exception.EmployeeNotFoundException;
import com.workhub.exception.ProjectNotFoundException;
import com.workhub.repository.EmployeeRepository;
import com.workhub.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ProjectRepository projectRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> EmployeeNotFoundException.notFoundById(employeeId));
    }

    @Override
    public void createEmployee(Employee employee) {
        Employee existingEmployee = employeeRepository.findEmployeeByEmail(employee.getEmail());
        if(existingEmployee != null) {
            throw new IllegalStateException("Cannot create employee, email already taken");
        }
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void updateEmployee(Long employeeId, Employee employee) {
        boolean existingEmployee = employeeRepository.existsById(employeeId);
        if(!existingEmployee) {
            throw EmployeeNotFoundException.cannotUpdate();
        }
        employee.setId(employeeId);
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        boolean existingEmployee = employeeRepository.existsById(employeeId);
        if(!existingEmployee) {
            throw EmployeeNotFoundException.cannotDelete();
        }
        employeeRepository.deleteById(employeeId);
    }

    @Override
    @Transactional
    public void createEmployeeForProject(Employee employee, Long projectId) {
        Employee existingEmployee = employeeRepository.findEmployeeByEmail(employee.getEmail());
        if(existingEmployee != null) {
            throw new IllegalStateException("Cannot create employee, email already taken");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.notFoundById(projectId));

        ValidationUtils.validateTechnicalSkills(employee, project);
        employee.addProject(project);
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void removeEmployeeForProject(Long employeeId, Long projectId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> EmployeeNotFoundException.notFoundById(employeeId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.notFoundById(projectId));

        employee.removeProject(project);
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void assignEmployeeToProject(Long employeeId, Long projectId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> EmployeeNotFoundException.notFoundById(employeeId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.notFoundById(projectId));

        ValidationUtils.validateTechnicalSkills(employee, project);
        employee.addProject(project);
        employeeRepository.save(employee);
    }
}
