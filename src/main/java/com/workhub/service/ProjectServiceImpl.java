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

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.notFoundById(projectId));
    }

    @Override
    public void createProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void updateProject(Long projectId, Project project) {
        boolean projectExists = projectRepository.existsById(projectId);
        if(!projectExists) {
            throw ProjectNotFoundException.cannotUpdate();
        }
        project.setId(projectId);
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long projectId) {
        boolean projectExists = projectRepository.existsById(projectId);
        if(!projectExists) {
            throw ProjectNotFoundException.cannotDelete();
        }
        projectRepository.deleteById(projectId);
    }

    @Override
    @Transactional
    public void createProjectForEmployee(Project project, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> EmployeeNotFoundException.notFoundById(employeeId));

        ValidationUtils.validateTechnicalSkills(employee, project);
        project.addEmployee(employee);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void removeProjectForEmployee(Long projectId, Long employeeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.notFoundById(projectId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> EmployeeNotFoundException.notFoundById(employeeId));

        project.removeEmployee(employee);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void assignProjectToEmployee(Long projectId, Long employeeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.notFoundById(projectId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> EmployeeNotFoundException.notFoundById(employeeId));

        ValidationUtils.validateTechnicalSkills(employee, project);
        project.addEmployee(employee);
        projectRepository.save(project);
    }

}
