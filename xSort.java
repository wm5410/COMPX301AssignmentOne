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
            int m = Integer.parseInt(args[0]); // Number of lines in each initial run
            String initialRunsFile = args[1]; // File with initial runs
            int k = Integer.parseInt(args[2]); // Number of runs merged on each pass
            distributeRuns(initialRunsFile, m, k);
            File[] tmpFiles = getTmpFiles();
            mergeRuns(tmpFiles,"output.txt");
        } catch (Exception e) {
            //Dysplay the error in the output
            System.out.println("Error: " + e.getMessage());
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





    public static File[] getTmpFiles() {
        // Directory where temporary files are located
        String directory = System.getProperty("user.dir");
    
        // Get list of files in the directory
        File[] files = new File(directory).listFiles();
    
        // Filter out only the files with the ".tmp" extension
        List<File> tempFileList = new ArrayList<>();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".tmp")) {
                tempFileList.add(file);
            }
        }
        // Convert the list to an array
        File[] tempFiles = tempFileList.toArray(new File[tempFileList.size()]);
        return tempFiles;
    }





    public static void mergeRuns(File[] tempFiles, String outputFile) throws IOException{
        // Perform k-way sort merge iteratively until one final sorted run is produced
        // Use a priority queue for merging the runs
        // Print the final sorted data to the standard output

            boolean isMergeFinished = false;
            boolean isMergeStart = true;
            int runSize = 128;
            int numOfRunsToMerge = 2;

            // Keep merging until the merge process is complete
            while (isMergeFinished == false) {
                // If at the very start of merges, don't increase runSize
                // After every merge, increase runSize by 2 times (number of runs being merged at once)
                if (isMergeStart == true) {
                    isMergeStart = false;
                } else {
                    //Only start increasing run size after first merge
                    runSize = runSize * numOfRunsToMerge;
                }

                Merge merge = new Merge(numOfRunsToMerge, runSize, tempFiles);
                merge.writeRuns();
                if (merge.isAtEndOfMerge() == true) {
                    // If at end of merge, output content to sorted.txt
                    writeFinalOutput("output.txt");
                    isMergeFinished = true;
                    System.out.print("bye");
                }
            }
    }






    public static class Merge{

        private int runSize;
        private File[] files;
        private int[] fileLineCountArray;
        private String[] topOfRunsArray;
        private Scanner[] scannersArray;
        private BufferedWriter[] writerArray;
        private BufferedWriter currentWriter;

        public Merge(int numOfRunsToMerge, int runSize, File[] tempFiles) {
            try {
                this.files = tempFiles;
                this.runSize = runSize;
                fileLineCountArray = new int[numOfRunsToMerge];
                topOfRunsArray = new String[numOfRunsToMerge];
                scannersArray = new Scanner[numOfRunsToMerge];
                writerArray = new BufferedWriter[numOfRunsToMerge];
                Arrays.fill(fileLineCountArray, 0);
                //Populate both arrays with 0 and 1 index
                for (int i = 0; i < numOfRunsToMerge; i++) {
                    scannersArray[i] = new Scanner(files[i]);
                    File outputFile = File.createTempFile("output", ".tmp");
                    writerArray[i] = new BufferedWriter(new FileWriter(outputFile));
                }
                currentWriter = writerArray[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void writeRuns() throws IOException {
            if (isAllRunsNotEmpty()) {
                DoMerge();
                
            } else {
                for (int i = 0; i < 2; i++) {
                    if (scannersArray[i].hasNextLine()) {
                        currentWriter.write(topOfRunsArray[i]);
                        currentWriter.newLine();
                        fileLineCountArray[i]++;
                        writeRestOfRun(i);
                    }
                    writerArray[i].close();
                }
            }
        }

        

        private void DoMerge() {
            try {
                while (!isEndOfAllRuns()) {
                    if (isTopOfRunsEmpty()) {
                        initialiseTopOfRuns();
                    }
                    String smallestValueToWrite = compareSmallest();
                    int smallestValueFromFileCounter = compareSmallestCount();
                    currentWriter.write(smallestValueToWrite);
                    currentWriter.newLine();
                    fileLineCountArray[smallestValueFromFileCounter]++;
                    if (isEndOfRun(fileLineCountArray[smallestValueFromFileCounter], scannersArray[smallestValueFromFileCounter])) {
                        int otherFileCount = 1 - smallestValueFromFileCounter;
                        currentWriter.write(topOfRunsArray[otherFileCount]);
                        currentWriter.newLine();
                        fileLineCountArray[otherFileCount]++;
                        writeRestOfRun(otherFileCount);
                        currentWriter = writerArray[0];
                        Arrays.fill(topOfRunsArray, null);
                        writeRuns();
                    } else {
                        topOfRunsArray[smallestValueFromFileCounter] = scannersArray[smallestValueFromFileCounter].nextLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean isEndOfRun(int fileLineCount, Scanner scanner) {
            return !scanner.hasNextLine() || (fileLineCount % runSize == 0 && fileLineCount != 0);
        }

        private boolean isAllRunsNotEmpty() {
            // Check if the first two scanners have lines left to read
            for (int i = 0; i < Math.min(scannersArray.length, 2); i++) {
                if (!scannersArray[i].hasNextLine()) {
                    return false;
                }
            }
            return true;
        }

        private boolean isEndOfAllRuns(){
            // If any files still have lines left, there are runs left, return false
            for(int i = 0; i < scannersArray.length; i++){
                if(scannersArray[i].hasNextLine()){
                    return false;
                }
            }
            return true;
        }

              /*
         * Checks to see if topOfRunsArray is null
         * Return true if any value in topOfRunsArray is null. Otherwise false
         */
        private boolean isTopOfRunsEmpty(){
            boolean isEmpty = false;
            for(int i = 0; i < topOfRunsArray.length; i++){
                if(topOfRunsArray[i] == null){
                    isEmpty = true;
                }
            }
            return isEmpty;
        }
    
        /*
         * For every value in topOfRunsArray, make it the next line of its respective file
         */
        private void initialiseTopOfRuns(){
            for(int i = 0; i < topOfRunsArray.length; i++){
                if(scannersArray[i].hasNextLine()){
                    topOfRunsArray[i] = scannersArray[i].nextLine();
                }
            }
        }
    
        /*
         * Takes in an array of two and returns the smaller value out of the two
         */
        private String compareSmallest(){
            String one = topOfRunsArray[0];
            String two = topOfRunsArray[1];
            String smallestValue;
            int result = one.compareTo(two);
            if(result > 0){
                smallestValue = two;
            }
            else{
                smallestValue = one;
            }
            return smallestValue;
        }
    
        /*
         * Takes in an array of two, returns the index of where the smaller value came from
         */
        private int compareSmallestCount(){
            String one = topOfRunsArray[0];
            String two = topOfRunsArray[1];
            int smallestValueCount;
            int result = one.compareTo(two);
            if(result > 0){
                smallestValueCount = 1;
            }
            else{
                smallestValueCount = 0;
            }
            return smallestValueCount;
        }

        /*
         * Writes out the rest of the run
         * fileCount is the count of the file currently reading
         */
        private void writeRestOfRun(int fileCount){
            try{
                while(isEndOfRun(fileLineCountArray[fileCount], scannersArray[fileCount]) == false){
                    // Update top of run
                    topOfRunsArray[fileCount] = scannersArray[fileCount].nextLine();
                    // Get top of run, write it out to specified file
                    currentWriter.write(topOfRunsArray[fileCount]);
                    currentWriter.newLine();
                    //System.out.println(topOfRunsArray[fileCount]);
                    fileLineCountArray[fileCount]++;
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        public boolean isAtEndOfMerge() {
            // Iterate through the scannersArray
            for (Scanner scanner : scannersArray) {
                // If any scanner has more lines to read, return false
                if (scanner.hasNextLine()) {
                    return false;
                }
            }
            // If all scanners have reached the end of their files, return true
            return true;
        }

    }

    private static void writeFinalOutput(String finalSortedFileName) {
        try {
            // Write everything from file2 to final output file
            String line;
            File file2 = new File("k2.tmp");
            Scanner scanner = new Scanner(file2);
            File sortedFile = new File(finalSortedFileName);
            sortedFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(sortedFile));
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                writer.write(line);
                writer.newLine();
            }
            scanner.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }






}