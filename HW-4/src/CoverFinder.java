import java.util.Arrays;
import java.util.List;

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

//    private void sort(boolean[][] input) {
//        int[] numElements = new int[subsetCount];
//        for (int i = 0; i < numElements.length; i++) {
//            numElements[i] = trueCount(input[i], input[i].length);
//        }
//
//        return;
//    }

    public boolean[][] find() {
//        sort(input);
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
        } else if (currentCover != null && hasSubsetOf(n-1, n)) {
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
     *
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
     * Returns true if any subsets of partialSol[pos] exist.
     *
     * @param pos
     * @param n
     * @return
     */
    private boolean hasSubsetOf(int pos, int n) {
        if (!partialSol[pos]) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (pos != i && isSubsetOf(i, pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if partialSol[first] is a subset of partialSol[second]
     *
     * @param first
     * @param second
     * @return
     */
    private boolean isSubsetOf(int first, int second) {
        if (!partialSol[first] || !partialSol[second]) {
            return false;
        }
        for (int i = 0; i < input.length; i++) {
            if (input[first][i] && !input[second][i]) {
                return false;
            }
        }
        return true;
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

}
