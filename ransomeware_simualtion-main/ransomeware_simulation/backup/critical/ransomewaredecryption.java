import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.util.Base64;

public class ransomewaredecryption {
    private static final String ALGORITHM = "AES";
    private static final String AES_KEY_BASE64 = "mL0NDJzMa2RB1bmF4OFFjtPYt8KAWu+5x1OhjUfX4Q=";

    public static void main(String[] args) throws Exception {
        String targetDir = "/home/ubuntu/Documents/critical";

        byte[] decodedKey = Base64.getDecoder().decode(AES_KEY_BASE64);
        SecretKeySpec key = new SecretKeySpec(decodedKey, ALGORITHM);

        System.out.println("🔐 Using AES Key: " + AES_KEY_BASE64);
        System.out.println("📂 Target Directory: " + targetDir);

        Files.walk(Paths.get(targetDir))
                .filter(path -> path.toString().endsWith(".enc"))
                .forEach(file -> {
                    System.out.println("➡️ Found encrypted file: " + file.getFileName());

                    try {
                        decryptFile(file.toFile(), key);
                        file.toFile().delete();
                        System.out.println("✅ Decrypted: " + file.getFileName());
                    } catch (Exception e) {
                        System.out.println("❌ Failed to decrypt: " + file.getFileName());
                        e.printStackTrace();
                    }
                });

        System.out.println("🎉 Decryption phase completed.");
    }

    private static void decryptFile(File file, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM); // Default mode is AES/ECB/PKCS5Padding
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] encryptedData = Files.readAllBytes(file.toPath());
        byte[] originalData = cipher.doFinal(encryptedData);  // <— This is where errors happen if key is wrong

        String originalFilePath = file.getAbsolutePath().replace(".enc", "");
        try (FileOutputStream fos = new FileOutputStream(originalFilePath)) {
            fos.write(originalData);
        }
    }
}
