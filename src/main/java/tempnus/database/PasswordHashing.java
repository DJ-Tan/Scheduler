package tempnus.database;

import tempnus.logger.AppLogger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class PasswordHashing {

    private static final String HASH_FUNCTION = "SHA-256";

    /**
     * Converts a string to its hashed value.
     *
     * @param input String to be converted.
     * @return Hashed value of the input string.
     */
    public static String getHashValue(String input) {
        byte[] byteArray = getByteArray(input);
        return toHexString(byteArray);
    }

    /**
     * Calculates message digest of an input and returns an array of bytes.
     *
     * @param input String to be run through the hash function.
     * @return Array of bytes.
     */
    private static byte[] getByteArray(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(HASH_FUNCTION);
        } catch (NoSuchAlgorithmException e) {
            AppLogger.log(Level.SEVERE, e.getMessage());
        }
        byte[] byteArray = input.getBytes(StandardCharsets.UTF_8);
        assert md != null;
        return md.digest(byteArray);
    }

    /**
     * Convert a byte array into a hexadecimal string.
     *
     * @param hash Byte array containing the hash.
     * @return Hexadecimal string of the byte array.
     */
    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

}
