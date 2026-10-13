import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {

    // Small local blacklist of common/weak passwords (never sent externally)
    private static final List<String> COMMON_PASSWORDS = List.of(
            "password", "123456", "12345678", "qwerty", "admin123",
            "letmein", "welcome", "novabank", "password123", "abc123"
    );

    public static class ValidationResult {
        private final boolean valid;
        private final String strengthCategory; // Weak, Moderate, Strong
        private final List<String> feedbackMessages;

        public ValidationResult(boolean valid, String strengthCategory, List<String> feedbackMessages) {
            this.valid = valid;
            this.strengthCategory = strengthCategory;
            this.feedbackMessages = feedbackMessages;
        }

        public boolean isValid() { return valid; }
        public String getStrengthCategory() { return strengthCategory; }
        public List<String> getFeedbackMessages() { return feedbackMessages; }
    }

    public static ValidationResult evaluatePassword(String password) {
        List<String> feedback = new ArrayList<>();
        int score = 0;

        if (password == null || password.isEmpty()) {
            feedback.add("Password cannot be empty.");
            return new ValidationResult(false, "Weak", feedback);
        }

        // 1. Length check
        if (password.length() >= 8) {
            score++;
        } else {
            feedback.add("Password should be at least 8 characters long.");
        }

        if (password.length() >= 12) {
            score++;
        }

        // 2. Diversity checks
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasLower = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        if (hasUpper) score++;
        else feedback.add("Add uppercase letters (A-Z).");

        if (hasLower) score++;
        else feedback.add("Add lowercase letters (a-z).");

        if (hasDigit) score++;
        else feedback.add("Add numbers (0-9).");

        if (hasSpecial) score++;
        else feedback.add("Add special characters (e.g., @, #, $).");

        // 3. Blacklist check
        if (COMMON_PASSWORDS.contains(password.toLowerCase())) {
            feedback.add("This password is too common and easily guessed.");
            return new ValidationResult(false, "Weak", feedback);
        }

        // 4. Determine strength category
        String category;
        boolean isValid;
        if (score <= 3) {
            category = "Weak";
            isValid = false;
            feedback.add(0, "Strength: Weak. Please improve your password.");
        } else if (score <= 5) {
            category = "Moderate";
            isValid = true;
            feedback.add(0, "Strength: Moderate.");
        } else {
            category = "Strong";
            isValid = true;
            feedback.add(0, "Strength: Strong!");
        }

        return new ValidationResult(isValid, category, feedback);
    }
}