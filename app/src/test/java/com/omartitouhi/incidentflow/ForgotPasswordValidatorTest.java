package com.omartitouhi.incidentflow;

import com.omartitouhi.incidentflow.ui.auth.ForgotPasswordValidator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ForgotPasswordValidatorTest {

    @Test
    public void isValidEmail_withValidEmail_returnsTrue() {
        assertTrue(ForgotPasswordValidator.isValidEmail("user@example.com"));
    }

    @Test
    public void isValidEmail_withEmptyEmail_returnsFalse() {
        assertFalse(ForgotPasswordValidator.isValidEmail(""));
    }

    @Test
    public void isValidEmail_withInvalidFormat_returnsFalse() {
        assertFalse(ForgotPasswordValidator.isValidEmail("user-at-example.com"));
    }
}

