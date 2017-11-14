import java.util.*;

/**
 * Created by Miki on 11/10/2017.
 */
public class SetBundle {

    private boolean[][] sets;

    public SetBundle(boolean[][] sets) {
        this.sets = sets;
    }

    public boolean[][] getSets() {
        return sets;
    }

    public boolean[] getSet(int i) {
        return sets[i];
    }

    public int getSetCount() {
        return sets.length;
    }

    public int getUniversalSetSize() {
        if (sets == null) {
            return 0;
        } else {
            return sets[0].length;
        }
    }

    public void sort() {

    }

    @Override
    public String toString() {
        String ret = "";
        for (int i = 0; i < sets.length; i++) {
            for (int j = 0; j < sets[i].length; j++) {
                if (sets[i][j]) {
                    ret += j + " ";
                }
            }
            ret += "\n";
        }
        return ret;
    }

    public static void printSets(boolean[][] sets) {
        System.out.print(new SetBundle(sets));
    }
}
