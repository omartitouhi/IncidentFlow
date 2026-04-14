package com.omartitouhi.incidentflow;

import com.omartitouhi.incidentflow.ui.auth.LoginValidator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginValidatorTest {

    @Test
    public void isValidEmail_withValidEmail_returnsTrue() {
        assertTrue(LoginValidator.isValidEmail("user@example.com"));
    }

    @Test
    public void isValidEmail_withInvalidEmail_returnsFalse() {
        assertFalse(LoginValidator.isValidEmail("user-example.com"));
    }

    @Test
    public void isValidPassword_withBlankPassword_returnsFalse() {
        assertFalse(LoginValidator.isValidPassword("   "));
    }
}

