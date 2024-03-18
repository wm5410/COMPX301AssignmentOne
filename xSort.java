import java.io.*;
import java.util.*;

//need Priority queue 

public class xSort {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java xSort <lines> <initial_runs_file> <merged runs>");
            System.exit(1);
        }

        int m = Integer.parseInt(args[0]); // Number of lines in each initial run
        String initialRunsFile = args[1]; // File with initial runs
        int k = Integer.parseInt(args[2]); // Number of runs merged on each pass

        try {
            mergeRuns(initialRunsFile, m, k);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void mergeRuns(String initialRunsFile, int m, int k){
        //test
        System.out.println(initialRunsFile + " " + m + " " + k);
    }
}
