import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;

public class cleandecryptor {

    private static final String ALGORITHM = "AES";
    private static final String FIXED_KEY = "12345678901234567890123456789012";

    public static void main(String[] args) throws Exception {
        String targetDir = "/home/ubuntu/Documents/critical";

        SecretKeySpec secretKey = new SecretKeySpec(FIXED_KEY.getBytes(), ALGORITHM);

        Files.walk(Paths.get(targetDir))
                .filter(path -> path.toString().endsWith(".enc"))
                .forEach(file -> {
                    try {
                        System.out.println("🔓 Decrypting: " + file.getFileName());

                        byte[] fileData = Files.readAllBytes(file);
                        Cipher cipher = Cipher.getInstance(ALGORITHM);
                        cipher.init(Cipher.DECRYPT_MODE, secretKey);
                        byte[] originalData = cipher.doFinal(fileData);

                        String originalPath = file.toString().replace(".enc", "");
                        Files.write(Paths.get(originalPath), originalData);
                        file.toFile().delete();
                    } catch (Exception e) {
                        System.out.println("❌ Failed: " + file.getFileName());
                        e.printStackTrace();
                    }
                });

        System.out.println("✅ All files decrypted using fixed key.");
    }
}
