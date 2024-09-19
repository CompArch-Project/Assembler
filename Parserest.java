import java.util.List;

public class Parserest {
    public static void main(String[] args) {
        String code = "lw 1 2 3\nlabel1:\nsw 4 5 6";
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize(code);

        Parser parser = new AssemblyParser();
        try {
            parser.parse(tokens);
            System.out.println("Parsing completed successfully.");
        } catch (ParseException e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }
}
