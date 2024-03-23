import java.io.*;
import java.util.*;
public class xSort {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java xSort <lines> <initial_runs_file> <merged runs>");
            System.exit(1);
        }

        try {
            int m = Integer.parseInt(args[0]); // Number of lines in each initial run
            //String initialRunsFile = args[1]; // File with initial runs
            int k = Integer.parseInt(args[2]); // Number of runs merged on each pass
            //distributeRuns(initialRunsFile, m, k);
            String[] tmpFiles = getTmpFiles();
            mergeRuns(tmpFiles,"output.txt");


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
            TEMP_FILES[i] = "k" + (i + 1) + ".tmp";
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

    public static String[] getTmpFiles(){
        // Directory where temporary files are located
        String directory = System.getProperty("user.dir");
    
        // Get list of files in the directory
        File[] files = new File(directory).listFiles();
            
        // Filter out only the files with the ".tmp" extension
        List<String> tempFileList = new ArrayList<>();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".tmp")) {
                tempFileList.add(file.getAbsolutePath());
            }
        }
        // Convert the list to an array
        String[] tempFiles = tempFileList.toArray(new String[0]);
        return tempFiles;
    }

    public static void mergeRuns(String[] tempFiles, String outputFile){
        // Perform k-way sort merge iteratively until one final sorted run is produced
        // Use a priority queue for merging the runs
        // Print the final sorted data to the standard output

        
    }

    /**
     * Performs balanced k way merge sort on the initial runs
     * @param initialRuns list of initial runs
     * @param numTapes number of tapes (runs sorted on each pass)
     * @return 
     */
    public static List<Integer> balancedMergeSort(List<List<Integer>> initialRuns, int numTapes){
        //Initialise tapes to hold the initial runs
        List<List<Integer>> tapes = new ArrayList<>();
        for(int i = 0; i < numTapes; i++){
            tapes.add(new ArrayList<>());
        }
        //Distribute initial runs among tapes
        for(List<Integer> run : initialRuns){
            tapes.get(0).addAll(run);
        }
        //Sort each tape individually
        for(List<Integer> tape : tapes){
            Collections.sort(tape);
        }
        //Merge pairs of tapes until only one tape remains
        while(tapes.size() > 1){
            List<List<Integer>> newTapes = new ArrayList<>();
            for(int i = 0; i < tapes.size(); i+=2){
                List<Integer> firstTape = tapes.get(i);
                List<Integer> secondTape = (i + 1 < tapes.size()) ? tapes.get(i + 1) : new ArrayList<>();
                //OVER HERE - merge two tapes into one
                newTapes.add(merge(firstTape, secondTape));
            }
            tapes = newTapes;
        }
        return tapes.get(0);
    }

    /**
     * Merges two sorted tapes/lists into a single sorted list
     * @param firstTape first sorted list
     * @param secondTape second sorted list
     * @return
     */
    public static List<Integer> merge(List<Integer> firstTape, List<Integer> secondTape){
        //Create a new array list to store tapes which were merged
        List<Integer> mergedData = new ArrayList<>();
        //Declare variables to zero (to get first element in the array)
        int i = 0, j = 0;
        //Iterate through both tapes and compare
        while (i < firstTape.size() && j < secondTape.size()) {
            //If the first run in the firstTape is less than the first run in the secondTape
            if (firstTape.get(i) < secondTape.get(j)) {
                //Add the run in the secondTape to mergedData and move to the next run
                mergedData.add(secondTape.get(i++));
            } else {
                //Add the run in the firstTape to mergedData and move to the next run
                mergedData.add(secondTape.get(j++));
            }
        }
        //Add remaining runs in the firstTape to mergedData
        while (i < firstTape.size()) {
            mergedData.add(firstTape.get(i++));
        }
        //Add remaining runs in the secondTape to mergedData
        while (j < secondTape.size()) {
            mergedData.add(secondTape.get(j++));
        }
        return mergedData;
    }
}