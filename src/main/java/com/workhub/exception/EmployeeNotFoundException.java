package com.workhub.exception;

public class EmployeeNotFoundException extends IllegalArgumentException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public static EmployeeNotFoundException notFoundById(Long employeeId) {
        return new EmployeeNotFoundException("Employee not found with ID: " + employeeId);
    }

    public static EmployeeNotFoundException cannotUpdate() {
        return new EmployeeNotFoundException("Cannot update employee, employee doesn't exist");
    }

    public static EmployeeNotFoundException cannotDelete() {
        return new EmployeeNotFoundException("Cannot delete employee, employee doesn't exist");
    }

}
