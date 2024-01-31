package com.workhub.Utils;

import com.workhub.entity.Employee;
import com.workhub.entity.Project;
import com.workhub.exception.TechnicalSkillsException;

public class ValidationUtils {

    public static void validateTechnicalSkills(Employee employee, Project project) {
        if (!employee.getTechnicalSkill().containsAll(project.getTechnology())) {
            throw TechnicalSkillsException.cannotAssign();
        }
    }
}
