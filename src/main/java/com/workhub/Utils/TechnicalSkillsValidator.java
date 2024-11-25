package com.workhub.Utils;

import com.workhub.entity.Employee;
import com.workhub.entity.Project;
import com.workhub.exception.ExceptionUtil;
import com.workhub.exception.WorkhubException;
import org.springframework.stereotype.Component;

@Component
public class TechnicalSkillsValidator implements ValidationUtils {

    @Override
    public void validateTechnicalSkills(Employee employee, Project project) {
        if (!employee.getTechnicalSkill().containsAll(project.getTechnology())) {
            throw ExceptionUtil.logAndBuildException(WorkhubException.BAD_REQUEST);
        }
    }
}
