import java.util.List;

public interface Parser {
    void parse(List<Token> tokens) throws ParseException;
}
