package src;

import java.util.Map;

public class Instruction {
    private static final Map<String, String> OPCODES = Map.of(
            "add", "000",
            "nand", "001",
            "lw", "010",
            "sw", "011",
            "beq", "100",
            "jalr", "101",
            "halt", "110",
            "noop", "111"
    );

    public static int toMachineCode(String instruction, Map<String, Integer> labels, int currentAddress) {
        String[] parts = instruction.split("\\s+");
        String opcode = parts[0];

        switch (opcode) {
            case "add":
            case "nand":
                validateRTypeArguments(parts);
                return processRType(opcode, parts);
            case "lw":
            case "sw":
            case "beq":
                return processIType(opcode, parts, labels, currentAddress);
            case "jalr":
                return processJType(parts);
            case "halt":
            case "noop":
                return processOType(opcode);
            default:
                throw new IllegalArgumentException("Unknown opcode: " + opcode);
        }
    }

    private static void validateRTypeArguments(String[] parts) {
        if (parts.length != 4) {
            throw new IllegalArgumentException("R-type instructions require exactly 3 arguments");
        }
        validateRegister(parts[1]);
        validateRegister(parts[2]);
        validateRegister(parts[3]);
    }

    private static void validateRegister(String reg) {
        int regNumber = Integer.parseInt(reg);
        if (regNumber < 0 || regNumber > 7) {
            throw new IllegalArgumentException("Register number must be between 0 and 7: " + reg);
        }
    }

    private static int processRType(String opcode, String[] parts) {
        int regA = Integer.parseInt(parts[1]);
        int regB = Integer.parseInt(parts[2]);
        int destReg = Integer.parseInt(parts[3]);
        String binary = OPCODES.get(opcode) + toBinary(regA, 3) + toBinary(regB, 3) + "0000000000000" + toBinary(destReg, 3);
        return binToDecimal(binary);
    }

    private static int processIType(String opcode, String[] parts, Map<String, Integer> labels, int currentAddress) {
        int regA = Integer.parseInt(parts[1]);
        int regB = Integer.parseInt(parts[2]);
        String offsetField = parts[3];

        int offset;
        if (isNumeric(offsetField)) {
            offset = Integer.parseInt(offsetField);
        } else if (labels.containsKey(offsetField)) {
            offset = labels.get(offsetField) - (currentAddress + 1);
        } else {
            throw new IllegalArgumentException("Undefined label: " + offsetField);
        }

        String binary = OPCODES.get(opcode) + toBinary(regA, 3) + toBinary(regB, 3) + toBinary(offset, 16);
        return binToDecimal(binary);
    }

    private static int processJType(String[] parts) {
        int regA = Integer.parseInt(parts[1]);
        int regB = Integer.parseInt(parts[2]);
        String binary = OPCODES.get("jalr") + toBinary(regA, 3) + toBinary(regB, 3) + "0000000000000000";
        return binToDecimal(binary);
    }

    private static int processOType(String opcode) {
        String binary = OPCODES.get(opcode) + "0000000000000000000000";
        return binToDecimal(binary);
    }

    private static String toBinary(int value, int bits) {
        String binary = Integer.toBinaryString(value);
        return String.format("%" + bits + "s", binary).replace(' ', '0');
    }

    private static int binToDecimal(String bin) {
        return Integer.parseInt(bin, 2);
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
