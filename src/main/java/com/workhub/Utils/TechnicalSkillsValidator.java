package com.workhub.Utils;

import com.workhub.entity.Employee;
import com.workhub.entity.Project;
import com.workhub.exception.TechnicalSkillsException;
import org.springframework.stereotype.Component;

@Component
public class TechnicalSkillsValidator implements ValidationUtils {

    @Override
    public void validateTechnicalSkills(Employee employee, Project project) throws TechnicalSkillsException {
        if (!employee.getTechnicalSkill().containsAll(project.getTechnology())) {
            throw TechnicalSkillsException.cannotAssign();
        }
    }
}
