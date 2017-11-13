import java.util.*;

/**
 * Created by Miki on 11/10/2017.
 */
public class SetBundle {

    private List<HashSet<Integer>> sets;
    private int universalSetSize;

    public SetBundle(List<HashSet<Integer>> sets, int universalSetSize) {
        this.sets = sets;
        this.universalSetSize = universalSetSize;
    }

    public List<HashSet<Integer>> getSets() {
        return sets;
    }

    public HashSet<Integer> getSet(int i) {
        return sets.get(i);
    }

    public int getSetCount() {
        return sets.size();
    }

    public int getUniversalSetSize() {
        return universalSetSize;
    }

    public void sort() {
        Collections.sort(sets, (HashSet<Integer> s1, HashSet<Integer> s2) -> s1.size() - s2.size());
    }

        @Override
    public String toString() {
        String ret = "";
        for (HashSet<Integer> set : sets) {
            Integer[] arr = new Integer[set.size()];
            Arrays.sort(set.toArray(arr));
            for (Integer i : arr) {
                ret += (i + " ");
            }
            ret += "\n";
        }
        return ret;
    }

    public static void printSets(List<HashSet<Integer>> sets) {
        System.out.print(new SetBundle(sets, 0));
    }
}
