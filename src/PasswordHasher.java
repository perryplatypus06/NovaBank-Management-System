import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    // Work factor for BCrypt (higher = slower/more secure, 12 is the gold standard)
    private static final int COST_FACTOR = 12;

    /**
     * Hashes a plaintext password using BCrypt with a randomly generated unique salt.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST_FACTOR));
    }

    /**
     * Verifies a submitted plaintext password against a stored BCrypt hash.
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }

        try {
            // Handle legacy plaintext passwords smoothly during migration
            if (!storedHash.startsWith("$2a$") && !storedHash.startsWith("$2b$") && !storedHash.startsWith("$2y$")) {
                return plainPassword.equals(storedHash);
            }

            return BCrypt.checkpw(plainPassword, storedHash);
        } catch (Exception e) {
            // Log securely or handle malformed hash strings gracefully
            return false;
        }
    }

    /**
     * Checks if a stored password string is already hashed with BCrypt.
     */
    public static boolean isHashed(String passwordField) {
        if (passwordField == null) return false;
        return passwordField.startsWith("$2a$") || passwordField.startsWith("$2b$") || passwordField.startsWith("$2y$");
    }
}