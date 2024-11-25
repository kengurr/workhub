package com.workhub.service;

import com.workhub.Utils.TechnicalSkillsValidator;
import com.workhub.dto.ProjectDto;
import com.workhub.exception.ExceptionUtil;
import com.workhub.exception.WorkhubException;
import com.workhub.mapper.ProjectMapper;
import com.workhub.repository.EmployeeRepository;
import com.workhub.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
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

    public List<ProjectDto> getProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper.INSTANCE::projectToProjectDTO)
                .toList();
    }

    public ProjectDto getProject(Long projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));
        return ProjectMapper.INSTANCE.projectToProjectDTO(project);
    }

    public void createProject(ProjectDto projectDto) {
        var project = ProjectMapper.INSTANCE.projectDTOToProject(projectDto);

        projectRepository.save(project);
    }

    public void updateProject(Long projectId, ProjectDto projectDto) {
        boolean projectExists = projectRepository.existsById(projectId);
        if(!projectExists) {
            throw ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND);
        }
        var updatedProject = ProjectMapper.INSTANCE.projectDTOToProject(projectDto);
        updatedProject.setId(projectId);

        projectRepository.save(updatedProject);
    }

    public void deleteProject(Long projectId) {
        boolean projectExists = projectRepository.existsById(projectId);
        if(!projectExists) {
            throw ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND);
        }
        projectRepository.deleteById(projectId);
    }

    public void removeProjectFromEmployee(Long projectId, Long employeeId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        project.getEmployees().remove(employee);
        employee.getProjects().remove(project);

        projectRepository.save(project);
    }

    public void assignProjectToEmployee(Long projectId, Long employeeId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        technicalSkillsValidator.validateTechnicalSkills(employee, project);
        if (!project.getEmployees().contains(employee)) {
            project.getEmployees().add(employee);
            employee.getProjects().add(project);
        }

        projectRepository.save(project);
    }
}
