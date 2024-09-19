import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation ของ Tokenizer โดยใช้ Regular Expressions
 */
public class RegexTokenizer implements Tokenizer {
    private static final String TOKEN_PATTERN =
            "(?<LABEL>[a-zA-Z_][a-zA-Z0-9_]*:?)|" + // label สามารถมี ":" ได้
                    "(?<INSTRUCTION>lw|sw|beq|add|nop)|" +
                    "(?<REGISTER>\\d)|" +
                    "(?<IMMEDIATE>-?\\d+)|" +
                    "(?<WHITESPACE>\\s+)|" +
                    "(?<UNKNOWN>.)";

    private static final Pattern pattern = Pattern.compile(TOKEN_PATTERN);

    @Override
    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group("WHITESPACE") != null) {
                // ข้าม whitespaces
                continue;
            } else if (matcher.group("UNKNOWN") != null) {
                // เก็บข้อผิดพลาดสำหรับตัวละครที่ไม่รู้จัก
                throw new RuntimeException("Unexpected character: " + matcher.group("UNKNOWN"));
            } else {
                if (matcher.group("LABEL") != null) {
                    tokens.add(new Token("LABEL", matcher.group("LABEL")));
                } else if (matcher.group("INSTRUCTION") != null) {
                    tokens.add(new Token("INSTRUCTION", matcher.group("INSTRUCTION")));
                } else if (matcher.group("REGISTER") != null) {
                    tokens.add(new Token("REGISTER", matcher.group("REGISTER")));
                } else if (matcher.group("IMMEDIATE") != null) {
                    tokens.add(new Token("IMMEDIATE", matcher.group("IMMEDIATE")));
                }
            }
        }
        return tokens;
    }
}
