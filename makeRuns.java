//Students:
//William Malone - 1604564

import java.io.BufferedReader;
import java.io.FileReader;

public class makeRuns {

    //This class is for:
    //1. Sorting a large dataset into chunks 
    //2. Sorting each chunk via Quickksort
    //3. Output the sorted runs to files 

    public static void main(String[] args) {
        //Check Parameters
        if (args.length != 2) {
            System.err.println("Incorrect Parameters - Make sure you follow: java makeRuns m filename");
        }

        try {
            //The first parameter <m> is the number of lines that go into each run
            int m = Integer.parseInt(args[0]);
            //The second parameter <filename> is the filename
            String filename = args[1]; 
            //only for 16 <= m <= 32768
            if (m < 16 || m > 32768) {
                System.err.println("Incorrect Parameters: m = 16 to 32768");
            }
            else{
                System.out.println(m);
                System.out.println(filename);
            }

            //Read the contents of filename into a buffered reader 
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            //Make an array to store m lines from file
            //Each element can hold one line 
            String[] lines = new String[m];

            //Need to :
            //Process the input file in chunks of size m

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("Done");
    }
}
