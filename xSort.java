import java.io.*;
import java.util.*;
public class xSort {
    public static void main(String[] args) {
        //Ensure there are only three parameters
        if (args.length != 3) {
            System.err.println("Usage: java xSort <lines> <initial_runs_file> <merged runs>");
            System.exit(1);
        }
        //Add a try-catch for error checking
        try {
            // Number of lines in each initial run
            int m = Integer.parseInt(args[0]);
            // File with initial runs
            String initialRunsFile = args[1]; 
            // Number of runs merged on each pass
            int k = Integer.parseInt(args[2]);
            //Call method to distribute the runs into <k> files
            distributeRuns(initialRunsFile, m, k);
            //Call method to merge the runs
            String[] tmpFiles = getTmpFiles();
            mergeRuns(tmpFiles,"output.txt");
        } catch (Exception e) {
            //Dysplay the error in the output
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void distributeRuns(String initialRunsFile, int m, int k) throws IOException{
        // Read initial runs from inputFile and distribute them into k files
        // Each file will contain a subset of the initial runs

        //Declare variables
        int NUM_FILES = k;
        int LINES_PER_FILE = m;
        String INPUT_FILE = initialRunsFile;
        String[] TEMP_FILES = new String[NUM_FILES];
        // Initialize temp file names
        for (int i = 0; i < NUM_FILES; i++) {
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

    public static void mergeRuns(String[] tempFiles, String outputFile) throws IOException{
        // Perform k-way sort merge iteratively until one final sorted run is produced
        // Use a priority queue for merging the runs
        // Print the final sorted data to the standard output

        //Priority queue for merging runs
        PriorityQueue<BufferedReader> pq = new PriorityQueue<>(Comparator.comparingInt(br ->{
            try{
                //Parse the first line of each buffered reader
                return Integer.parseInt(br.readLine());
            }catch(IOException e){
                e.printStackTrace();
                return Integer.MAX_VALUE;   //Incase of error, treat as maximum value
            }
        }));

        //Open readers for each temporary file 
        for(String file : tempFiles){
            BufferedReader br = new BufferedReader(new FileReader(file));
            //Add reader to the priority queue
            pq.offer(br);
        }

        try(PrintWriter printWriter = new PrintWriter(new FileWriter(outputFile))){
            //Merge runs until priority queue is empty
            while(!pq.isEmpty()){
                //Get the smallest reader
                BufferedReader bReader = pq.poll();
                //Read a line from the reader
                String line = bReader.readLine();
                if(line != null){
                    //Write the line to the output file
                    printWriter.println(line);
                    //Add the reader BACK to the priority queue if it still has lines
                    pq.offer(bReader);
                }
                bReader.close();
            }
        }
    }
}