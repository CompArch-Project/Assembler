import java.util.List;
import java.util.Scanner;

public class Main {

    static String Input_path = "input/";
    static String Output_path = "output/";

    /**
     * outExtension[0] for file.bin
     * outExtension[1] for file.txt
     * outExtension[2] for file.hex
     */
    static String[] outExtension = {
            ".binary", ".decimal" ,".hex"
    };

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Ask for the file name without .txt
        System.out.print("Enter the file name (without .txt, e.g., input1): ");
        String fileName = input.nextLine();

        // Construct the full path
        String filePath = Input_path + fileName + ".txt"; // Automatically add .txt

        // Print the assembly file content
        printAssembly(filePath);

        System.out.print("Press Enter to compute to machine code: ");
        input.nextLine();

        // Main function to compute machine code
        compute(filePath, fileName);

        // Show output, starting with Hex first
        print(fileName, outExtension[2]);  // Print hex first
        print(fileName, outExtension[0]);  // Print binary next
        print(fileName, outExtension[1]);  // Print decimal last
    }

    // Compute function to handle conversion of assembly code to machine code
    public static void compute(String filePath, String fileName) {
        Assembler encoder = new Assembler(
                FileOperator.FileToString(filePath)
        );

        // Generate binary, decimal, and hex codes
        List<String> binaryCodes = encoder.computeToMachineCode();
        List<String> decimalCodes = Assembler.binaryToDecimal(binaryCodes);
        List<String> hexCodes = Assembler.binaryToHex(binaryCodes);  // Convert binary to hexadecimal

        // Save output files based on the input file name
        String baseFileName = fileName;  // No need to extract file name, since it's already without .txt

        // Save files with new names in the output folder
        FileOperator.StringToFile(Output_path + baseFileName + outExtension[0], binaryCodes);  // Save binary
        FileOperator.StringToFile(Output_path + baseFileName + outExtension[1], decimalCodes); // Save decimal
        FileOperator.StringToFile(Output_path + baseFileName + outExtension[2], hexCodes);     // Save hex with 0x prefix
    }

    // Print function to display the output content
    public static void print(String fileName, String fileExtension) {
        String binaryFilePath = Output_path + fileName + outExtension[0];
        String decimalFilePath = Output_path + fileName + outExtension[1];
        String hexFilePath = Output_path + fileName + outExtension[2];

        System.out.println("\n=== Output for File: " + fileName + " ===\n");

        // First, print HexCode
        if (fileExtension.equals(outExtension[2])) {
            System.out.println(">>> Hexadecimal Representation <<<\n");
            System.out.println(FileOperator.FileToString(hexFilePath));
            System.out.println("\n===============================\n");
        }

        // Then, print BinaryCode
        if (fileExtension.equals(outExtension[0])) {
            System.out.println(">>> Binary Representation <<<\n");
            System.out.println(FileOperator.FileToString(binaryFilePath));
            System.out.println("\n===============================\n");
        }

        // Finally, print DecimalCode
        if (fileExtension.equals(outExtension[1])) {
            System.out.println(">>> Decimal Representation <<<\n");
            System.out.println(FileOperator.FileToString(decimalFilePath));
            System.out.println("\n===============================\n");
        }
    }

    // Function to print the assembly file content
    public static void printAssembly(String filePath) {
        System.out.println("\n=== Assembly Code: " + filePath + " ===\n");
        System.out.println(FileOperator.FileToString(filePath));
        System.out.println("\n======= End of Assembly Code =======\n");
    }

}
