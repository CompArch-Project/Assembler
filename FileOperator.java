import java.io.*;
import java.util.List;
import java.util.Scanner;

public class FileOperator { // สำหรับการอ่านและเขียนไฟล์

    /**
     * เขียนรายการของ String ไปยังไฟล์ที่ระบุ
     *
     * @param path เส้นทางไฟล์
     * @param str_list รายการ String ที่จะเขียนลงไฟล์
     */
    public static void StringToFile(String path, List<String> str_list) {
        try {
            System.out.println("Output -> " + path);
            BufferedWriter writer = new BufferedWriter(new FileWriter(path)); // ใช้ BufferedWriter เพื่อเขียนไฟล์
            for (String str : str_list) {
                writer.write(str); // เขียนแต่ละ string ลงไฟล์
                writer.write("\n"); // เพิ่มบรรทัดใหม่
            }

            writer.close(); // ปิด writer

        } catch (IOException e) { // ตรวจจับข้อผิดพลาดหากไม่สามารถเขียนไฟล์ได้
            System.out.println("ERROR TO WRITE FILE!!!");
        }
    }

    /**
     * อ่านข้อมูลจากไฟล์และแปลงเป็น String
     *
     * @param path เส้นทางของไฟล์
     * @return ข้อมูลที่อ่านมาในรูปแบบ String
     */
    public static String FileToString(String path) {
        StringBuilder data = new StringBuilder(); // ใช้ StringBuilder เพื่อเก็บข้อมูล
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file); // ใช้ Scanner เพื่ออ่านไฟล์
            while (reader.hasNextLine()) {
                data.append(reader.nextLine()).append(" \n "); // อ่านแต่ละบรรทัดและเพิ่มลงใน StringBuilder
            }
            data.append(" \n ");
            reader.close(); // ปิด reader หลังอ่านเสร็จ
        } catch (FileNotFoundException e) { // ตรวจจับกรณีที่ไฟล์ไม่พบ
            System.out.println("FILE NOT FOUND!!! Exiting program.");
            System.exit(1);  // ออกจากโปรแกรมเมื่อไม่พบไฟล์
        }
        return data.toString(); // คืนค่าข้อมูลที่อ่านมา
    }

}