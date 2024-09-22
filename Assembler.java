import java.math.BigInteger;
import java.util.*;

public class Assembler {

    // Assembler constructor
    private final AssemblerTokenizer tkz;

    private static final Map<String, String> opcodeMap = Map.of(
            "add", "000",
            "nand", "001",
            "lw", "010",
            "sw", "011",
            "beq", "100",
            "jalr", "101",
            "halt", "110",
            "noop", "111",
            ".fill", "no-opcode"
    );

    private static final Map<String, String> typeMap = Map.of(
            "add", "R",
            "nand", "R",
            "lw", "I",
            "sw", "I",
            "beq", "I",
            "jalr", "J",
            "halt", "O",
            "noop", "O",
            ".fill", "F"
    );

    private static final Map<String, Integer> fieldCountMap = Map.of(
            "add", 3,
            "nand", 3,
            "lw", 2,
            "sw", 2,
            "beq", 2,
            "jalr", 2,
            "halt", 0,
            "noop", 0,
            ".fill", 0
    );

    // Other fields and constructor
    boolean TestTool = true;
    private static final String zero_bit = "0000000";
    private final List<String> machineCodes = new ArrayList<>();
    private String machineCode = zero_bit;
    private int pointer = 0;
    public List<String> Data_list = new ArrayList<>();
    private final Map<String, Integer> Label_Mapping = new HashMap<>();

    /**
     * คอนสตรัคเตอร์สำหรับคลาส Assembler
     * รับพารามิเตอร์ assembly ที่เป็นโค้ดแอสเซมบลีที่ต้องการประมวลผล
     *
     * @param assembly โค้ดแอสเซมบลีที่นำเข้ามาเพื่อประมวลผล
     */
    Assembler(String assembly) {
        tkz = new AssemblerTokenizer(assembly);
    }

    /**
     * ดึงค่า opcode จากคำสั่งที่กำหนด
     *
     * @param instruction คำสั่งแอสเซมบลีที่ต้องการ
     * @return คืนค่า opcode ที่สอดคล้องกับคำสั่ง
     */
    public static String getOpcode(String instruction) {
        return opcodeMap.get(instruction);
    }

    /**
     * ดึงประเภทคำสั่ง (R, I, J, O, F) ของคำสั่งที่ระบุ
     *
     * @param instruction คำสั่งแอสเซมบลี
     * @return คืนค่าประเภทของคำสั่ง
     */
    public static String getType(String instruction) {
        return typeMap.get(instruction);
    }

    /**
     * ดึงจำนวนฟิลด์ที่ต้องการสำหรับคำสั่งที่กำหนด
     *
     * @param instruction คำสั่งแอสเซมบลี
     * @return จำนวนฟิลด์ที่ต้องการของคำสั่งนั้นๆ
     */
    public static int getNumberOfField(String instruction) {
        return fieldCountMap.getOrDefault(instruction, 0);
    }

    /**
     * ทำการอ่านบรรทัดของโค้ดแอสเซมบลีและแปลงเป็นข้อมูลแบบลิสต์
     */
    void parseLineToData() {
        Data_list = new ArrayList<>();
        while (tkz.hasNext()) {
            String token = tkz.next();
            if (token.equals("\n")) {
                break;
            }
            Data_list.add(token);
        }

        if (TestTool) {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("parsed! -> " + Data_list);
        }
    }

    /**
     * ทำการประมวลผลและสร้างโค้ดเครื่องจากโค้ดแอสเซมบลี
     *
     * @return คืนค่าลิสต์ของโค้ดเครื่องที่สร้างจากคำสั่งแอสเซมบลี
     */
    public List<String> computeToMachineCode() {
        System.out.println("*** Start mapping Label before compute! ***");
        LabelMapping();
        if (TestTool) System.out.println("Label Mapping : " + Label_Mapping);
        return compute();
    }

    /**
     * ทำการรีเซ็ตตำแหน่งของตัวอ่านโค้ดแอสเซมบลีไปที่จุดเริ่มต้น
     */
    public void reset() {
        tkz.repositionToStart();
        pointer = 0;
    }

