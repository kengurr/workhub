package com.workhub.service;

import com.workhub.Utils.TechnicalSkillsValidator;
import com.workhub.entity.Employee;
import com.workhub.entity.Project;
import com.workhub.entity.Technology;
import com.workhub.exception.EmployeeNotFoundException;
import com.workhub.exception.ProjectNotFoundException;
import com.workhub.exception.TechnicalSkillsException;
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
    private EmployeeServiceImpl employeeService;

    private Employee testEmployee;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee(1L, "Employee 1", "employee1@example.com", EnumSet.allOf(Technology.class), new HashSet<>());
        testProject = new Project(1L, "Project 1", new HashSet<>(), new HashSet<>());
    }
    @Test
    void getEmployees() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(testEmployee));

        assertEquals(Collections.singletonList(testEmployee), employeeService.getEmployees());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        assertEquals(testEmployee, employeeService.getEmployee(1L));

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void createEmployee_WhenEmailIsUnique_ShouldSaveEmployee() {
        when(employeeRepository.findEmployeeByEmail(testEmployee.getEmail())).thenReturn(null);

        assertDoesNotThrow(() -> employeeService.createEmployee(testEmployee));

        verify(employeeRepository, times(1)).save(testEmployee);
    }

    @Test
    void createEmployee_WhenEmailIsNotUnique_ShouldThrowException() {
        when(employeeRepository.findEmployeeByEmail(testEmployee.getEmail())).thenReturn(testEmployee);

        assertThrows(IllegalStateException.class, () -> employeeService.createEmployee(testEmployee));
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

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(testEmployee.getId(), testEmployee));
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

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(testEmployee.getId()));
    }

    @Test
    void createEmployeeForProject_WhenEmailIsUnique_ShouldSaveEmployeeAndAssignToProject() {
        when(employeeRepository.findEmployeeByEmail(testEmployee.getEmail())).thenReturn(null);
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));

        assertDoesNotThrow(() -> employeeService.createEmployeeForProject(testEmployee, testProject.getId()));

        verify(employeeRepository, times(1)).save(testEmployee);
        assertTrue(testEmployee.getProjects().contains(testProject));
    }

    @Test
    void createEmployeeForProject_WhenTechnicalSkillsMismatch_ShouldThrowException() {
        when(employeeRepository.findEmployeeByEmail(testEmployee.getEmail())).thenReturn(null);
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));

        doThrow(TechnicalSkillsException.class)
                .when(technicalSkillsValidator).validateTechnicalSkills(testEmployee, testProject);

        assertThrows(TechnicalSkillsException.class, () -> employeeService.createEmployeeForProject(testEmployee, testProject.getId()));

        verify(employeeRepository, never()).save(testEmployee);
    }

    @Test
    void createEmployeeForProject_WhenEmailIsNotUnique_ShouldThrowException() {
        when(employeeRepository.findEmployeeByEmail(testEmployee.getEmail())).thenReturn(testEmployee);

        assertThrows(IllegalStateException.class, () -> employeeService.createEmployeeForProject(testEmployee, testProject.getId()));
    }

    @Test
    void createEmployeeForProject_WhenProjectNotFound_ShouldThrowException() {
        when(employeeRepository.findEmployeeByEmail(testEmployee.getEmail())).thenReturn(null);
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> employeeService.createEmployeeForProject(testEmployee, testProject.getId()));
    }

    @Test
    void removeEmployeeForProject_WhenEmployeeAndProjectExist_ShouldRemoveProject() {
        Set<Project> projects = new HashSet<>();
        projects.add(testProject);

        testProject.addEmployee(testEmployee);
        testEmployee.setProjects(projects);

        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));

        assertDoesNotThrow(() -> employeeService.removeEmployeeForProject(testEmployee.getId(), testProject.getId()));

        verify(employeeRepository, times(1)).save(testEmployee);
        assertFalse(testEmployee.getProjects().contains(testProject));
    }

    @Test
    void removeEmployeeForProject_WhenEmployeeNotFound_ShouldThrowException() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.removeEmployeeForProject(testEmployee.getId(), testProject.getId()));
    }

    @Test
    void removeEmployeeForProject_WhenProjectNotFound_ShouldThrowException() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(new Employee()));
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> employeeService.removeEmployeeForProject(testEmployee.getId(), testProject.getId()));
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

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.assignEmployeeToProject(testEmployee.getId(), testProject.getId()));
    }

    @Test
    void assignEmployeeToProject_WhenProjectNotFound_ShouldThrowException() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(new Employee()));
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> employeeService.assignEmployeeToProject(testEmployee.getId(), testProject.getId()));
    }

    @Test
    void assignEmployeeToProject_WhenTechnicalSkillsMismatch_ShouldThrowException() {
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployee));
        when(projectRepository.findById(testProject.getId())).thenReturn(Optional.of(testProject));

        doThrow(TechnicalSkillsException.class)
                .when(technicalSkillsValidator).validateTechnicalSkills(testEmployee, testProject);


        assertThrows(TechnicalSkillsException.class, () -> employeeService.assignEmployeeToProject(testEmployee.getId(), testProject.getId()));
    }
}