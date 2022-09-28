import java.math.BigInteger;
import java.security.MessageDigest;

public class Hash {
    static byte[] hash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(data);
        } catch (Exception e) {
            System.out.println("Failed to get instance of sha256");
            return null;
        }
    }

    static String hex(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64)
            hexString.insert(0, '0');
        return hexString.toString();
    }
}
