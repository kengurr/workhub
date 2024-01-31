package com.workhub.Utils;

import com.workhub.entity.Employee;
import com.workhub.entity.Project;

public class ValidationUtils {

    public static void validateTechnicalSkills(Employee employee, Project project) {
        if (!employee.getTechnicalSkill().containsAll(project.getTechnology())) {
            throw new IllegalArgumentException("Cannot assign employee to project, employee doesn't have technical skills");
        }
    }
}
