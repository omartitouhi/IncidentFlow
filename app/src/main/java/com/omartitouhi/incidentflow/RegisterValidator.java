package com.omartitouhi.incidentflow;

import java.util.regex.Pattern;

public final class RegisterValidator {

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
	);

	private RegisterValidator() {
	}

	public enum Field {
		FULL_NAME,
		EMAIL,
		PASSWORD,
		CONFIRM_PASSWORD
	}

	public static final class ValidationResult {
		private final boolean valid;
		private final Field field;
		private final String message;

		private ValidationResult(boolean valid, Field field, String message) {
			this.valid = valid;
			this.field = field;
			this.message = message;
		}

		public static ValidationResult ok() {
			return new ValidationResult(true, null, null);
		}

		public static ValidationResult error(Field field, String message) {
			return new ValidationResult(false, field, message);
		}

		public boolean isValid() {
			return valid;
		}

		public Field getField() {
			return field;
		}

		public String getMessage() {
			return message;
		}
	}

	public static ValidationResult validate(String fullName,
											String email,
											String password,
											String confirmPassword) {
		if (fullName == null || fullName.trim().isEmpty()) {
			return ValidationResult.error(Field.FULL_NAME, "Le nom complet est requis.");
		}

		if (email == null || email.trim().isEmpty()) {
			return ValidationResult.error(Field.EMAIL, "L'email est requis.");
		}

		if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
			return ValidationResult.error(Field.EMAIL, "Format d'email invalide.");
		}

		if (password == null || password.isEmpty()) {
			return ValidationResult.error(Field.PASSWORD, "Le mot de passe est requis.");
		}

		if (password.length() < 6) {
			return ValidationResult.error(Field.PASSWORD, "Le mot de passe doit contenir au moins 6 caracteres.");
		}

		if (confirmPassword == null || confirmPassword.isEmpty()) {
			return ValidationResult.error(Field.CONFIRM_PASSWORD, "Confirmez votre mot de passe.");
		}

		if (!password.equals(confirmPassword)) {
			return ValidationResult.error(Field.CONFIRM_PASSWORD, "Les mots de passe ne correspondent pas.");
		}

		return ValidationResult.ok();
	}
}

