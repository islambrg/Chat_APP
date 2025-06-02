import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class SecurityUtils {
    // Password hashing
    public static String hashSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Input validation
    public static boolean isValidInput(String input) {
        return input != null && input.matches("^[a-zA-Z0-9_]{1,20}$");
    }

    public static boolean containsSQLPatterns(String input) {
        final String[] sqlPatterns = {
            "(?i)\\b(DROP|DELETE|INSERT|ALTER|CREATE|TRUNCATE|UNION|SELECT|UPDATE)\\b",
            "[;\\\\/]", "--", "/\\*.*\\*/", "'", "\""
        };
        
        for (String pattern : sqlPatterns) {
            if (Pattern.compile(pattern).matcher(input).find()) {
                return true;
            }
        }
        return false;
    }
}