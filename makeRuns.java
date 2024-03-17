//Students:
//William Malone - 1604564
//Justin Poutoa  - 1620107

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is for creating the initial runs
 */
public class makeRuns {
    /**
     * This class is for:
     * 1. Sorting a large dataset into chunks 
     * 2. Sorting each chunk via Quickksort
     * 3. Output the sorted runs to files
     * @param args
     */
    public static void main(String[] args) {
        //Check Parameters
        if (args.length != 2) {
            System.err.println("Error: Incorrect Parameters");
        }

        try {
            //The number of lines to be sorted in each initial run
            int m = Integer.parseInt(args[0]);
            //The name of the file
            String filename = args[1]; 
            //only for 16 <= m <= 32768
            if (m < 16 || m > 32768) {
                System.err.println("Incorrect Parameters: m must be between 16 and 32768");
            }
            else{
                //Call createRuns method
                createRuns(m, filename);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Done");
    }

    /**
     * Method to create runs of sorted lines from the input file
     */
    public static void createRuns(int m, String inputFile) throws IOException{
        //Read the contents of filename into a buffered reader 
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        //Array to store lines of each run
        String[] lines = new String[m];
        // Counter to keep track of the number of lines read in the current run
        int count = 0;
        // Counter to keep track of the total number of runs created
        int runCount = 0;
        try{
            String line;
            while((line = reader.readLine()) != null){
                //Store the current line in the lines array
                lines[count++] = line;
                //Check if the current run is full
                if(count == m){
                    //Sort the array
                    heapSort(lines);
                    //Print the stored lines to output
                    for(String sortedLine : lines){
                        System.out.println(sortedLine);
                    }
                    //Reset counter for the next run
                    count = 0;
                    //Increment the total run count
                    runCount++;
                }
            }
        } catch(Exception e){
            System.err.println("Error for createRuns: " + e.getMessage());
        }
        //Close the Buffer reader
        reader.close();
    }

    /**
     * Method to perform heap sort on an array of strings
     * @param arr
     */
    public static void heapSort(String arr[]){
        int N = arr.length;
        //Build heap (rearrange the array)
        for(int i = N/2-1; i >= 0; i--){
            //Call the heapify 
            heapify(arr, N, i);
        }
        //One by one extract an element from the heap 
        for(int i = N-1; i > 0; i--){
            //Move current root to end
            String temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            //Call max heapify on the reduced heap
            heapify(arr, i, 0);
        }
    }

    /**
     * Heapify a subtree  with node 'i' which is an index in 'arr[]'
     * @param arr is the array
     * @param N an integer
     * @param i another integer
     */
    static void heapify(String arr[], int N, int i){
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if(l < N && arr[l].compareTo(arr[largest]) > 0){
            largest = l;
        }

        if(r < N && arr[r].compareTo(arr[largest]) > 0){
            largest = r;
        }

        if(largest != i){
            String swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, N, largest);
        }
    }

    /**
     * Method to perform heap sort on a part of the array 
     * @param arr the array
     * @param N an integer
     */
    static void heapSort(String arr[], int N) {
        // Build heap (rearrange array)
        for (int i = N / 2 - 1; i >= 0; i--)
            heapify(arr, N, i);

        // One by one extract an element from heap
        for (int i = N - 1; i > 0; i--) {
            // Move current root to end
            String temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // call max heapify on the reduced heap
            heapify(arr, i, 0);
        }
    }

    /**
     * Method to print an array of strings
     * @param arr
     */
    static void printArray(String arr[]) {
        int N = arr.length;

        for (int i = 0; i < N; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
}
