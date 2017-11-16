import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Miki on 11/10/2017.
 */
public class CoverFinder {

    private boolean[][] input;
    private List<List<Integer>> sparseInput;
    private int[] currentCover;
    private final int subsetCount;
    private final int universalSetSize;
    private static final int DNU = -1; // INVALID INDEX (DNU = DO NOT USE).
    /*
    The current partial solution. partialSol[i] holds the row index (input[partialSol[i]]), for the ith element of the solution.
     */
    private int[] partialSol;
    int count = 0;

    public CoverFinder(boolean[][] input) {
        this.input = input;
        sparseInput = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int j = 0; j < input[i].length; j++) {
                if (input[i][j]) {
                    list.add(j);
                }
            }
            sparseInput.add(list);
        }
        subsetCount = input.length;
        universalSetSize = input[0].length;
    }

    public boolean[][] find() {
//        sort(input);
        partialSol = new int[subsetCount];
        find(0);
        System.out.println("NUM OF CALLS: " + count);
        return makeSet(currentCover, currentCover.length);
    }

    private void find(int n) {
        count++;
        if (isASolution(n)) {
            currentCover = new int[n];
            System.arraycopy(partialSol, 0, currentCover, 0, n);
        } else if (n > 0 && shouldPrune(n)) {
            return;
        } else {
            int[] candidates = constructCandidates1(n); // holds potential partialSol indices for the nth subset.
            int stoppingIndex = getStoppingIndex(candidates.length);
            for (int i = 0; i < stoppingIndex; i++) {
                partialSol[n] = candidates[i];
                makeMove(n);
//                int incr = (partialSol[n]) ? 1 : 0;
                find(n + 1);
//                partialSol[n] = 0;
                unmakeMove(n);
                stoppingIndex = getStoppingIndex(candidates.length);
            }
        }
    }

    private int getStoppingIndex(int numCandidates) {
        int stoppingIndex = (currentCover != null) ? currentCover.length : numCandidates;
        stoppingIndex = (stoppingIndex > numCandidates) ? numCandidates : stoppingIndex;
        return stoppingIndex;
    }

    private boolean isASolution(int n) {
        return (currentCover == null || n < currentCover.length) && isCover(n);
    }

    private boolean shouldPrune(int n) {
        boolean ret = false;
        if (currentCover != null && n >= currentCover.length) { // Prune searches which are already >= than current best.
            ret = true;
        } else if (currentCover != null && (getDiff(partialSol, n - 1, n) == 0)) { // Prune if doesn't contain any unique elements.
            ret = true;
        } else if (currentCover != null && (hasSubsetOf(n - 1, n))) { // Prune if has a useless set somewhere.
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
    private int[] constructCandidates1(int n) {
        int cands[] = new int[subsetCount];
        for (int i = 0; i < subsetCount; i++) {
            cands[i] = i;
        }
        for (int i = 0; i < n; i++) { // Remove currently used sets in partialSol.
            cands[partialSol[i]] = DNU;
        }
        for (int i = 0; i < n; i++) {
            if (getDiff(cands, i, n) == 0) {
                cands[i] = DNU;
            }
        }
        int dnuCount = 0;
        for (int i = 0; i < cands.length; i++) {
            if (cands[i] == DNU) {
                dnuCount++;
            }
        }
        int[] ret = new int[cands.length - dnuCount];
        int retIndex = 0;
        for (int i = 0; i < cands.length; i++) {
            if (cands[i] != DNU) {
                ret[retIndex] = i;
                retIndex++;
            }
        }
        return sortCandidates(ret, n);
    }

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
        return sortCandidates(ret, n);
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
    private int getDiff(int[] rows, int pos, int n) {
        int diff = 0;
        int oldValue = rows[pos];
        rows[pos] = DNU;
        boolean[] union = getUnion(n);
        rows[pos] = oldValue;
        int row = rows[pos];
        if (row == DNU) {
            return -1;
        }
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

    private boolean[] getUnion1(int n) {
        boolean[] universalSet = new boolean[universalSetSize];
        for (int i = 0; i < n; i++) {
            int row = partialSol[i];
            if (row != DNU) {
                List<Integer> list = sparseInput.get(row);
                for (int j = 0; j < list.size(); j++) {
                    universalSet[list.get(j)] = true;
                }
            }
        }
        return universalSet;
    }

    private int[] sortCandidates(int[] cands, int n) {
        List<Candidate> candsList = new ArrayList<>(cands.length);
        for (int i = 0; i < cands.length; i++) {
            candsList.add(new Candidate(cands[i], -1 * getDiff(cands, i, n)));
        }
        Collections.sort(candsList);
        int[] ret = new int[candsList.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = candsList.get(i).candIndex;
        }
        return ret;
    }

    class Candidate implements Comparable<Candidate> {
        private int candIndex;
        private int priority;

        Candidate(int candIndex, int priority) {
            this.candIndex = candIndex;
            this.priority = priority;
        }

        @Override
        public int compareTo(Candidate other) {
            return priority - other.priority;
        }

    }

}
