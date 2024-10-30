package com.workhub.Utils;

import com.workhub.entity.Employee;
import com.workhub.entity.Project;
import com.workhub.exception.TechnicalSkillsException;

public interface ValidationUtils {

    void validateTechnicalSkills(Employee employee, Project project) throws TechnicalSkillsException;

}
