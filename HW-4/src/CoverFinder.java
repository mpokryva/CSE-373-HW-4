import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Miki on 11/10/2017.
 */
public class CoverFinder {

    private SetBundle bundle;
    private List<HashSet<Integer>> currentCover;
    private boolean isFinished;

    public CoverFinder(SetBundle bundle) {
        this.bundle = bundle;
    }

    public List<HashSet<Integer>> find() {
        bundle.sort();
        return find(new boolean[bundle.getSetCount()], 0, 0);
    }

    private List<HashSet<Integer>> find(boolean[] arr, int n, int numSubsets) {
        if (currentCover != null && numSubsets >= currentCover.size()) { // Prune searches which are already >= than current best.
            return currentCover;
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
                int incr = (candidates[i]) ? 1 : 0;
                find(arr, n + 1, numSubsets + incr);
            }
        }
        return currentCover;
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
