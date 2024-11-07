package com.workhub.controller;

import com.workhub.entity.Project;
import com.workhub.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/")
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{projectId}")
    public Project getProject(@PathVariable(name = "projectId") Long projectId) {
        return projectService.getProject(projectId);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        projectService.createProject(project);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{projectId}")
    public ResponseEntity<?> updateProject(@RequestBody Project project,
                                           @PathVariable(name = "projectId") Long projectId) {
        projectService.updateProject(projectId, project);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable(name = "projectId") Long projectId) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{projectId}/employees/{employeeId}")
    public ResponseEntity<?> assignProjectToEmployee(@PathVariable(name = "projectId") Long projectId,
                                                     @PathVariable(name = "employeeId") Long employeeId) {
        projectService.assignProjectToEmployee(projectId, employeeId);
        return ResponseEntity.ok("Project assigned to employee successfully");
    }

    @DeleteMapping(value = "/{projectId}/employees/{employeeId}")
    public ResponseEntity<?> removeProjectFromEmployee(@PathVariable(name = "projectId") Long projectId,
                                                       @PathVariable(name = "employeeId") Long employeeId) {
        projectService.removeProjectFromEmployee(projectId, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
