import java.util.List;
import java.util.Scanner;

public class Main {

    static String Input_path = "input/";
    static String Output_path = "output/";

    /**
     * outExtension[0] for file.bin
     * outExtension[1] for file.txt
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

        // Show output
        print(fileName, outExtension[0]);
        print(fileName, outExtension[1]);
    }

    public static void compute(String filePath) {
        Assembler encoder = new Assembler(
                FileOperator.FileToString(filePath)
        );

        List<String> binaryCodes = encoder.computeToMachineCode();
        List<String> decimalCodes = Assembler.binaryToDecimal(binaryCodes);

        // Save output files based on the input file name
        String baseFileName = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.lastIndexOf('.'));

        // Save files with new names
      // FileOperator.StringToFile(Output_path + baseFileName + "binary" + outExtension[0], binaryCodes);
      // FileOperator.StringToFile(Output_path + baseFileName + "decimal" + outExtension[1], decimalCodes);
        FileOperator.StringToFile(Output_path + baseFileName  + outExtension[0], binaryCodes);
        FileOperator.StringToFile(Output_path + baseFileName + outExtension[1], decimalCodes);


    }

    public static void print(String fileName, String fileExtension) {
        // Construct the output file names based on the specified requirements
      //  String binaryFilePath = Output_path + fileName.substring(0, fileName.lastIndexOf('.')) + "binary" + outExtension[0];
        //String decimalFilePath = Output_path + fileName.substring(0, fileName.lastIndexOf('.')) + "decimal" + outExtension[1];
        String binaryFilePath = Output_path + fileName.substring(0, fileName.lastIndexOf('.')) + outExtension[0];
        String decimalFilePath = Output_path + fileName.substring(0, fileName.lastIndexOf('.')) + outExtension[1];

        System.out.println("\n");

        if (fileExtension.equals(outExtension[1])) {
            System.out.println("Print in DecimalCode -> " + decimalFilePath);
            String content = FileOperator.FileToString(decimalFilePath);
            if (!content.trim().isEmpty()) {
                System.out.println(content);
            } else {
                System.out.println("The decimal file is empty or not found.");
            }
        } else if (fileExtension.equals(outExtension[0])) {
            System.out.println("Print in BinaryCode -> " + binaryFilePath);
            String content = FileOperator.FileToString(binaryFilePath);
            if (!content.trim().isEmpty()) {
                System.out.println(content);
            } else {
                System.out.println("The binary file is empty or not found.");
            }
        }
    }



    public static void printAssembly(String filePath) {
        System.out.println("\n");
        System.out.println("Print: " + filePath + "\n");
        System.out.println(" " + FileOperator.FileToString(filePath));
    }
}
