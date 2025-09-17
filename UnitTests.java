import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UnitTests {

    public static void main(String[] args) {
        System.out.println("Running unit tests...\n");
        boolean allPassed = true;
        allPassed &= testEmployee();
        allPassed &= testReporter();
        allPassed &= testCreator();
        System.out.println("\n" + (allPassed ? "All tests passed!" : "Some tests failed."));
    }

    public static boolean testEmployee() {
        System.out.println("Testing Employee...");
        try {
            Employee emp = new Employee(1, "John", 40.5);
            assert emp.getNum() == 1 : "Num getter failed";
            assert emp.getName().equals("John") : "Name getter failed";
            assert emp.getHours() == 40.5 : "Hours getter failed";
            assert emp.toString().equals("Num: 1, Name: John, Hours: 40.5") : "toString failed";
            System.out.println("Employee tests passed.");
            return true;
        } catch (AssertionError e) {
            System.out.println("Employee test failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean testReporter() {
        System.out.println("Testing Reporter...");
        String binaryFile = "test_bin";
        String reportFile = "test_report.txt";
        try {
            try (RandomAccessFile raf = new RandomAccessFile(binaryFile, "rw")) {
                raf.writeInt(1);
                byte[] nameBytes = "John".getBytes(StandardCharsets.US_ASCII);
                byte[] paddedName = new byte[10];
                System.arraycopy(nameBytes, 0, paddedName, 0, nameBytes.length);
                raf.write(paddedName);
                raf.writeDouble(40.5);

                raf.writeInt(2);
                nameBytes = "Jane".getBytes(StandardCharsets.US_ASCII);
                paddedName = new byte[10];
                System.arraycopy(nameBytes, 0, paddedName, 0, nameBytes.length);
                raf.write(paddedName);
                raf.writeDouble(35.0);
            }

            String[] args = {binaryFile, reportFile};
            Reporter.main(args);

            List<String> lines = Files.readAllLines(Paths.get(reportFile));
            assert lines.size() == 3 : "Report should have 3 lines";
            assert lines.get(0).equals("Employee Report:") : "First line incorrect";
            assert lines.get(1).equals("Num: 1, Name: John, Hours: 40.5") : "Second line incorrect";
            assert lines.get(2).equals("Num: 2, Name: Jane, Hours: 35.0") : "Third line incorrect";

            Files.deleteIfExists(Paths.get(binaryFile));
            Files.deleteIfExists(Paths.get(reportFile));
            System.out.println("Reporter tests passed.");
            return true;
        } catch (Exception e) {
            System.out.println("Reporter test failed: " + e.getMessage());
            try {
                Files.deleteIfExists(Paths.get(binaryFile));
                Files.deleteIfExists(Paths.get(reportFile));
            } catch (IOException ignored) {}
            return false;
        }
    }

    public static boolean testCreator() {
        System.out.println("Testing Creator...");
        String filename = "test_creator_bin";
        try {
            String simulatedInput = "1\nJohn\n40,5\n";
            ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
            System.setIn(in);

            String[] args = {filename, "1"};
            Creator.main(args);

            try (RandomAccessFile raf = new RandomAccessFile(filename, "r")) {
                assert raf.readInt() == 1 : "Num not written correctly";
                byte[] nameBytes = new byte[10];
                raf.read(nameBytes);
                String name = new String(nameBytes, StandardCharsets.US_ASCII).trim();
                assert name.equals("John") : "Name not written correctly";
                assert raf.readDouble() == 40.5 : "Hours not written correctly";
            }

            Files.deleteIfExists(Paths.get(filename));
            System.out.println("Creator tests passed.");
            return true;
        } catch (Exception e) {
            System.out.println("Creator test failed: " + e.getMessage());
            try {
                Files.deleteIfExists(Paths.get(filename));
            } catch (IOException ignored) {}
            return false;
        } finally {
            System.setIn(System.in);
        }
    }
}
