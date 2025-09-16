import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Reporter {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Reporter <binary_file> <report_file>");
            System.out.println("Example: java Reporter bin1 report.txt");
            System.out.flush();
            return;
        }
        String binaryFile = args[0];
        String reportFile = args[1];

        List<Employee> employees = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(binaryFile, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int num = raf.readInt();
                byte[] nameBytes = new byte[10];
                raf.read(nameBytes);
                String name = new String(nameBytes, StandardCharsets.US_ASCII).trim();
                double hours = raf.readDouble();
                employees.add(new Employee(num, name, hours));
            }
        } catch (IOException e) {
            System.err.println("Error reading binary file: " + e.getMessage());
            System.out.flush();
            e.printStackTrace();
            return;
        }

        try (PrintWriter writer = new PrintWriter(reportFile, StandardCharsets.UTF_8)) {
            writer.println("Employee Report:");
            for (Employee emp : employees) {
                writer.println(emp.toString());
            }
            System.out.println("Text report created: " + reportFile);
            System.out.flush();
        } catch (IOException e) {
            System.err.println("Error creating report: " + e.getMessage());
            System.out.flush();
            e.printStackTrace();
        }
        System.out.println("Reporter completed successfully.");
        System.out.flush();
    }
}
