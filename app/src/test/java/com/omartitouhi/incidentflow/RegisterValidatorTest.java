package com.omartitouhi.incidentflow;

import com.omartitouhi.incidentflow.ui.auth.RegisterValidator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegisterValidatorTest {

    @Test
    public void validate_withValidData_returnsSuccess() {
        RegisterValidator.ValidationResult result = RegisterValidator.validate(
                "Omar Titouhi",
                "omar@example.com",
                "secret123",
                "secret123"
        );

        assertTrue(result.isValid());
    }

    @Test
    public void validate_withInvalidEmail_returnsEmailError() {
        RegisterValidator.ValidationResult result = RegisterValidator.validate(
                "Omar Titouhi",
                "omar_at_example.com",
                "secret123",
                "secret123"
        );

        assertFalse(result.isValid());
        assertEquals(RegisterValidator.Field.EMAIL, result.getField());
    }

    @Test
    public void validate_withPasswordMismatch_returnsConfirmPasswordError() {
        RegisterValidator.ValidationResult result = RegisterValidator.validate(
                "Omar Titouhi",
                "omar@example.com",
                "secret123",
                "other123"
        );

        assertFalse(result.isValid());
        assertEquals(RegisterValidator.Field.CONFIRM_PASSWORD, result.getField());
    }
}

