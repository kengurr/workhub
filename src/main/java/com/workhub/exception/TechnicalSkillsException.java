package com.workhub.exception;

public class TechnicalSkillsException extends IllegalArgumentException {
    public TechnicalSkillsException(String message) {
        super(message);
    }

    public static TechnicalSkillsException cannotAssign() {
        return new TechnicalSkillsException("Cannot assign, employee doesn't have matching technical skills");
    }
}
