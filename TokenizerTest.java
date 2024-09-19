import java.util.List;

public class TokenizerTest {
    public static void main(String[] args) {
        String code = "lw 1 2 3\nlabel1:\nsw 4 5 6";
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize(code);

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}