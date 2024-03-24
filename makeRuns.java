//Students:
//William Malone - 1604564
//Justin Poutoa  - 1620107
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class makeRuns {
    public static void main(String[] args) {
        //Ensure there are only two parameters
        if (args.length != 2) {
            System.err.println("Error: Incorrect Parameters");
            System.exit(1);
        }
        //Add a try-catch for error checking
        try {
            //Store the parameters into appropriate variables
            //Size of the initial runs
            int m = Integer.parseInt(args[0]);
            //Name of the file
            String inputFile = args[1];
            //Ensure m is between 16 and 32768 (both inclusive)
            if (m < 16 || m > 32768) {
                //If it is out of bounds, display an error
                System.err.println("Incorrect Parameters: m must be between 16 and 32768");
                System.exit(1);
            } else {
                //If it is all good, create runs
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
        //Declare variables
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String[] lines = new String[m];
        int count = 0;
        //Add try-catch for error checking
        try {
            //Declare a variable to store a line from the file when it is read
            String line;
            //Loop through the entire file
            while ((line = reader.readLine()) != null) {
                //Move to the next line
                lines[count++] = line;
                if (count == m) {
                    //Sort the lines in the array 
                    Arrays.sort(lines);
                    //Iterate through the sorted lines and print out each one
                    for (String sortedLine : lines) {
                        System.out.println(sortedLine);
                    }
                    //Reset counter
                    count = 0;
                }
            }
            //If count is greater than zero, meaning there are remaining lines which haven't been sorted
            if (count > 0) {
                //Sort the lines
                Arrays.sort(lines, 0, count);
                //Iterate through the sorted lines and print each line
                for (int i = 0; i < count; i++) {
                    System.out.println(lines[i]);
                }
            }
        } finally {
            reader.close();
        }
    }
}
