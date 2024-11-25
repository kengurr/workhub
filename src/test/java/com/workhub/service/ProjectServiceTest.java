package com.workhub.service;

import com.workhub.Utils.TechnicalSkillsValidator;
import com.workhub.entity.Employee;
import com.workhub.entity.Project;
import com.workhub.dto.Role;
import com.workhub.dto.Technology;
import com.workhub.exception.ServiceProcessingException;
import com.workhub.repository.EmployeeRepository;
import com.workhub.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TechnicalSkillsValidator technicalSkillsValidator;

    @InjectMocks
    private ProjectService projectService;

    private Project testProject;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testProject = new Project(1L, "Project 1", new HashSet<>(), new HashSet<>());
        testEmployee = new Employee(1L, "Employee 1", "employee1@example.com", "pass", Role.ADMIN, EnumSet.allOf(Technology.class), new HashSet<>());
    }

    @Test
    void getProject_WhenProjectFound_shouldReturnProject() {
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));

        assertEquals(testProject, projectService.getProject(testProject.getId()));

        verify(projectRepository, times(1)).findById(testProject.getId());
    }

    @Test
    void getProject_WhenProjectNotFound_shouldThrowException() {
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class,
                () -> projectService.getProject(testProject.getId()));

        verify(projectRepository, times(1)).findById(testProject.getId());
    }

    @Test
    void getProjects_shouldReturnAllProjects() {
        when(projectRepository.findAll()).thenReturn(Collections.singletonList(testProject));

        assertEquals(Collections.singletonList(testProject), projectService.getProjects());

        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void createProject_shouldSaveProject() {
        assertDoesNotThrow(() -> projectService.createProject(testProject));
        verify(projectRepository, times(1)).save(testProject);
    }

    @Test
    void updateProject_whenProjectExists_shouldUpdateProject() {
        when(projectRepository.existsById(testProject.getId())).thenReturn(true);

        assertDoesNotThrow(() -> projectService.updateProject(testProject.getId(), testProject));

        verify(projectRepository, times(1)).save(testProject);
    }

    @Test
    void updateProject_whenProjectDoesNotExist_shouldThrowException() {
        when(projectRepository.existsById(testProject.getId())).thenReturn(false);

        assertThrows(ServiceProcessingException.class,
                () -> projectService.updateProject(testProject.getId(), testProject));

        verify(projectRepository, never()).save(testProject);
    }

    @Test
    void deleteProject_whenProjectExists_shouldDeleteProject() {
        when(projectRepository.existsById(testProject.getId())).thenReturn(true);

        projectService.deleteProject(testProject.getId());

        verify(projectRepository, times(1)).deleteById(testProject.getId());
    }

    @Test
    void deleteProject_whenProjectDoesNotExist_shouldThrowException() {
        when(projectRepository.existsById(testProject.getId())).thenReturn(false);

        assertThrows(ServiceProcessingException.class,
                () -> projectService.deleteProject(testProject.getId()));

        verify(projectRepository, never()).deleteById(testProject.getId());
    }

    @Test
    void removeProjectForEmployee_whenProjectAndEmployeeExist_shouldRemoveEmployee() {
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));

        assertDoesNotThrow(() -> projectService.removeProjectFromEmployee(testProject.getId(), testEmployee.getId()));

        verify(projectRepository, times(1)).save(testProject);
        assertFalse(testProject.getEmployees().contains(testEmployee));
    }

    @Test
    void removeProjectForEmployee_whenProjectNotFound_shouldThrowException() {
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class,
                () -> projectService.removeProjectFromEmployee(testProject.getId(), testEmployee.getId()));

        verify(projectRepository, never()).save(any());
    }

    @Test
    void removeProjectForEmployee_whenEmployeeNotFound_shouldThrowException() {
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class,
                () -> projectService.removeProjectFromEmployee(testProject.getId(), testEmployee.getId()));

        verify(projectRepository, never()).save(any());
    }

    @Test
    void assignProjectToEmployee_whenProjectAndEmployeeExist_shouldAssignProject() {
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));

        assertDoesNotThrow(() -> projectService.assignProjectToEmployee(testProject.getId(), testEmployee.getId()));

        verify(technicalSkillsValidator, times(1)).validateTechnicalSkills(testEmployee, testProject);
        verify(projectRepository, times(1)).save(testProject);
        assertTrue(testProject.getEmployees().contains(testEmployee));
    }

    @Test
    void assignProjectToEmployee_whenProjectNotFound_shouldThrowException() {
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class,
                () -> projectService.assignProjectToEmployee(testProject.getId(), testEmployee.getId()));

        verify(projectRepository, never()).save(any());
    }

    @Test
    void assignProjectToEmployee_whenEmployeeNotFound_shouldThrowException() {
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class,
                () -> projectService.assignProjectToEmployee(testProject.getId(), testEmployee.getId()));

        verify(projectRepository, never()).save(any());
    }

    @Test
    void assignProjectToEmployee_WhenTechnicalSkillsMismatch_ShouldThrowException() {
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));

        doThrow(ServiceProcessingException.class)
                .when(technicalSkillsValidator).validateTechnicalSkills(testEmployee, testProject);

        assertThrows(ServiceProcessingException.class,
                () -> projectService.assignProjectToEmployee(testProject.getId(), testEmployee.getId()));

        verify(projectRepository, never()).save(any());
    }
}