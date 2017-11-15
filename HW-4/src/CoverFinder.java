import java.util.*;

/**
 * Created by Miki on 11/10/2017.
 */
public class CoverFinder {

    private boolean[][] input;
    private int[] currentCover;
    private boolean isFinished;
    private final int subsetCount;
    private final int universalSetSize;
    private static final int DNU = -1; // INVALID INDEX (DO NOT USE).
    /*
    The current partial solution. partialSol[i] holds the row index (input[partialSol[i]]), for the ith element of the solution.
     */
    private int[] partialSol;
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
        partialSol = new int[subsetCount];
        find(0);
        System.out.println("NUM OF CALLS: " + count);
        return makeSet(currentCover, currentCover.length);
    }

    private void find(int n) {
        count++;
//        if (isASolution(n)) {
        if (isASolution(n)) {
            currentCover = new int[n];
            System.arraycopy(partialSol, 0, currentCover, 0, n);
//            }
        } else if (n > 0 && shouldPrune(n)) {
            return;
        } else {
            int[] candidates = constructCandidates(n); // holds potential partialSol indices for the nth subset.
            for (int i = 0; i < candidates.length; i++) {
                partialSol[n] = candidates[i];
                makeMove(n);
//                int incr = (partialSol[n]) ? 1 : 0;
                find(n + 1);
//                partialSol[n] = 0;
                unmakeMove(n);
            }
        }
    }

    private boolean isASolution(int n) {
        return isCover(n) && (currentCover == null || n < currentCover.length);
    }

    private boolean shouldPrune(int n) {
        boolean ret = false;
        if (currentCover != null && n >= currentCover.length) { // Prune searches which are already >= than current best.
            ret = true;
        } else if (currentCover != null && (getDiff(n - 1, n) == 0)) {
            ret = true;
        } else if (currentCover != null && (hasSubsetOf(n - 1, n))) {
            ret = true;
        }
        return ret;
    }

    /*
    Returns an array of possible candidates for partialSol[n].
    A set is a candidate if:
        1) It has not yet been used.
        2) It contains a useful element(s) (diff from union of sets in partialSol is not empty).

     */
    private int[] constructCandidates(int n) {
        HashSet<Integer> cands = new HashSet<>();
        for (int i = 0; i < subsetCount; i++) {
            cands.add(i);
        }
        for (int i = 0; i < n; i++) { // Remove currently used sets in partialSol.
            cands.remove(partialSol[i]);
        }
        int[] ret = new int[cands.size()];
        Iterator<Integer> it = cands.iterator();
        int i = 0;
        while (it.hasNext()) {
            ret[i] = it.next();
            i++;
        }
        return ret;
    }

    private void makeMove(int n) {

    }

    private void unmakeMove(int n) {

    }


    /**
     * Returns the number of unique elements the specified set has in comparison to the union of the partial solution.
     *
     * @return
     */
    private int getDiff(int pos, int n) {
        int diff = 0;
        int oldValue = partialSol[pos];
        partialSol[pos] = DNU;
        boolean[] union = getUnion(n);
        partialSol[pos] = oldValue;
        int row = partialSol[pos];
        boolean[] set = input[row];
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
//        if (!partialSol[first] || !partialSol[second]) {
//            return false;
//        }
        for (int i = 0; i < universalSetSize; i++) {
            int firstRow = partialSol[first];
            int secondRow = partialSol[second];
            if (input[firstRow][i] && !input[secondRow][i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * Returns a set representing the current solution.
     *
     * @param sol
     * @param n
     * @return
     */
    private boolean[][] makeSet(int[] sol, int n) {
        boolean[][] ret = new boolean[n][universalSetSize];
        int r = 0;
        for (int i = 0; i < n; i++) { // Row
            int row = sol[i];
            System.arraycopy(input[row], 0, ret[r], 0, input[i].length);
            r++;
        }
        return ret;
    }

    private boolean isCover(int n) {
        boolean[] union = getUnion(n);
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

    private boolean[] getUnion(int n) {
        boolean[] universalSet = new boolean[universalSetSize];
        for (int i = 0; i < n; i++) {
            int row = partialSol[i];
            if (row != DNU) {
                for (int j = 0; j < universalSetSize; j++) { // Col.
                    if (input[row][j]) {
                        universalSet[j] = true;
                    }
                }
            }
        }
        return universalSet;
    }

}
