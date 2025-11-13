import java.io.*;
import java.text.DecimalFormat;
import java.util.Random;

public class TaoFileLog {
    public static void main(String[] args) {
        int soFile = 3000;             // number of files
        int soDongMoiFile = 20000;     // lines per file
        Random random = new Random();

        String[] mauLog = {
            "User login by 12 success",
            "User login by 99 failed",
            "Connection established",
            "Server started successfully",
            "User logout",
            "Database query executed",
            "File uploaded successfully",
            "User login by 99 success",
            "Error: timeout",
            "Cache cleared"
        };

        DecimalFormat df = new DecimalFormat("00");

        File thuMuc = new File("logs");
        if (!thuMuc.exists()) thuMuc.mkdir();

        System.out.println("Creating 3000 log files, please wait...");

        for (int i = 1; i <= soFile; i++) {
            String tenFile = "logs/log_" + df.format(i) + "_11_25.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tenFile))) {
                for (int j = 1; j <= soDongMoiFile; j++) {
                    String log = mauLog[random.nextInt(mauLog.length)];
                    writer.write(log);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (i % 100 == 0) {
                System.out.println("Created " + i + " files...");
            }
        }

        System.out.println("Done! 3000 log files created in folder /logs");
    }
}
