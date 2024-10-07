import java.util.List;
import java.util.Scanner;

public class Main {

    static String Input_path = "input/";
    static String Output_path = "output/";

    /**
     * outExtension[0] for file.bin
     * outExtension[1] for file.txt
     * * outExtension[2] for file.hex
     */
    static String[] outExtension = {
            ".binary", ".decimal" ,".hex"
    };

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Ask for the file name
        System.out.print("Enter the file name (e.g., input1.txt): ");
        String fileName = input.nextLine();

        // Construct the full path
        String filePath = Input_path + fileName;

        printAssembly(filePath);

        System.out.print("Press Enter to compute to machine code: ");
        input.nextLine();

        // Main function
        compute(filePath);

        // Show output, starting with Hex first
        print(fileName, outExtension[2]);  // Print hex first
        print(fileName, outExtension[0]);  // Print binary next
        print(fileName, outExtension[1]);  // Print decimal last
    }


    public static void compute(String filePath) {
        Assembler encoder = new Assembler(
                FileOperator.FileToString(filePath)
        );

        List<String> binaryCodes = encoder.computeToMachineCode();
        List<String> decimalCodes = Assembler.binaryToDecimal(binaryCodes);
        List<String> hexCodes = Assembler.binaryToHex(binaryCodes);  // Convert binary to hexadecimal

        // Save output files based on the input file name
        String baseFileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.lastIndexOf('.'));

        // Save files with new names
        FileOperator.StringToFile(Output_path + baseFileName + outExtension[0], binaryCodes);  // Save binary
        FileOperator.StringToFile(Output_path + baseFileName + outExtension[1], decimalCodes); // Save decimal
        FileOperator.StringToFile(Output_path + baseFileName + outExtension[2], hexCodes);     // Save hex with 0x prefix
    }



    public static void print(String fileName, String fileExtension) {
        String binaryFilePath = Output_path + fileName.substring(0, fileName.lastIndexOf('.')) + outExtension[0];
        String decimalFilePath = Output_path + fileName.substring(0, fileName.lastIndexOf('.')) + outExtension[1];
        String hexFilePath = Output_path + fileName.substring(0, fileName.lastIndexOf('.')) + outExtension[2]; // Hex file path

        System.out.println("\n");

        // First, print HexCode
        if (fileExtension.equals(outExtension[2])) {
            System.out.println("Print in HexCode -> " + hexFilePath);
            String content = FileOperator.FileToString(hexFilePath);
            if (!content.trim().isEmpty()) {
                System.out.println(content);
            } else {
                System.out.println("The hex file is empty or not found.");
            }
        }

        // Then, print BinaryCode
        if (fileExtension.equals(outExtension[0])) {
            System.out.println("Print in BinaryCode -> " + binaryFilePath);
            String content = FileOperator.FileToString(binaryFilePath);
            if (!content.trim().isEmpty()) {
                System.out.println(content);
            } else {
                System.out.println("The binary file is empty or not found.");
            }
        }

        // Finally, print DecimalCode
        if (fileExtension.equals(outExtension[1])) {
            System.out.println("Print in DecimalCode -> " + decimalFilePath);
            String content = FileOperator.FileToString(decimalFilePath);
            if (!content.trim().isEmpty()) {
                System.out.println(content);
            } else {
                System.out.println("The decimal file is empty or not found.");
            }
        }
    }





    public static void printAssembly(String filePath) {
        System.out.println("\n");
        System.out.println("Print: " + filePath + "\n");
        System.out.println(" " + FileOperator.FileToString(filePath));
    }

}
