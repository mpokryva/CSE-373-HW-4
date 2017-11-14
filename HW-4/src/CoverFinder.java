//import java.util.*;
//
///**
// * Created by Miki on 11/10/2017.
// */
//public class CoverFinder {
//
//    private SetBundle bundle;
//    private SetBundle currentCover;
//    private boolean isFinished;
//    int count = 0;
//
//    public CoverFinder(SetBundle bundle) {
//        this.bundle = bundle;
//    }
//
//    public SetBundle find() {
//        bundle.sort();
//        find(new boolean[bundle.getSetCount()], 0, 0);
//        System.out.println("NUM OF CALLS: " + count);
//        return currentCover;
//    }
//
//    private void find(boolean[] arr, int n, int numSubsets) {
//        count++;
//        SetBundle current = makeSet(arr, n);
//        if (n == arr.length) {
//            if (isCover(current) && (currentCover == null || current.size() < currentCover.size())) {
//                currentCover = current;
//            }
//        } else if (currentCover != null && numSubsets >= currentCover.size()) { // Prune searches which are already >= than current best.
//            return;
//        } else if (currentCover != null && getDiff(current, current.get(current.size() - 1)) == 0) {
//            return;
//        } else {
//            boolean[] candidates = new boolean[2];
//            candidates[0] = false;
//            candidates[1] = true;
//            for (int i = 0; i < candidates.length; i++) {
//                arr[n] = candidates[i];
//                int incr = (arr[n]) ? 1 : 0;
//                find(arr, n + 1, numSubsets + incr);
//            }
//        }
//    }
//
//
//    private int getDiff(List<HashSet<Integer>> sets, HashSet<Integer> target) {
//        sets.remove(target);
//        HashSet<Integer> universalSet = makeUniversalSet(sets);
//        HashSet<Integer> cpy = new HashSet<>(target);
//        cpy.removeAll(universalSet);
//        sets.add(target);
//        return cpy.size();
//    }
//
//
//
//
//
//
//
//
//
//
//    private SetBundle makeSet(boolean[] arr, int n) {
//        int numSubsets = 0;
//        for (int i = 0; i < n; i++) {
//            if (arr[i]) {
//                numSubsets++;
//            }
//        }
//        boolean[][] set = new boolean[numSubsets][bundle.getUniversalSetSize()];
//        for (int i = 0; i < n; i++) {
//
//        }
//
//        return subset;
//    }
//
//    private boolean isCover(List<HashSet<Integer>> sets) {
//        return (makeUniversalSet(sets).size() == bundle.getUniversalSetSize());
//    }
//
//    private HashSet<Integer> makeUniversalSet(List<HashSet<Integer>> sets) {
//        HashSet<Integer> universalSet = new HashSet<>();
//        for (HashSet<Integer> set : sets) {
//            for (Integer i : set) {
//                universalSet.add(i);
//            }
//        }
//        return universalSet;
//    }
//
//
//    public List<List<HashSet<Integer>>> getPowerSet() {
//        return getPowerSet(new ArrayList<>(), new boolean[bundle.getSetCount()], 0);
//    }
//
//    private List<List<HashSet<Integer>>> getPowerSet(List<List<HashSet<Integer>>> powerSet, boolean[] arr, int n) {
//        if (n == arr.length) {
//            List<HashSet<Integer>> subset = new ArrayList<>();
//            for (int i = 0; i < arr.length; i++) {
//                if (arr[i]) {
//                    subset.add(bundle.getSets().get(i));
//                }
//            }
//            powerSet.add(subset);
//        } else {
//            boolean[] candidates = new boolean[2];
//            candidates[0] = false;
//            candidates[1] = true;
//            for (int i = 0; i < candidates.length; i++) {
//                arr[n] = candidates[i];
//                getPowerSet(powerSet, arr, n + 1);
//            }
//        }
//        return powerSet;
//    }
//
//
//}
