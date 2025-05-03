=import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

public class filemonitorwithlogger {

    private static final String TARGET_DIR = "/home/ubuntu/Documents/critical";
    private static final String LOG_FILE = "file_monitor_log.csv";

    public static void main(String[] args) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(TARGET_DIR);

            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            System.out.println("👀 Monitoring started on: " + TARGET_DIR);
            System.out.println("Logging to: " + LOG_FILE);

            // Add CSV header
            try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
                writer.write("Timestamp,Event Type,File Name\n");
            }

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    String fileName = event.context().toString();
                    String eventType = event.kind().toString();
                    String timestamp = LocalDateTime.now().toString();

                    System.out.println("📁 " + eventType + ": " + fileName);

                    try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
                        writer.write(timestamp + "," + eventType + "," + fileName + "\n");
                    }

                    if (fileName.endsWith(".enc")) {
                        System.out.println("⚠️ ALERT: Potential ransomware detected on file: " + fileName);
                    }
                }

                boolean valid = key.reset();
                if (!valid) break;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
