package com.workhub.service;

import com.workhub.entity.Employee;
import com.workhub.entity.Project;
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
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
    }

    @Override
    public void createProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long projectId) {
        boolean projectExists = projectRepository.existsById(projectId);
        if(!projectExists) {
            throw new IllegalStateException("Cannot delete project, project doesn't exist");
        }
        projectRepository.deleteById(projectId);
    }

    @Override
    @Transactional
    public void createProjectForEmployee(Project project, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        project.addEmployee(employee);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void removeProjectForEmployee(Long projectId, Long employeeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        project.removeEmployee(employee);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void assignProjectToEmployee(Long projectId, Long employeeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));

        project.addEmployee(employee);
        projectRepository.save(project);
    }

}
