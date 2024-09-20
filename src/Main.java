package src;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // ใช้ชื่อไฟล์ที่ต้องการอ่าน
        String filename = "src/input.txt";

        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("File not found: " + filename);
            return;
        }

        try {
            List<String> assemblyLines = FileUtil.readFile(filename);
            if (assemblyLines.isEmpty()) {
                System.err.println("The assembly file is empty.");
                return;
            }

            Assembler assembler = new Assembler();
            List<Integer> machineCode = assembler.assemble(assemblyLines);

            // แสดงผล machine code
            System.out.println("Machine Code:");
            for (int code : machineCode) {
                System.out.println(code);
            }

            // เขียน machine code ไปยัง output.txt
            FileUtil.writeFile("src/output.txt", machineCode);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
