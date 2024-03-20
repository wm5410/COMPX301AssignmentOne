//Students:
//William Malone - 1604564
//Justin Poutoa  - 1620107
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MakeRunsTest {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Error: Incorrect Parameters");
            System.exit(1);
        }

        try {
            int m = Integer.parseInt(args[0]);
            String inputFile = args[1];
            if (m < 16 || m > 32768) {
                System.err.println("Incorrect Parameters: m must be between 16 and 32768");
                System.exit(1);
            } else {
                createRuns(m, inputFile);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid value for m");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void createRuns(int m, String inputFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String[] lines = new String[m];
        int count = 0;

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lines[count++] = line;
                if (count == m) {
                    //Assuming .sort can be used 
                    Arrays.sort(lines);
                    for (String sortedLine : lines) {
                        System.out.println(sortedLine);
                    }
                    count = 0;
                }
            }
            if (count > 0) {
                Arrays.sort(lines, 0, count);
                for (int i = 0; i < count; i++) {
                    System.out.println(lines[i]);
                }
            }
        } finally {
            reader.close();
        }
    }
}
