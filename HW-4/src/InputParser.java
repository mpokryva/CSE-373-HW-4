import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Miki on 11/10/2017.
 */
public class InputParser {

    private boolean[][] parse(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int universalSetSize = Integer.parseInt(reader.readLine().trim());
            int subsetCount = Integer.parseInt(reader.readLine().trim());
            boolean[][] sets = new boolean[subsetCount][universalSetSize]; // first entry is sentinel
            String current;
            int row = 0;
            while ((current = reader.readLine()) != null) {
                current = current.trim();
                String[] elements = current.split(" ");
                if (!elements[0].equals("")) {
                    for (int i = 0; i < elements.length; i++) {
                        int col = Integer.parseInt(elements[i]) - 1;
                        sets[row][col] = true;
                    }
                }
                row++;
            }
            return sets;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void printPowerSet(List<List<HashSet<Integer>>> powerSet) {
        int count = 0;
        for (List<HashSet<Integer>> subset : powerSet) {
            count++;
            System.out.println("**************************");
            String ret = "";
            for (HashSet<Integer> set : subset) {
                for (Integer i : set) {
                    ret += (i + " ");
                }
                ret += "\n";
            }
            System.out.print(ret);
            System.out.println("**************************");
        }
        System.out.println("Number of sets: " + count);
    }


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        boolean[][] input = new InputParser().parse(args[0]);
        PrintUtils.sets(input);
        PrintUtils.dashDivider();
        CoverFinder coverFinder = new CoverFinder(input);
        boolean[][] cover = coverFinder.find();
        PrintUtils.sets(cover);
        System.out.println("MINIMUM COVER of size " +  cover.length + "  in  " + (double)(System.currentTimeMillis() - startTime) / 1000  + " seconds");

    }


}
