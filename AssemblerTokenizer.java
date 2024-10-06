import java.util.StringTokenizer;

public class AssemblerTokenizer implements Tokenizer {
    StringTokenizer start_token; // Tokenizer หลักสำหรับเริ่มการแยกคำ
    StringTokenizer repostion_token; // สำเนา Tokenizer สำหรับการรีเซ็ตกลับจุดเริ่มต้น

    /**
     * คอนสตรัคเตอร์สำหรับการแยกคำในโค้ด Assembly
     *
     * @param Assembly โค้ด Assembly ที่จะถูกแยกคำ
     */
    AssemblerTokenizer(String Assembly){
        start_token = new StringTokenizer(Assembly , " \t"); // แยกคำโดยใช้ช่องว่างและแท็บเป็นตัวแบ่ง
        repostion_token = new StringTokenizer(Assembly , " \t"); // สำรองตัวแยกคำสำหรับการรีเซ็ต
    }

    /**
     * ตรวจสอบว่ามี token ต่อไปหรือไม่
     *
     * @return true หากยังมี token ต่อไป
     */
    @Override
    public boolean hasNext() {
        return start_token.hasMoreTokens();
    }

    /**
     * ดึง token ถัดไปจากชุดข้อมูล
     *
     * @return token ถัดไปในรูปแบบ String
     */
    @Override
    public String next() {
        return start_token.nextToken();
    }

    /**
     * รีเซ็ตตัว tokenizer กลับไปยังจุดเริ่มต้น
     */
    @Override
    public void repositionToStart() {
        start_token = repostion_token; // รีเซ็ตตัวแยกคำไปที่จุดเริ่มต้น
    }
}
