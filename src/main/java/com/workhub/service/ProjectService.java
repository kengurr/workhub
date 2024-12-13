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

    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository,
                          EmployeeRepository employeeRepository, TechnicalSkillsValidator technicalSkillsValidator, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.technicalSkillsValidator = technicalSkillsValidator;
        this.projectMapper = projectMapper;
    }

    public List<ProjectDto> getProjects() {
        var projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND);
        }
        return projects.stream()
                .map(projectMapper::projectToProjectDTO)
                .toList();
    }

    public ProjectDto getProject(Long projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));
        return projectMapper.projectToProjectDTO(project);
    }

    public void createProject(ProjectDto projectDto) {
        var project = projectMapper.projectDTOToProject(projectDto);

        projectRepository.save(project);
    }

    public void updateProject(Long projectId, ProjectDto projectDto) {
        var existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        projectMapper.updateProjectFromDTO(projectDto, existingProject);

        projectRepository.save(existingProject);
    }

    public void deleteProject(Long projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> ExceptionUtil.logAndBuildException(WorkhubException.NOT_FOUND));

        projectRepository.delete(project);
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
