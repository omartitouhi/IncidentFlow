package com.omartitouhi.incidentflow.ui.auth;

import java.util.regex.Pattern;

public final class ForgotPasswordValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private ForgotPasswordValidator() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isEmailEmpty(String email) {
        return email == null || email.trim().isEmpty();
    }
}

