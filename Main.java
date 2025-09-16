import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting the program. Enter binary file name (example: bin1): ");
        System.out.flush();
        String filename = scanner.nextLine();
        System.out.println("Enter number of records (integer, example: 2): ");
        System.out.flush();
        int count = scanner.nextInt();
        scanner.nextLine();

        String classpath = System.getProperty("java.class.path");

        try {
            ProcessBuilder builder = new ProcessBuilder("java", "-cp", classpath, "Creator", filename, String.valueOf(count));
            builder.inheritIO();
            Process process = builder.start();
            System.out.println("Creator started. Enter data in the console above.");
            System.out.flush();
            int exitCode = process.waitFor();
            System.out.println("Creator finished with code: " + exitCode);
            System.out.flush();
            if (exitCode != 0) {
                System.out.println("Error in Creator. Check input.");
                System.out.flush();
                return;
            }

            String reportFile = filename + "_report.txt";
            ProcessBuilder builder2 = new ProcessBuilder("java", "-cp", classpath, "Reporter", filename, reportFile);
            builder2.inheritIO();
            Process process2 = builder2.start();
            System.out.println("Reporter started. Generating report...");
            System.out.flush();
            int exitCode2 = process2.waitFor();
            System.out.println("Reporter finished with code: " + exitCode2);
            System.out.flush();
            if (exitCode2 == 0) {
                System.out.println("Program completed. Report: " + reportFile);
                System.out.flush();
            } else {
                System.out.println("Error in Reporter.");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error launching processes: " + e.getMessage());
            System.out.flush();
            e.printStackTrace();
        }
    }
}
