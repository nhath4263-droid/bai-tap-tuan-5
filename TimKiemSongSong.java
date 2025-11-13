import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.nio.file.*;

public class TimKiemSongSong {
    private static final String FOLDER_PATH = "logs";      
    private static final String KEYWORD = "login by 99";   
    private static final String OUTPUT_FILE = "logs/ketqua.txt"; 

    public static void main(String[] args) {
        File folder = new File(FOLDER_PATH);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.out.println("No log files found in /logs folder");
            return;
        }

        int soLuongLuong = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(soLuongLuong);
        List<Future<List<String>>> futures = new ArrayList<>();

        System.out.println("Starting parallel search with " + soLuongLuong + " threads...");
        long start = System.currentTimeMillis();

        for (File file : files) {
            Future<List<String>> future = executor.submit(() -> timTrongFile(file));
            futures.add(future);
        }

        int demFile = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            for (Future<List<String>> future : futures) {
                List<String> ketQuaFile = future.get();
                for (String dong : ketQuaFile) {
                    writer.write(dong);
                    writer.newLine();
                }
                demFile++;
                if (demFile % 100 == 0) {
                    System.out.println("Processed " + demFile + " files...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.shutdown();
        long end = System.currentTimeMillis();

        System.out.println("Results written to file: " + OUTPUT_FILE);
        System.out.println("Time: " + (end - start) + " ms");
    }

    private static List<String> timTrongFile(File file) {
        List<String> ketQua = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int soDong = 0;
            while ((line = reader.readLine()) != null) {
                soDong++;
                if (line.contains(KEYWORD)) {
                    ketQua.add("File: " + file.getName() + " | Line: " + soDong + " | " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}
