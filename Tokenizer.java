public interface Tokenizer {
    /**
     * ตรวจสอบว่ามี token ต่อไปหรือไม่
     *
     * @return true หากมี token ต่อไป
     */
    boolean hasNext();

    /**
     * ดึง token ถัดไปจากชุดข้อมูล
     *
     * @return token ถัดไปในรูปแบบ String
     */
    String next();

    /**
     * รีเซ็ตตัว tokenizer กลับไปยังจุดเริ่มต้น
     */
    void repositionToStart();
}