    /**
     * ฟังก์ชันสำหรับการประมวลผลคำสั่งแอสเซมบลีเป็นโค้ดเครื่อง
     * อ่านข้อมูลในแต่ละบรรทัด ประมวลผล และจัดการการแปลงแต่ละประเภทของคำสั่ง
     *
     * @return คืนค่า ArrayList ของโค้ดเครื่องที่ประมวลผลแล้ว
     */
    private ArrayList<String> compute() {
        reset();
        parseLineToData();
        while (tkz.hasNext()) {
            int index = 0;
            if (TestTool) System.out.println("Line : " + (pointer + 1));

            if (Data_list.isEmpty()) {
                parseLineToData();
            }

            // ตรวจสอบว่าคำสั่งเริ่มต้นด้วย instruction หรือ label
            if (!isInstruction(Data_list.get(index))) {
                if (!isLabel(Data_list.get(index))) {
                    System.out.println("First token isn't Label");
                    exit(1);
                } else {
                    index++;
                    if (!isInstruction(Data_list.get(index))) {
                        System.out.println("Second token isn't Instruction");
                        exit(1);
                    }
                }
            }

            // รับ instruction, type, และ opcode
            String instruction = Data_list.get(index);
            String type = getType(instruction);
            String opcode = getOpcode(instruction);

            if (TestTool) {
                System.out.println("Instruction : " + instruction);
                System.out.println("Type : " + type);
                System.out.println("Opcode : " + opcode);
            }

            // กำหนดค่าพื้นฐาน
            int FieldNum = getNumberOfField(instruction);
            String[] fields = new String[3];
            machineCode = zero_bit + opcode;

            // ทำการแปลงแต่ละฟิลด์เป็นบิต
            for (int i = 0; i < FieldNum; ++i) {
                fields[i] = Data_list.get(index + 1 + i);

                // ตรวจสอบว่าเป็นจำนวนเต็มหรือไม่
                if (!isInteger(fields[i])) {
                    System.out.println("Fields -> " + i + " is not an integer.");
                    exit(1);
                }

                // แปลงเป็นจำนวนเต็มแล้วเช็คค่า
                int integerFields = toInteger(fields[i]);
                if (integerFields < 0 || integerFields > 7) {
                    System.out.println("Fields -> " + i + " is out of [0,7]");
                    exit(1);
                }

                fields[i] = addZeroBits(toBinaryString(integerFields), 3); // แปลงเป็นบิต
            }

            // ตรวจสอบประเภทของคำสั่งและดำเนินการที่สอดคล้องกัน
            if (type.equals("R")) {
                R_type(fields);
            } else if (type.equals("I")) {
                I_type(fields, index, instruction);
            } else if (type.equals("J")) {
                J_type(fields);
            } else if (type.equals("O")) {
                O_type();
            } else if (type.equals("F")) {
                F_type(index);
            } else {
                System.out.println("No type match");
                exit(1);
            }

            // เพิ่มโค้ดเครื่องที่สร้างเข้าในลิสต์
            machineCodes.add(machineCode);
            pointer++;
            parseLineToData();
        }
        if (TestTool)
            System.out.println("------------------------------------------------------------------------------");
        return new ArrayList<>(machineCodes);
    }

    /**
     * ฟังก์ชันสำหรับการแปลงคำสั่งประเภท R เป็นโค้ดเครื่อง
     *
     * @param fields ฟิลด์ของคำสั่ง (registers และ destination)
     */
    private void R_type(String[] fields) {
        machineCode += fields[0]; // regA
        machineCode += fields[1]; // regB
        machineCode += "0000000000000";
        machineCode += fields[2]; // destReg
    }

    private void I_type(String[] fields, int index, String instruction) {
        machineCode += fields[0]; // regA
        machineCode += fields[1]; // regB

        int offset = 0;
        fields[2] = Data_list.get(index + 3); // รับค่าที่อยู่จาก Data_list

        // ตรวจสอบว่า offset เป็น label หรือไม่
        if (isLabel(fields[2])) {
            // หากเป็น beq, ใช้ PC เพื่อคำนวณ offset
            if (Objects.equals(instruction, "beq")) offset = Label_Mapping.get(fields[2]) - pointer - 1;
            else offset = Label_Mapping.get(fields[2]); // lw & sw

        } else if (isInteger(fields[2])) { // ตรวจสอบว่าเป็นจำนวนเต็มหรือไม่
            offset = toInteger(fields[2]);
        } else {
            System.out.println("Invalid offset!!!");
            exit(1);
        }

        // ตรวจสอบว่า offset อยู่ในช่วงที่ถูกต้องหรือไม่
        if (offset > 32767 || offset < -32768) {
            System.out.println("offset out of range!!!");
            exit(1);
        }

        String offsetBinary;
        if (offset >= 0) {
            offsetBinary = addZeroBits(toBinaryString(offset), 16);
        } else {
            offsetBinary = addZeroBits(toBinaryString(-offset), 16);
            offsetBinary = twosCompliment(offsetBinary);
        }
        if (TestTool) {
            System.out.println("machineCode + offsetBinary : " + (machineCode + offsetBinary));
        }
        machineCode += offsetBinary; // เพิ่มค่า offset ใน machineCode
    }

