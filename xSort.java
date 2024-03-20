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
        // Use appropriate data structures and algorithms to distribute the runs
        // You can use the buffered reader to read the input file line by line

        BufferedReader reader = new BufferedReader(new FileReader(initialRunsFile));
        BufferedWriter[] writers = new BufferedWriter[k];
        String[] lines = new String[m];
        int count = 0;
        int fileIndex = 0;

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines[count++] = line;
                } else {    
                    // Sort the lines in the run 
                    Arrays.sort(lines, 0, count);
                    // Distribute the sorted lines into k files
                    if (writers[fileIndex % k] == null) {
                        //Create the tempoary file on your disk instead of in memory
                        //Cycling, round-robin fashion through BufferedWriter objects
                        writers[fileIndex % k] = new BufferedWriter(new FileWriter("K_" + fileIndex + ".txt"));
                        System.out.println("k " + fileIndex + 1);   
                    }
                    BufferedWriter writer = writers[fileIndex % k];
                    for (int i = 0; i < count; i++) {
                        //



                        //This
                        
                        writers[fileIndex % k].write(lines[i]);
                        System.out.println(lines[i]); 
                        writers[fileIndex % k].newLine();

                        //
                    }
                    writer.flush();
                    count = 0;
                    fileIndex++;
                    
                }
            }
        } finally {
            reader.close();
            for (BufferedWriter writer : writers) {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    public static void mergeRuns(String initialRunsFile, int m, int k){
        // Perform k-way sort merge iteratively until one final sorted run is produced
        // Use a priority queue or another suitable data structure for merging the runs
        // You may need to create temporary files for intermediate results
        // Print the final sorted data to the standard output
    }
}
