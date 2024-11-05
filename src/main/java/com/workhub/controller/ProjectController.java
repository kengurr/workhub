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
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/")
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/get/{projectId}")
    public Project getProject(@PathVariable(name = "projectId") Long projectId) {
        return projectService.getProject(projectId);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        projectService.createProject(project);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/update/{projectId}")
    public ResponseEntity<?> updateProject(@RequestBody Project project,
                                           @PathVariable(name = "projectId") Long projectId) {
        projectService.updateProject(projectId, project);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable(name = "projectId") Long projectId) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/create-project-for-employee/{employeeId}")
    public ResponseEntity<?> createProjectForEmployee(@RequestBody Project project,
                                                      @PathVariable(name = "employeeId") Long employeeId) {
        projectService.createProjectForEmployee(project, employeeId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/remove-project-for-employee/{projectId}/{employeeId}")
    public ResponseEntity<?> removeProjectForEmployee(@PathVariable(name = "projectId") Long projectId,
                                        @PathVariable(name = "employeeId") Long employeeId) {
        projectService.removeProjectForEmployee(projectId, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/assign-project/{projectId}/{employeeId}")
    public ResponseEntity<?> assignProjectToEmployee(@PathVariable(name = "projectId") Long projectId,
                                                     @PathVariable(name = "employeeId") Long employeeId) {
        projectService.assignProjectToEmployee(projectId, employeeId);
        return ResponseEntity.ok("Project assigned to employee successfully");
    }
}
