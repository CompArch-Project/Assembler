import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class TokenizerTest {

    @Test
    public void testSingleInstruction() {
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize("MOV");
        assertEquals(1, tokens.size());
        assertEquals("INSTRUCTION", tokens.get(0).getType());
        assertEquals("MOV", tokens.get(0).getValue());
    }

    @Test
    public void testMultipleTokens() {
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize("ADD R1, R2");
        assertEquals(3, tokens.size());
        assertEquals("INSTRUCTION", tokens.get(0).getType());
        assertEquals("ADD", tokens.get(0).getValue());
        assertEquals("REGISTER", tokens.get(1).getType());
        assertEquals("1", tokens.get(1).getValue());
        assertEquals("REGISTER", tokens.get(2).getType());
        assertEquals("2", tokens.get(2).getValue());
    }

    @Test
    public void testLabelToken() {
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize("label1: MOV R1, R2");
        assertEquals(3, tokens.size());
        assertEquals("LABEL", tokens.get(0).getType());
        assertEquals("label1:", tokens.get(0).getValue());
    }

    @Test
    public void testIgnoreWhitespace() {
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize("   SUB   R1,  R2  ");
        assertEquals(3, tokens.size());
        assertEquals("INSTRUCTION", tokens.get(0).getType());
        assertEquals("SUB", tokens.get(0).getValue());
    }

    @Test
    public void testImmediateValue() {
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize("ADD R1, -5");
        assertEquals(3, tokens.size());
        assertEquals("IMMEDIATE", tokens.get(2).getType());
        assertEquals("-5", tokens.get(2).getValue());
    }

    @Test
    public void testInvalidCharacter() {
        Tokenizer tokenizer = new RegexTokenizer();
        Exception exception = assertThrows(RuntimeException.class, () -> {
            tokenizer.tokenize("MOV R1, @R2");
        });
        assertTrue(exception.getMessage().contains("Unexpected character: @"));
    }

    @Test
    public void testEmptyInput() {
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize("");
        assertEquals(0, tokens.size());
    }

    @Test
    public void testMultipleInstructions() {
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize("MOV R1, R2\nADD R2, R3");
        assertEquals(5, tokens.size());
    }

    @Test
    public void testCommentIgnored() {
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize("ADD R1, R2 ; Add R1 and R2");
        assertEquals(3, tokens.size());
    }

    @Test
    public void testRegisterToken() {
        Tokenizer tokenizer = new RegexTokenizer();
        List<Token> tokens = tokenizer.tokenize("sw R0, R1");
        assertEquals(3, tokens.size());
        assertEquals("REGISTER", tokens.get(1).getType());
        assertEquals("0", tokens.get(1).getValue());
    }
}
