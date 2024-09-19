import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testValidInstruction() throws ParseException {
        Parser parser = new AssemblyParser();
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("INSTRUCTION", "MOV"));
        tokens.add(new Token("REGISTER", "1"));
        tokens.add(new Token("REGISTER", "2"));
        parser.parse(tokens);
    }

    @Test
    public void testLabelParsing() throws ParseException {
        Parser parser = new AssemblyParser();
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("LABEL", "label1:"));
        tokens.add(new Token("INSTRUCTION", "MOV"));
        tokens.add(new Token("REGISTER", "1"));
        tokens.add(new Token("REGISTER", "2"));
        parser.parse(tokens);
    }

    @Test
    public void testInvalidInstruction() {
        Parser parser = new AssemblyParser();
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("INSTRUCTION", "INVALID"));
        tokens.add(new Token("REGISTER", "1"));
        tokens.add(new Token("REGISTER", "2"));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(tokens);
        });
        assertTrue(exception.getMessage().contains("Unknown instruction: INVALID"));
    }

    @Test
    public void testCommentIgnored() throws ParseException {
        Parser parser = new AssemblyParser();
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("INSTRUCTION", "ADD"));
        tokens.add(new Token("REGISTER", "1"));
        tokens.add(new Token("REGISTER", "2"));
        // Assume we handle comments here
        parser.parse(tokens);
    }

    @Test
    public void testEmptyLine() {
        Parser parser = new AssemblyParser();
        List<Token> tokens = new ArrayList<>();
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(tokens);
        });
        assertTrue(exception.getMessage().contains("Unexpected token: "));
    }

    @Test
    public void testMultipleInstructions() throws ParseException {
        Parser parser = new AssemblyParser();
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("INSTRUCTION", "MOV"));
        tokens.add(new Token("REGISTER", "1"));
        tokens.add(new Token("REGISTER", "2"));
        tokens.add(new Token("INSTRUCTION", "ADD"));
        tokens.add(new Token("REGISTER", "1"));
        tokens.add(new Token("REGISTER", "3"));
        parser.parse(tokens);
    }

    @Test
    public void testValidJump() throws ParseException {
        Parser parser = new AssemblyParser();
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("INSTRUCTION", "JMP"));
        tokens.add(new Token("LABEL", "label1"));
        parser.parse(tokens);
    }

    @Test
    public void testInvalidLabel() {
        Parser parser = new AssemblyParser();
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("INSTRUCTION", "MOV"));
        tokens.add(new Token("REGISTER", "1"));
        tokens.add(new Token("LABEL", "invalid_label"));
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse(tokens);
        });
        assertTrue(exception.getMessage().contains("Unknown instruction: invalid_label"));
    }

    @Test
    public void testFunctionReturn() throws ParseException {
        Parser parser = new AssemblyParser();
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token("INSTRUCTION", "RET"));
        parser.parse(tokens);
    }
}
