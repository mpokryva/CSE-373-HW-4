import java.util.*;

/**
 * Created by Miki on 11/10/2017.
 */
public class CoverFinder {

    private SetBundle bundle;
    private List<HashSet<Integer>> currentCover;
    private boolean isFinished;
    int count = 0;

    public CoverFinder(SetBundle bundle) {
        this.bundle = bundle;
    }

    public List<HashSet<Integer>> find() {
        bundle.sort();
        find(new boolean[bundle.getSetCount()], 0);
        System.out.println("NUM OF CALLS: " + count);
        return currentCover;
    }

    private void find(boolean[] arr, int n) {
        count++;
        if (currentCover != null && trueCount(arr) >= currentCover.size()) { // Prune searches which are already >= than current best.
            return;
        }
        if (n == arr.length) {
            List<HashSet<Integer>> subset = makeSet(arr);
            if (isCover(subset) && (currentCover == null || subset.size() < currentCover.size())) {
                currentCover = subset;
            }
        } else {
            boolean[] candidates = new boolean[2];
            candidates[0] = false;
            candidates[1] = true;
            for (int i = 0; i < candidates.length; i++) {
                arr[n] = candidates[i];
                find(arr, n + 1);
            }
        }
    }

    private List<HashSet<Integer>> makeSet(boolean[] arr) {
        List<HashSet<Integer>> subset = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]) {
                subset.add(bundle.getSets().get(i));
            }
        }
        return subset;
    }

    private int trueCount(boolean[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            count = (arr[i]) ? count + 1 : count;
        }
        return count;
    }

    private boolean isCover(List<HashSet<Integer>> sets) {
        HashSet<Integer> universalSet = new HashSet<>();
        for (HashSet<Integer> set : sets) {
            for (Integer i : set) {
                universalSet.add(i);
            }
        }
        return (universalSet.size() == bundle.getUniversalSetSize());
    }


    public List<List<HashSet<Integer>>> getPowerSet() {
        return getPowerSet(new ArrayList<>(), new boolean[bundle.getSetCount()], 0);
    }

    private List<List<HashSet<Integer>>> getPowerSet(List<List<HashSet<Integer>>> powerSet, boolean[] arr, int n) {
        if (n == arr.length) {
            List<HashSet<Integer>> subset = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                if (arr[i]) {
                    subset.add(bundle.getSets().get(i));
                }
            }
            powerSet.add(subset);
        } else {
            boolean[] candidates = new boolean[2];
            candidates[0] = false;
            candidates[1] = true;
            for (int i = 0; i < candidates.length; i++) {
                arr[n] = candidates[i];
                getPowerSet(powerSet, arr, n + 1);
            }
        }
        return powerSet;
    }


}
