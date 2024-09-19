import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Parser สำหรับ Assembly Language
 */
public class AssemblyParser implements Parser {
    private List<Token> tokens;
    private int currentIndex = 0;
    private Map<String, Integer> labelAddresses = new HashMap<>();

    @Override
    public void parse(List<Token> tokens) throws ParseException {
        this.tokens = tokens;
        firstPass();  // ขั้นตอนแรก: ค้นหา labels
        secondPass(); // ขั้นตอนที่สอง: แปลง Assembly เป็น Machine Code
    }

    private void firstPass() throws ParseException {
        int address = 0;
        while (hasMoreTokens()) {
            Token token = nextToken();
            if (token.getType().equals("LABEL")) {
                String label = token.getValue();
                if (label.endsWith(":")) {
                    label = label.substring(0, label.length() - 1); // Remove ":"
                }
                if (labelAddresses.containsKey(label)) {
                    throw new ParseException("Label " + label + " is already defined.");
                }
                labelAddresses.put(label, address);
            } else if (token.getType().equals("INSTRUCTION")) {
                address++;
                if (token.getValue().equals("fill")) {
                    address++; // Move address for .fill pseudo instruction
                }
            }
        }
    }

    private void secondPass() throws ParseException {
        currentIndex = 0;
        while (hasMoreTokens()) {
            Token token = nextToken();
            if (token.getType().equals("LABEL")) {
                // Skip labels
                continue;
            }
            if (token.getType().equals("INSTRUCTION")) {
                String instruction = token.getValue();
                // Implement specific instruction parsing here
                if (instruction.equals("lw") || instruction.equals("sw")) {
                    // Handle I-type instructions
                } else if (instruction.equals("add") || instruction.equals("nand")) {
                    // Handle R-type instructions
                } else if (instruction.equals("beq")) {
                    // Handle beq instruction
                } else if (instruction.equals("jalr")) {
                    // Handle jalr instruction
                } else if (instruction.equals("noop") || instruction.equals("halt")) {
                    // Handle O-type instructions
                } else {
                    throw new ParseException("Unknown instruction: " + instruction);
                }
            } else if (token.getType().equals("IMMEDIATE")) {
                // Handle immediate values
            } else if (token.getType().equals("REGISTER")) {
                // Handle register values
            } else {
                throw new ParseException("Unexpected token: " + token.getValue());
            }
        }
    }

    private boolean hasMoreTokens() {
        return currentIndex < tokens.size();
    }

    private Token nextToken() {
        return tokens.get(currentIndex++);
    }
}
