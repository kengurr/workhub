package com.workhub.service;

import com.workhub.entity.Project;

import java.util.List;

public interface ProjectService {

    List<Project> getProjects();

    Project getProject(Long projectId);

    void createProject(Project project);

    void updateProject(Long projectId, Project project);

    void deleteProject(Long projectId);

    void createProjectForEmployee(Project project, Long employeeId);

    void removeProjectForEmployee(Long projectId, Long employeeId);

    void assignProjectToEmployee(Long projectId, Long employeeId);

}
