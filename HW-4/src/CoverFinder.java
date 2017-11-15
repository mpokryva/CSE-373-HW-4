/**
 * Created by Miki on 11/10/2017.
 */
public class CoverFinder {

    private boolean[][] input;
    private boolean[] currentCover;
    private int currentCoverSize;
    private boolean isFinished;
    private final int subsetCount;
    private final int universalSetSize;
    private boolean[] partialSol;
    int count = 0;

    public CoverFinder(boolean[][] input) {
        this.input = input;
        subsetCount = input.length;
        universalSetSize = input[0].length;
    }

    public boolean[][] find() {
//        input.sort();
        partialSol = new boolean[input.length];
        find(0, 0);
        System.out.println("NUM OF CALLS: " + count);
        return makeSet(currentCover, subsetCount, currentCoverSize);
    }

    private void find(int n, int numSubsets) {
        count++;
//        SetBundle current = makeSet(partialSol, n);
        if (n == partialSol.length) {
            if (isCover(partialSol, n) && (currentCover == null || numSubsets < currentCoverSize)) {
                currentCover = (currentCover == null) ? new boolean[partialSol.length] : currentCover;
                System.arraycopy(partialSol, 0, currentCover, 0, partialSol.length);
                currentCoverSize = numSubsets;
            }
        } else if (currentCover != null && numSubsets >= currentCoverSize) { // Prune searches which are already >= than current best.
            return;
        } else if (currentCover != null && getDiff(n - 1, n) == 0) {
            return;
        } else {
            boolean[] candidates = new boolean[2];
            candidates[0] = false;
            candidates[1] = true;
            for (int i = 0; i < candidates.length; i++) {
                partialSol[n] = candidates[i];
                int incr = (partialSol[n]) ? 1 : 0;
                find(n + 1, numSubsets + incr);
            }
        }
    }


    /**
     * Returns the number of unique elements the specified set has in comparison to the union of the partial solution.
     * @return
     */
    private int getDiff(int pos, int n) {
        if (!partialSol[pos]) {
            return -1;
        }
        int diff = 0;
        boolean oldValue = partialSol[pos];
        partialSol[pos] = false;
        boolean[] union = getUnion(partialSol, n);
        partialSol[pos] = oldValue;
        boolean[] set = input[pos];
        for (int i = 0; i < set.length; i++) {
            int incr = (set[i] && !union[i]) ? 1 : 0;
            diff += incr;
        }
        return diff;
    }


    /**
     * Returns a set representing the current solution.
     *
     * @param partialSol
     * @param n
     * @return
     */
    private boolean[][] makeSet(boolean[] partialSol, int n, int numSubsets) {
        boolean[][] ret = new boolean[numSubsets][universalSetSize];
        int r = 0;
        for (int i = 0; i < partialSol.length; i++) { // Row
            if (partialSol[i]) {
                System.arraycopy(input[i], 0, ret[r], 0, input[i].length);
                r++;
            }
        }
        return ret;
    }

    private boolean isCover(boolean[] partialSol, int n) {
        boolean[] union = getUnion(partialSol, n);
        return trueCount(union, union.length) == universalSetSize;
    }

    private int trueCount(boolean[] partialSol, int n) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (partialSol[i]) {
                count++;
            }
        }
        return count;
    }

    private boolean[] getUnion(boolean[] partialSol, int n) {
        boolean[] universalSet = new boolean[universalSetSize];
        for (int i = 0; i < n; i++) { // Row.
            if (partialSol[i]) {
                for (int j = 0; j < universalSetSize; j++) { // Col.
                    if (input[i][j]) {
                        universalSet[j] = true;
                    }
                }
            }
        }
        return universalSet;
    }


//    public List<List<HashSet<Integer>>> getPowerSet() {
//        return getPowerSet(new ArrayList<>(), new boolean[input.getSetCount()], 0);
//    }
//
//    private List<List<HashSet<Integer>>> getPowerSet(List<List<HashSet<Integer>>> powerSet, boolean[] arr, int n) {
//        if (n == arr.length) {
//            List<HashSet<Integer>> subset = new ArrayList<>();
//            for (int i = 0; i < arr.length; i++) {
//                if (arr[i]) {
//                    subset.add(input.getSets().get(i));
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


}
