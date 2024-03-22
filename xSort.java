import java.io.*;
import java.util.*;

//need Priority queue 

public class xSort {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java xSort <lines> <initial_runs_file> <merged runs>");
            System.exit(1);
        }

        try {
            int m = Integer.parseInt(args[0]); // Number of lines in each initial run
            String initialRunsFile = args[1]; // File with initial runs
            int k = Integer.parseInt(args[2]); // Number of runs merged on each pass
            distributeRuns(initialRunsFile, m, k);
            mergeRuns(initialRunsFile, m, k);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void distributeRuns(String initialRunsFile, int m, int k) throws IOException{
        // Read initial runs from inputFile and distribute them into k files
        // Each file will contain a subset of the initial runs

        int NUM_FILES = k;
        int LINES_PER_FILE = m;
        String INPUT_FILE = initialRunsFile;
        String[] TEMP_FILES = new String[NUM_FILES];

        // Initialize temp file names
        for (int i = 0; i < NUM_FILES; i++) {
            TEMP_FILES[i] = "temp" + (i + 1) + ".txt";
        }

        FileReader inputFileReader = null;

        try {
            // Open input file
            inputFileReader = new FileReader(INPUT_FILE);
            int linesWritten = 0;
            int currentFileIndex = 0;
            boolean inputEnded = false;

            while (!inputEnded) {
                //Use FileWriter as BufferedWriter doesnt work 
                try (FileWriter tempWriter = new FileWriter(TEMP_FILES[currentFileIndex], true)) {
                    int character;
                    // Loop to write lines to the temp file
                    while ((character = inputFileReader.read()) != -1) {
                        tempWriter.write(character);
                        if (character == '\n') {
                            linesWritten++;
                            if (linesWritten == LINES_PER_FILE) {
                                linesWritten = 0;
                                currentFileIndex = (currentFileIndex + 1) % NUM_FILES;
                                break; // Move to the next temp file
                            }
                        }
                    }
                    // If reached end of input file, reset file pointer
                    if (character == -1) {
                        inputEnded = true;
                        inputFileReader.close();
                        inputFileReader = new FileReader(INPUT_FILE);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the input file reader if it's open
            if (inputFileReader != null) {
                try {
                    inputFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void mergeRuns(String initialRunsFile, int m, int k){
        // Perform k-way sort merge iteratively until one final sorted run is produced
        // Use a priority queue for merging the runs
        // Print the final sorted data to the standard output
    }
}
