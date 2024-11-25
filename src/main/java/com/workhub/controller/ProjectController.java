package com.workhub.controller;

import com.workhub.dto.ProjectDto;
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

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getProjects() {
        log.info("Received request to get list of projects");
        var projects = projectService.getProjects();

        return projects.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable(name = "projectId") Long projectId) {
        log.info("Received request to get project - projectId: {}", projectId);
        var projects = projectService.getProject(projectId);

        return projects == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<Void> createProject(@RequestBody ProjectDto projectDto) {
        log.info("Received request to create project");
        projectService.createProject(projectDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(@RequestBody ProjectDto projectDto,
                                              @PathVariable(name = "projectId") Long projectId) {
        log.info("Received request to update project - projectId: {}", projectId);
        projectService.updateProject(projectId, projectDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable(name = "projectId") Long projectId) {
        log.info("Received request to delete project - projectId: {}", projectId);
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{projectId}/employees/{employeeId}")
    public ResponseEntity<Void> assignProjectToEmployee(@PathVariable(name = "projectId") Long projectId,
                                                     @PathVariable(name = "employeeId") Long employeeId) {
        log.info("Received request to assign project to employee");
        projectService.assignProjectToEmployee(projectId, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{projectId}/employees/{employeeId}")
    public ResponseEntity<Void> removeProjectFromEmployee(@PathVariable(name = "projectId") Long projectId,
                                                       @PathVariable(name = "employeeId") Long employeeId) {
        log.info("Received request to remove project for employee");
        projectService.removeProjectFromEmployee(projectId, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
