import java.util.List;

/**
 * Interface สำหรับ Tokenizer
 */
public interface Tokenizer {
    /**
     * แปลงข้อความที่ได้รับเป็นรายการของ Token
     * @param input ข้อความที่จะถูกแปลงเป็น Token
     * @return รายการของ Token ที่ได้จากการแปลง
     */
    List<Token> tokenize(String input);
}
