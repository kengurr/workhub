package com.workhub.service;

import com.workhub.Utils.TechnicalSkillsValidator;
import com.workhub.entity.Project;
import com.workhub.exception.EmployeeNotFoundException;
import com.workhub.exception.ProjectNotFoundException;
import com.workhub.repository.EmployeeRepository;
import com.workhub.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final EmployeeRepository employeeRepository;

    private final TechnicalSkillsValidator technicalSkillsValidator;

    public ProjectService(ProjectRepository projectRepository,
                          EmployeeRepository employeeRepository, TechnicalSkillsValidator technicalSkillsValidator) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.technicalSkillsValidator = technicalSkillsValidator;
    }

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.notFoundById(projectId));
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public void createProject(Project project) {
        projectRepository.save(project);
    }

    @Transactional
    public void updateProject(Long projectId, Project project) {
        boolean projectExists = projectRepository.existsById(projectId);
        if(!projectExists) {
            throw ProjectNotFoundException.cannotUpdate();
        }
        project.setId(projectId);
        projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        boolean projectExists = projectRepository.existsById(projectId);
        if(!projectExists) {
            throw ProjectNotFoundException.cannotDelete();
        }
        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void removeProjectFromEmployee(Long projectId, Long employeeId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.notFoundById(projectId));

        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> EmployeeNotFoundException.notFoundById(employeeId));

        project.getEmployees().remove(employee);
        employee.getProjects().remove(project);

        projectRepository.save(project);
    }

    @Transactional
    public void assignProjectToEmployee(Long projectId, Long employeeId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ProjectNotFoundException.notFoundById(projectId));

        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> EmployeeNotFoundException.notFoundById(employeeId));

        technicalSkillsValidator.validateTechnicalSkills(employee, project);
        if (!project.getEmployees().contains(employee)) {
            project.getEmployees().add(employee);
            employee.getProjects().add(project);
        }

        projectRepository.save(project);
    }

}
