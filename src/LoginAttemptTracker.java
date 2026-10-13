import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class LoginAttemptTracker {
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION_SECONDS = 30;

    private static class AttemptRecord {
        int failedCount = 0;
        Instant lockoutEndTime = null;
    }

    private static final Map<String, AttemptRecord> attemptMap = new HashMap<>();

    public static synchronized boolean isLockedOut(String username) {
        AttemptRecord record = attemptMap.get(username.toLowerCase());
        if (record == null) return false;

        if (record.lockoutEndTime != null) {
            if (Instant.now().isBefore(record.lockoutEndTime)) {
                return true; // Still locked out
            } else {
                // Lockout expired, reset timer
                record.lockoutEndTime = null;
                record.failedCount = 0;
            }
        }
        return false;
    }

    public static synchronized void recordFailure(String username) {
        String key = username.toLowerCase();
        AttemptRecord record = attemptMap.computeIfAbsent(key, k -> new AttemptRecord());
        record.failedCount++;

        if (record.failedCount >= MAX_ATTEMPTS) {
            record.lockoutEndTime = Instant.now().plusSeconds(LOCKOUT_DURATION_SECONDS);
        }
    }

    public static synchronized void recordSuccess(String username) {
        attemptMap.remove(username.toLowerCase());
    }

    public static synchronized long getRemainingLockoutSeconds(String username) {
        AttemptRecord record = attemptMap.get(username.toLowerCase());
        if (record != null && record.lockoutEndTime != null) {
            long remaining = Instant.now().until(record.lockoutEndTime, java.time.temporal.ChronoUnit.SECONDS);
            return Math.max(0, remaining);
        }
        return 0;
    }
}