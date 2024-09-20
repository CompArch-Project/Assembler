package src;

import java.util.*;

public class Assembler {
    private Map<String, Integer> labels = new HashMap<>();
    private List<Integer> machineCode = new ArrayList<>();
    private int currentAddress = 0;

    public List<Integer> assemble(List<String> assemblyLines) {
        firstPass(assemblyLines);   // First pass: Collect labels
        secondPass(assemblyLines);  // Second pass: Generate machine code
        return machineCode;
    }

    private void firstPass(List<String> assemblyLines) {
        for (String line : assemblyLines) {
            String[] parts = line.split("\\s+");
            if (parts.length > 0 && parts[0].endsWith(":")) {
                String label = parts[0].substring(0, parts[0].length() - 1);
                if (labels.containsKey(label)) {
                    throw new IllegalArgumentException("Duplicate label: " + label);
                }
                labels.put(label, currentAddress);
            }
            currentAddress++;
        }
    }

    private void secondPass(List<String> assemblyLines) {
        currentAddress = 0;
        for (String line : assemblyLines) {
            String[] parts = line.split("\\s+");
            String label = null;
            String instruction = line;

            if (parts.length > 0 && parts[0].endsWith(":")) {
                label = parts[0].substring(0, parts[0].length() - 1);
                instruction = line.substring(line.indexOf(":") + 1).trim();
            }

            if (instruction.startsWith(".fill")) {
                processFill(instruction);
            } else {
                int machineInstr = Instruction.toMachineCode(instruction, labels, currentAddress);
                machineCode.add(machineInstr);
            }

            currentAddress++;
        }
    }

    private void processFill(String instruction) {
        String[] parts = instruction.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException(".fill requires exactly one argument");
        }
        String value = parts[1];

        int fillValue;
        if (labels.containsKey(value)) {
            fillValue = labels.get(value);
        } else {
            try {
                fillValue = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value for .fill: " + value);
            }
        }

        machineCode.add(fillValue);
    }
}
