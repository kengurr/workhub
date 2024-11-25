package com.workhub.service;

import com.workhub.Utils.TechnicalSkillsValidator;
import com.workhub.dto.EmployeeDto;
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

    private EmployeeDto testEmployeeDto;

    private Project testProject;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee(1L, "Employee", "test@example.com", "password123", Role.ADMIN, EnumSet.noneOf(Technology.class), new HashSet<>());
        testProject = new Project(1L, "Project", new HashSet<>(), new HashSet<>());
        testEmployeeDto = new EmployeeDto("Employee", "test@example.com", "ADMIN", EnumSet.noneOf(Technology.class));
    }

    @Test
    void getEmployees_ShouldReturnListOfEmployeeDtos() {
        when(employeeRepository.findAll()).thenReturn(List.of(testEmployee));

        var result = employeeService.getEmployees();

        assertEquals(1, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployee_WhenExists_ShouldReturnEmployeeDto() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        var result = employeeService.getEmployee(1L);

        assertEquals(testEmployee.getEmail(), result.getEmail());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployee_WhenNotExists_ShouldThrowException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceProcessingException.class, () -> employeeService.getEmployee(1L));
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void createEmployee_WhenEmailIsUnique_ShouldSaveEmployee() {
        when(employeeRepository.countByEmail(testEmployeeDto.getEmail())).thenReturn(0L);

        employeeService.createEmployee(testEmployeeDto);

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_WhenEmailIsNotUnique_ShouldThrowException() {
        when(employeeRepository.countByEmail(testEmployeeDto.getEmail())).thenReturn(1L);

        assertThrows(ServiceProcessingException.class, () -> employeeService.createEmployee(testEmployeeDto));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployee_WhenExists_ShouldUpdateEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.updateEmployee(1L, testEmployeeDto);

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee_WhenNotExists_ShouldThrowException() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThrows(ServiceProcessingException.class, () -> employeeService.updateEmployee(1L, testEmployeeDto));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteEmployee_WhenExists_ShouldDeleteEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEmployee_WhenNotExists_ShouldThrowException() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThrows(ServiceProcessingException.class, () -> employeeService.deleteEmployee(1L));
        verify(employeeRepository, never()).deleteById(anyLong());
    }

    @Test
    void assignEmployeeToProject_WhenValid_ShouldAssignProject() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        doNothing().when(technicalSkillsValidator).validateTechnicalSkills(testEmployee, testProject);

        employeeService.assignEmployeeToProject(1L, 1L);

        assertTrue(testEmployee.getProjects().contains(testProject));
        assertTrue(testProject.getEmployees().contains(testEmployee));
        verify(employeeRepository, times(1)).save(testEmployee);
        verify(technicalSkillsValidator, times(1)).validateTechnicalSkills(testEmployee, testProject);
    }

    @Test
    void removeEmployeeFromProject_WhenValid_ShouldRemoveProject() {
        testEmployee.getProjects().add(testProject);
        testProject.getEmployees().add(testEmployee);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        employeeService.removeEmployeeFromProject(1L, 1L);

        assertFalse(testEmployee.getProjects().contains(testProject));
        verify(employeeRepository, times(1)).save(testEmployee);
    }
}