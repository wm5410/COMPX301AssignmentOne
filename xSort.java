import java.io.*;
import java.util.*;
public class xSort {
    public static void main(String[] args) {
        //Ensure there are only three parameters
        if (args.length != 3) {
            System.err.println("Usage: java xSort <lines> <initial_runs_file> <merged runs>");
            System.exit(1);
        }
        //If k is not 2, exit
        if(Integer.parseInt(args[2]) != 2){
            System.out.println("This program is only a 2 way merge.");
            System.exit(0);
        }
        //Add a try-catch for error checking
        try {
            int m = Integer.parseInt(args[0]); // Number of lines in each initial run
            String initialRunsFile = args[1]; // File with initial runs
            int k = Integer.parseInt(args[2]); // Number of runs merged on each pass
            distributeRuns(initialRunsFile, m, k);
            String[] tmpFiles = getTmpFiles();
            mergeRuns(tmpFiles,"output.txt");
            //Delete temp files after merging

        } catch (Exception e) {
            //Dysplay the error in the output
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void distributeRuns(String initialRunsFile, int m, int k) throws IOException{
        // Read initial runs from inputFile and distribute them into 2 files
        //Declare variables
        int NUM_FILES = k;
        int LINES_PER_FILE = m;
        String INPUT_FILE = initialRunsFile;
        String[] TEMP_FILES = new String[NUM_FILES];
        // Initialize temp file names, should create 4 files
        for (int i = 0; i <= NUM_FILES*2; i++) {
            TEMP_FILES[i] = "k" + (i + 1) + ".tmp";    //This is how the files will be named
        }
        

        FileReader inputFileReader = null;

        try {
            // Open input file for reading
            inputFileReader = new FileReader(INPUT_FILE);
            int linesWritten = 0;         //Counter for lines written to each temporary file
            int currentFileIndex = 0;     //Index of the current temporary file being written to
            boolean inputEnded = false;   //Flag to indicate if end of the file is reached
            //Loop until end of file is reached
            while (!inputEnded) {
                //Use FileWriter as BufferedWriter doesnt work 
                try (FileWriter tempWriter = new FileWriter(TEMP_FILES[currentFileIndex], true)) {
                    int character;
                    // Loop to write lines to the current temp file
                    while ((character = inputFileReader.read()) != -1) {
                        //Write character to the temp file
                        tempWriter.write(character);
                        if (character == '\n') {
                            //Increment line counter
                            linesWritten++;
                            //If all the lines in a single file have been read
                            if (linesWritten == LINES_PER_FILE) {
                                //Reset counter
                                linesWritten = 0;
                                //Move to the next temp file
                                currentFileIndex = (currentFileIndex + 1) % NUM_FILES;
                                break; 
                            }
                        }
                    }
                    // If reached end of input file, reset file pointer
                    if (character == -1) {
                        inputEnded = true;
                        inputFileReader.close();
                        //Re-open input file for reading
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
        //Initialised a bufferedWriter to write the data in the outputFile
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))){
            // Initialize a priority queue to manage merging of runs
            //PriorityQueue<RunEntry> pq = new PriorityQueue<>(Comparator.comparingInt(o -> o.value));
            
            // Initialize a buffered reader for each temporary file
            BufferedReader[] readers = new BufferedReader[tempFiles.length];
            for (int i = 0; i < tempFiles.length; i++) {
                readers[i] = new BufferedReader(new FileReader(tempFiles[i]));
                // Read the first line of each file
                String line = readers[i].readLine();
                if (line != null) {
                    int value = Integer.parseInt(line);
                    //pq.offer(new RunEntry(value, i));
                }
            }

            // Merge runs until the priority queue is empty
            while (!pq.isEmpty()) {
                // Get the smallest value from the priority queue
                RunEntry entry = pq.poll();
                // Write the value to the output file
                writer.write(String.valueOf(entry.value));
                writer.newLine();

                // Read the next line from the corresponding file and add it to the priority queue
                String line = readers[entry.fileIndex].readLine();
                if (line != null) {
                    int value = Integer.parseInt(line);
                    pq.offer(new RunEntry(value, entry.fileIndex));
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // Nested class to represent an entry in the priority queue
    static class RunEntry {
        int value; // Value of the entry
        int fileIndex; // Index of the file containing the entry

        public RunEntry(int value, int fileIndex) {
            this.value = value;
            this.fileIndex = fileIndex;
        }
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
                //List<Integer> firstTape = tapes.get(i);
                //List<Integer> secondTape = (i + 1 < tapes.size()) ? tapes.get(i + 1) : new ArrayList<>();
                //OVER HERE - merge two tapes into one
                //newTapes.add(merge(firstTape, secondTape));
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