import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;

public class ransomewareencryptor {
    private static final String ALGORITHM = "AES";
    private static final String AES_KEY_BASE64 = "mL0NDJzMa2RB1bmF4OFFjtPYt8KAWu+5x1OhjUfX4Q="; // ← Replace this with your actual key

    public static void main(String[] args) throws Exception {
        String targetDir = "/home/ubuntu/Documents/critical";

        byte[] decodedKey = Base64.getDecoder().decode(AES_KEY_BASE64);
        SecretKeySpec key = new SecretKeySpec(decodedKey, ALGORITHM);

        Files.walk(Paths.get(targetDir))
                .filter(path -> path.toString().endsWith(".enc"))
                .forEach(file -> {
                    try {
                        decryptFile(file.toFile(), key);
                        file.toFile().delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        System.out.println("✅ All files decrypted.");
    }

    private static void decryptFile(File file, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] encryptedData = Files.readAllBytes(file.toPath());
        byte[] originalData = cipher.doFinal(encryptedData);

        String originalPath = file.getAbsolutePath().replace(".enc", "");
        FileOutputStream fos = new FileOutputStream(originalPath);
        fos.write(originalData);
        fos.close();
    }
}
