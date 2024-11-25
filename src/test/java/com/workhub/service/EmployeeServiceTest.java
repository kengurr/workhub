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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TechnicalSkillsValidator technicalSkillsValidator;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee(1L, "Employee 1", "employee1@example.com", "pass", Role.ADMIN, EnumSet.allOf(Technology.class), new HashSet<>());
        testProject = new Project(1L, "Project 1", new HashSet<>(), new HashSet<>());
    }
    @Test
    void getEmployees_shouldReturnAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(testEmployee));

        assertEquals(Collections.singletonList(testEmployee), employeeService.getEmployees());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployee_whenEmployeeExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        assertEquals(testEmployee, employeeService.getEmployee(1L));

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployee_whenEmployeeDoesNotExist() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class, () ->
            employeeService.getEmployee(testEmployee.getId()));

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void createEmployee_WhenEmailIsUnique_ShouldSaveEmployee() {
        assertDoesNotThrow(() -> employeeService.createEmployee(testEmployee));

        verify(employeeRepository, times(1)).save(testEmployee);
    }

    @Test
    void createEmployee_WhenEmailIsNotUnique_ShouldThrowException() {
        when(employeeRepository.countByEmail(testEmployee.getEmail())).thenReturn(1L);

        assertThrows(ServiceProcessingException.class, () -> employeeService.createEmployee(testEmployee));

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployee_WhenEmployeeExists_ShouldUpdateEmployee() {
        when(employeeRepository.existsById(testEmployee.getId())).thenReturn(true);

        assertDoesNotThrow(() -> employeeService.updateEmployee(testEmployee.getId(), testEmployee));

        verify(employeeRepository, times(1)).save(testEmployee);
    }

    @Test
    void updateEmployee_WhenEmployeeDoesNotExist_ShouldThrowException() {
        when(employeeRepository.existsById(testEmployee.getId())).thenReturn(false);

        assertThrows(ServiceProcessingException.class,
                () -> employeeService.updateEmployee(testEmployee.getId(), testEmployee));

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteEmployee_WhenEmployeeExists_ShouldDeleteEmployee() {
        when(employeeRepository.existsById(testEmployee.getId())).thenReturn(true);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(testEmployee.getId()));

        verify(employeeRepository, times(1)).deleteById(testEmployee.getId());
    }

    @Test
    void deleteEmployee_WhenEmployeeDoesNotExist_ShouldThrowException() {
        when(employeeRepository.existsById(testEmployee.getId())).thenReturn(false);

        assertThrows(ServiceProcessingException.class,
                () -> employeeService.deleteEmployee(testEmployee.getId()));

        verify(employeeRepository, never()).deleteById(any());
    }

    @Test
    void removeEmployeeForProject_WhenEmployeeAndProjectExist_ShouldRemoveProject() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));

        assertDoesNotThrow(() -> employeeService.removeEmployeeFromProject(testEmployee.getId(), testProject.getId()));

        verify(employeeRepository, times(1)).save(testEmployee);
        assertFalse(testEmployee.getProjects().contains(testProject));
    }

    @Test
    void removeEmployeeForProject_WhenEmployeeNotFound_ShouldThrowException() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class,
                () -> employeeService.removeEmployeeFromProject(testEmployee.getId(), testProject.getId()));

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void removeEmployeeForProject_WhenProjectNotFound_ShouldThrowException() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(new Employee(testEmployee.getName(), testEmployee.getEmail())));
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class,
                () -> employeeService.removeEmployeeFromProject(testEmployee.getId(), testProject.getId()));

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void assignEmployeeToProject_WhenEmployeeAndProjectExist_ShouldAssignProject() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));

        assertDoesNotThrow(() -> employeeService.assignEmployeeToProject(testEmployee.getId(), testProject.getId()));

        verify(employeeRepository, times(1)).save(testEmployee);
        assertTrue(testEmployee.getProjects().contains(testProject));
    }

    @Test
    void assignEmployeeToProject_WhenEmployeeNotFound_ShouldThrowException() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class,
                () -> employeeService.assignEmployeeToProject(testEmployee.getId(), testProject.getId()));

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void assignEmployeeToProject_WhenProjectNotFound_ShouldThrowException() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(new Employee(testEmployee.getName(), testEmployee.getEmail())));
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class,
                () -> employeeService.assignEmployeeToProject(testEmployee.getId(), testProject.getId()));

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void assignEmployeeToProject_WhenTechnicalSkillsMismatch_ShouldThrowException() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));

        doThrow(ServiceProcessingException.class)
                .when(technicalSkillsValidator).validateTechnicalSkills(testEmployee, testProject);


        assertThrows(ServiceProcessingException.class,
                () -> employeeService.assignEmployeeToProject(testEmployee.getId(), testProject.getId()));

        verify(employeeRepository, never()).save(any());
    }
}