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

    private SetBundle parse(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int universalSetSize = Integer.parseInt(reader.readLine().trim());
            int subsetCount = Integer.parseInt(reader.readLine().trim());
            boolean[][] sets = new boolean[subsetCount][universalSetSize + 1]; // first entry is sentinel
            String current;
            int row = 0;
            while ((current = reader.readLine()) != null) {
                current = current.trim();
                String[] elements = current.split(" ");
                if (!elements[0].equals("")) {
                    for (int i = 0; i < elements.length; i++) {
                        int col = Integer.parseInt(elements[i]);
                        sets[row][col] = true;
                    }
                }
                row++;
            }
            SetBundle ret = new SetBundle(sets);
            System.out.println(ret);
            return ret;
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
        SetBundle bundle = new InputParser().parse(args[0]);
        System.out.println(bundle);
        System.out.println("--------------------------");
//        CoverFinder coverFinder = new CoverFinder(bundle);
//        SetBundle cover = coverFinder.find();
//        System.out.println(cover);
//        System.out.println("MINIMUM COVER of size " +  cover.size() + "  in  " + (double)(System.currentTimeMillis() - startTime) / 1000  + " seconds");

    }


}
