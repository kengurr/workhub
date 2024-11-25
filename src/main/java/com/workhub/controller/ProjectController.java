package com.workhub.controller;

import com.workhub.entity.Project;
import com.workhub.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/")
    public List<Project> getProjects() {
        log.info("Received request to get list of projects");
        return projectService.getProjects();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{projectId}")
    public Project getProject(@PathVariable(name = "projectId") Long projectId) {
        log.info("Received request to get project - projectId: {}", projectId);
        return projectService.getProject(projectId);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        log.info("Received request to create project");
        projectService.createProject(project);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{projectId}")
    public ResponseEntity<?> updateProject(@RequestBody Project project,
                                           @PathVariable(name = "projectId") Long projectId) {
        log.info("Received request to update project - projectId: {}", projectId);
        projectService.updateProject(projectId, project);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable(name = "projectId") Long projectId) {
        log.info("Received request to delete project - projectId: {}", projectId);
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{projectId}/employees/{employeeId}")
    public ResponseEntity<?> assignProjectToEmployee(@PathVariable(name = "projectId") Long projectId,
                                                     @PathVariable(name = "employeeId") Long employeeId) {
        log.info("Received request to assign project to employee");
        projectService.assignProjectToEmployee(projectId, employeeId);
        return ResponseEntity.ok("Project assigned to employee successfully");
    }

    @DeleteMapping(value = "/{projectId}/employees/{employeeId}")
    public ResponseEntity<?> removeProjectFromEmployee(@PathVariable(name = "projectId") Long projectId,
                                                       @PathVariable(name = "employeeId") Long employeeId) {
        log.info("Received request to remove project for employee");
        projectService.removeProjectFromEmployee(projectId, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
