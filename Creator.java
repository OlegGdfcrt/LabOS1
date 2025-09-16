import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Creator {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Creator <filename> <count>");
            System.out.println("Example: java Creator bin1 2");
            System.out.flush();
            return;
        }
        String filename = args[0];
        int count;
        try {
            count = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Error: <count> must be an integer.");
            System.out.println("Example: java Creator bin1 2");
            System.out.flush();
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(filename, "rw");
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Creating binary file: " + filename + " with " + count + " records.");
            System.out.println("For each record, enter data in the specified order:");
            System.out.flush();
            for (int i = 0; i < count; i++) {
                try {
                    System.out.println("\nRecord " + (i + 1) + ":");
                    System.out.print("Enter num (integer, example: 1): ");
                    System.out.flush();
                    int num = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter name (string up to 10 chars, example: John): ");
                    System.out.flush();
                    String name = scanner.nextLine();
                    System.out.print("Enter hours (double, example: 40.5): ");
                    System.out.flush();
                    double hours = scanner.nextDouble();
                    scanner.nextLine();

                    raf.writeInt(num);
                    byte[] nameBytes = name.getBytes(StandardCharsets.US_ASCII);
                    byte[] paddedName = new byte[10];
                    System.arraycopy(nameBytes, 0, paddedName, 0, Math.min(10, nameBytes.length));
                    raf.write(paddedName);
                    raf.writeDouble(hours);
                    System.out.println("Record " + (i + 1) + " saved.");
                    System.out.flush();
                } catch (InputMismatchException e) {
                    System.out.println("Input error: enter data of the correct type (num: integer, name: string, hours: double).");
                    System.out.println("Please retry input for this record.");
                    System.out.flush();
                    scanner.nextLine();
                    i--;
                }
            }
            System.out.println("\nBinary file created: " + filename);
            System.out.flush();
        } catch (IOException e) {
            System.err.println("Error creating binary file: " + e.getMessage());
            System.out.flush();
            e.printStackTrace();
        }
        System.out.println("Creator completed successfully.");
        System.out.flush();
    }
}
