
public class makeRuns {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Incorrect Parameters - Make sure you follow: java makeRuns m filename");
        }

        try {
            int m = Integer.parseInt(args[0]);
            String filename = args[1]; 
            if (m < 16 || m > 32768) {
                System.err.println("Incorrect Parameters: m = 16 to 32768");
            }
            else{
                System.out.println(m);
                System.out.println(filename);
            }

            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("Done");
    }
}
