package com.workhub.controller;

import com.workhub.entity.Project;
import com.workhub.service.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;

    @Autowired
    public ProjectController(ProjectServiceImpl projectServiceImpl) {
        this.projectServiceImpl = projectServiceImpl;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/get/{projectId}")
    public Project getProject(@PathVariable(name = "projectId") Long projectId) {
        return projectServiceImpl.getProject(projectId);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        projectServiceImpl.createProject(project);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/update/{projectId}")
    public ResponseEntity<?> updateProject(@RequestBody Project project,
                                           @PathVariable(name = "projectId") Long projectId) {
        projectServiceImpl.updateProject(projectId, project);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable(name = "projectId") Long projectId) {
        projectServiceImpl.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/create-project-for-employee/{employeeId}")
    public ResponseEntity<?> createProjectForEmployee(@RequestBody Project project,
                                                      @PathVariable(name = "employeeId") Long employeeId) {
        projectServiceImpl.createProjectForEmployee(project, employeeId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/remove-project-for-employee/{projectId}/{employeeId}")
    public ResponseEntity<?> removeProjectForEmployee(@PathVariable(name = "projectId") Long projectId,
                                        @PathVariable(name = "employeeId") Long employeeId) {
        projectServiceImpl.removeProjectForEmployee(projectId, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/assign-project/{projectId}/{employeeId}")
    public ResponseEntity<?> assignProjectToEmployee(@PathVariable(name = "projectId") Long projectId,
                                                     @PathVariable(name = "employeeId") Long employeeId) {
        projectServiceImpl.assignProjectToEmployee(projectId, employeeId);
        return ResponseEntity.ok("Project assigned to employee successfully");
    }
}
