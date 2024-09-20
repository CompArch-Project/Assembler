package src;

import java.io.*;
import java.util.*;

public class FileUtil {
    public static List<String> readFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }

    public static void writeFile(String filename, List<Integer> machineCode) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int code : machineCode) {
                writer.write(Integer.toString(code));
                writer.newLine();
            }
        }
    }
}