    private void J_type(String[] fields) {
        machineCode += fields[0]; // regA
        machineCode += fields[1]; // regB
        machineCode += "0000000000000000"; // ไม่มี offset
    }

    private void O_type() { // คำสั่งประเภท O ไม่มีฟิลด์เพิ่มเติม
        machineCode += "0000000000000000000000";
    }

    private void F_type(int index) { // คำสั่งประเภท F ไม่มีฟิลด์เพิ่มเติม
        machineCode = "";
        String value = Data_list.get(1 + index); // รับค่า field ถัดไป

        // ตรวจสอบว่า value เป็นจำนวนเต็มหรือ label
        if (isInteger(value)) { // Integer State
            int integer = toInteger(value);
            String binary;
            if (integer >= 0) {
                binary = toBinaryString(integer);
                machineCode = addZeroBits(binary, 32);
            } else { // หากค่าเป็นลบ ต้องทำ 2's complement
                binary = toBinaryString(-integer);
                binary = addZeroBits(binary, 32);
                machineCode = twosCompliment(binary);
            }
        } else if (isLabel(value)) { // Label State
            int integer = Label_Mapping.get(value);
            String binary = toBinaryString(integer);
            machineCode = addZeroBits(binary, 32);
        } else {
            System.out.println("Invalid .fill!!!");
            exit(1);
        }
    }

    /**
     * เพิ่มเลขศูนย์ให้ field จนมีขนาดเท่ากับ size ที่กำหนด
     *
     * @param field สตริงของบิตที่จะเพิ่มเลขศูนย์
     * @param size  ขนาดที่ต้องการหลังจากเพิ่มเลขศูนย์
     * @return สตริงของบิตที่ถูกเพิ่มเลขศูนย์จนมีขนาดเท่ากับ size
     */
    public static String addZeroBits(String field, int size) {
        StringBuilder result = new StringBuilder();
        while (result.length() + field.length() < size) {
            result.append('0');
        }
        result.append(field);
        return result.toString();
    }

    /**
     * ทำ 2's complement กับสตริงบิต
     *
     * @param binary สตริงบิตที่ต้องการทำ 2's complement
     * @return สตริงบิตที่ได้หลังจากทำ 2's complement
     */
    public static String twosCompliment(String binary) {
        // Invert บิตแต่ละบิต
        StringBuilder inverted = new StringBuilder();
        for (char bit : binary.toCharArray()) {
            inverted.append((bit == '0') ? '1' : '0');
        }

        // บวก 1 เข้าไปในบิตที่ถูก invert
        int carry = 1;
        StringBuilder result = new StringBuilder();
        for (int i = inverted.length() - 1; i >= 0; i--) {
            int bit = Character.getNumericValue(inverted.charAt(i)) + carry;
            if (bit > 1) {
                carry = 1;
                bit = 0;
            } else {
                carry = 0;
            }
            result.insert(0, bit);
        }
        return addZeroBits(result.toString(), binary.length());
    }

    /**
     * แปลงสตริงเป็นจำนวนเต็ม
     *
     * @param field สตริงที่ต้องการแปลง
     * @return จำนวนเต็มที่แปลงจากสตริง
     */
    public static int toInteger(String field) {
        return Integer.parseInt(field);
    }

    /**
     * ฟังก์ชันสำหรับการทำ Label Mapping เพื่อสร้างการเชื่อมโยงระหว่าง label และตำแหน่งคำสั่ง
     */
    public void LabelMapping() {
        while (tkz.hasNext()) {
            if (Data_list.isEmpty()) {
                parseLineToData();
            }
            if (!isInstruction(Data_list.get(0))) {
                if (LabelValidCheck(Data_list.get(0))) {
                    Label_Mapping.put(Data_list.get(0), pointer);
                } else {
                    System.out.println("First index is not Label or instruction");
                    exit(1);
                }
            }
            pointer++;
            parseLineToData();
        }
    }

