import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by mpokr on 11/13/2017.
 */
public class PrintUtils {

    public static void sortedSet(HashSet<Integer> set) {
        Integer[] temp = new Integer[set.size()];
        Arrays.sort(set.toArray(temp));
        System.out.println(Arrays.toString(temp));
    }

    public static void sets(boolean[][] sets) {
        for (int i = 0; i < sets.length; i++) {
            for (int j = 0; j < sets[i].length; j++) {
                if (sets[i][j]) {
                    System.out.print(j + 1 + " ");
                }
            }
            System.out.println();
        }

    }

    public static void starDivider() {
        System.out.println("***************");
    }

    public static void dashDivider() {
        System.out.println("---------------");
    }
}
