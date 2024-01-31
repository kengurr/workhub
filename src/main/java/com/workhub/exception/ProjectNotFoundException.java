package com.workhub.exception;

public class ProjectNotFoundException extends IllegalArgumentException {
    public ProjectNotFoundException(String message) {
        super(message);
    }

    public static ProjectNotFoundException notFoundById(Long projectId) {
        return new ProjectNotFoundException("Project not found with ID: " + projectId);
    }

    public static ProjectNotFoundException cannotUpdate() {
        return new ProjectNotFoundException("Cannot update project, project doesn't exist");
    }

    public static ProjectNotFoundException cannotDelete() {
        return new ProjectNotFoundException("Cannot delete project, project doesn't exist");
    }
}