    /**
     * ตรวจสอบว่าสตริงเป็น label หรือไม่
     *
     * @param tkz สตริงที่ต้องการตรวจสอบ
     * @return true หากเป็น label
     */
    public boolean isLabel(String tkz) {
        return Label_Mapping.containsKey(tkz);
    }

    /**
     * ตรวจสอบว่าสตริงเป็น instruction หรือไม่
     *
     * @param tkz สตริงที่ต้องการตรวจสอบ
     * @return true หากเป็น instruction
     */
    public static boolean isInstruction(String tkz) {
        return getType(tkz) != null;
    }

    /**
     * ตรวจสอบว่าสตริงเป็นจำนวนเต็มหรือไม่
     *
     * @param input สตริงที่ต้องการตรวจสอบ
     * @return true หากเป็นจำนวนเต็ม
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * ตรวจสอบว่า label ที่กำหนดมีความยาวถูกต้องและยังไม่มีการ map อยู่แล้วหรือไม่
     *
     * @param label สตริงของ label ที่ต้องการตรวจสอบ
     * @return true หาก label ถูกต้องและไม่ซ้ำ
     */
    private boolean LabelValidCheck(String label) {
        return !Label_Mapping.containsKey(label) && label.length() <= 6;
    }

    /**
     * แปลงบิตเป็นทศนิยม
     *
     * @param binaryList ลิสต์ของบิตที่ต้องการแปลง
     * @return ลิสต์ของค่าทศนิยมที่ได้จากการแปลง
     */
    public static List<String> binaryToDecimal(List<String> binaryList) {
        List<String> decimalList = new ArrayList<>();

        for (String binaryString : binaryList) {
            String decimal;

            if (binaryString.charAt(0) == '1') {
                StringBuilder invertedString = new StringBuilder();
                for (char bit : binaryString.toCharArray()) {
                    invertedString.append((bit == '0') ? '1' : '0');
                }

                BigInteger absoluteValue = new BigInteger(invertedString.toString(), 2).add(BigInteger.ONE);
                decimal = "-" + absoluteValue.toString();
            } else {
                decimal = new BigInteger(binaryString, 2).toString();
            }

            decimalList.add(decimal);
        }

        return decimalList;
    }

    /**
     * แปลงทศนิยมเป็นบิต
     *
     * @param decimalList ลิสต์ของค่าทศนิยมที่ต้องการแปลง
     * @return ลิสต์ของบิตที่ได้จากการแปลง
     */
    public static List<String> decimalToBinary(List<String> decimalList) {
        List<String> binaryStrings = new ArrayList<>();

        for (String decimal : decimalList) {
            String binary;

            if (decimal.startsWith("-")) {
                String absoluteValue = decimal.substring(1); // ลบเครื่องหมายลบออก
                BigInteger absoluteBigInt = new BigInteger(absoluteValue);
                String binaryString = absoluteBigInt.toString(2);

                // ทำ 2's complement กับค่าที่เป็นลบ
                binary = twosCompliment(addZeroBits(binaryString, 32));
            } else {
                BigInteger positiveBigInt = new BigInteger(decimal);
                binary = positiveBigInt.toString(2);

                // เพิ่มเลขศูนย์ด้วย addZeroBits
                binary = addZeroBits(binary, 32);
            }

            binaryStrings.add(binary);
        }

        return binaryStrings;
    }

    /**
     * แปลงจำนวนเต็มเป็นสตริงของบิต
     *
     * @param value จำนวนเต็มที่ต้องการแปลง
     * @return สตริงบิตที่แปลงจากจำนวนเต็ม
     */
    public static String toBinaryString(int value) {
        return Integer.toBinaryString(value);
    }

    /**
     * ฟังก์ชันสำหรับหยุดโปรแกรมและแสดงข้อผิดพลาด
     *
     * @param i รหัสสถานะ (1 สำหรับข้อผิดพลาด, 0 สำหรับการหยุดแบบปกติ)
     */
    public void exit(int i) {
        System.out.println("exit (" + i + ") : ERROR!!!");
        System.exit(i);
    }
}
