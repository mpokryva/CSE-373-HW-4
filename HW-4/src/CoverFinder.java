import java.util.*;

/**
 * Created by Miki on 11/10/2017.
 */
public class CoverFinder {

    private boolean[][] input;
    private List<List<Integer>> sparseInput;
    private int[] currentCover;
    private int subsetCount;
    private final int universalSetSize;
    private static final int DNU = -1; // INVALID INDEX (DNU = DO NOT USE).
    private int[] includedElements;
    private int includedCount;
    private boolean[] boolIncluded;
    private int[][] setSizeSums;
    private int[] setToSizeList;
    private int prunedCount;
    /*
    The current partial solution. partialSol[i] holds the row index (input[partialSol[i]]), for the ith element of the solution.
     */
    private int[] partialSol;
    private int count = 0;

    public CoverFinder(boolean[][] initialInput) {
        this.input = initialInput;
        subsetCount = initialInput.length;
        universalSetSize = initialInput[0].length;
        trimInput();
        setToSizeList = new int[subsetCount];
        updateSetSizesList();
        sortInput();
        updateSetSizesList();
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
        setSizeSums = new int[subsetCount][subsetCount];
        for (int i = 0; i < subsetCount; i++) {
            int sum = 0;
            int k = i;
            for (int j = 0; j < subsetCount - i; j++) {
                sum += sparseInput.get(k).size();
                k++;
                setSizeSums[i][j] = sum;
            }
        }
        includedElements = new int[universalSetSize];
        boolIncluded = new boolean[universalSetSize];
        includedCount = 0;
    }

    private int trueCount(boolean[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            count = (arr[i]) ? count + 1 : count;
        }
        return count;
    }

    private void sortInput() {
        List<Candidate> listToSort = new ArrayList<>(subsetCount);
        for (int i = 0; i < subsetCount; i++) {
            listToSort.add(new Candidate(i, -1 * setToSizeList[i]));
        }
        Collections.sort(listToSort);
        boolean[][] ret = new boolean[subsetCount][universalSetSize];
        for (int i = 0; i < listToSort.size(); i++) {
            int rowIndex = listToSort.get(i).candIndex;
            ret[i] = input[rowIndex];
        }
        input = ret;
    }

    private void updateSetSizesList() {
        for (int i = 0; i < subsetCount; i++) {
            int count = trueCount(input[i]);
            setToSizeList[i] = count;
        }
    }

    private void trimInput() {
        boolean[] setsToInclude = new boolean[subsetCount];
        int trimmedSubsetCount = subsetCount;
        for (int i = 0; i < subsetCount; i++) {
            setsToInclude[i] = true;
        }
        for (int i = 0; i < subsetCount; i++) {
            for (int j = 0; j < subsetCount; j++) {
                if (i != j && setsToInclude[j] && isSubsetOf(j, i)) {
                    setsToInclude[j] = false;
                    trimmedSubsetCount--;
                }
            }
        }
        boolean[][] trimmedInput = new boolean[trimmedSubsetCount][universalSetSize];
        int rowIndex = 0;
        for (int i = 0; i < subsetCount; i++) {
            if (setsToInclude[i]) {
                System.arraycopy(input[i], 0, trimmedInput[rowIndex], 0, input[i].length);
                rowIndex++;
            }
        }
        subsetCount = trimmedSubsetCount;
        input = trimmedInput;
    }

    public boolean[][] find() {
        partialSol = new int[subsetCount];
        find(0, 0);
        System.out.println("NUM OF CALLS: " + count);
        System.out.println("PRUNED COUNT: " + prunedCount);
        return makeSet(currentCover, currentCover.length);
    }

    private void find(int n, int subsetSum) {
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
                if (currentCover != null && subsetSum + setSizeSums[candidates[i]][currentCover.length - n - 1] < universalSetSize) {
                    prunedCount++;
                    return;
                }
                partialSol[n] = candidates[i];
                makeMove(n);
                subsetSum += setToSizeList[partialSol[n]];
                find(n + 1, subsetSum);
                unmakeMove(n);
                subsetSum -= setToSizeList[partialSol[n]];
                stoppingIndex = getStoppingIndex(candidates.length);
            }
        }
    }

    private int getStoppingIndex(int numCandidates) {
        int stoppingIndex = (currentCover != null) ? currentCover.length - 1 : numCandidates;
        stoppingIndex = (stoppingIndex > numCandidates) ? numCandidates : stoppingIndex;
        return stoppingIndex;
    }

    private boolean isASolution(int n) {
        return (currentCover == null || n < currentCover.length) && isCover1();
    }

    private boolean shouldPrune(int n) {
        boolean ret = false;
        if (currentCover != null && n >= currentCover.length - 1) { // Prune searches which are already >= than current best.
            ret = true;
        } else if (currentCover != null && (getDiff(partialSol, n - 1, n) == 0)) { // Prune if doesn't contain any unique elements.
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
//        return ret;
    }

    private void makeMove(int n) {
        // add variables in partialSol[n] to includedElements array
        int rowIndex = partialSol[n];
        List<Integer> row = sparseInput.get(rowIndex);
        for (int i = 0; i < row.size(); i++) {
            if (includedElements[row.get(i)] == 0) {
                includedCount++;
                boolIncluded[row.get(i)] = true;
            }
            includedElements[row.get(i)]++;
        }
    }

    private void unmakeMove(int n) {
        int rowIndex = partialSol[n];
        List<Integer> row = sparseInput.get(rowIndex);
        for (int i = 0; i < row.size(); i++) {
            includedElements[row.get(i)]--;
            if (includedElements[row.get(i)] == 0) {
                boolIncluded[row.get(i)] = false;
                includedCount--;
            }
        }
    }

    /**
     * Returns the number of unique elements the specified set has in comparison to the union of the partial solution.
     */
    private int getDiff(int[] rows, int pos, int n) {
        int diff = 0;
        unmakeMove(pos);
        boolean[] union = getUnion3();
        makeMove(pos);
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
     */
    private boolean hasSubsetOf(int pos, int n) {
        for (int i = 0; i < n; i++) {
            if (pos != i && isSubsetOf(partialSol[i], partialSol[pos])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the first row is a subset of the second row.
     * (NOT necessarily proper subset). This means the two sets can be identical.
     */
    private boolean isSubsetOf(int firstRow, int secondRow) {
        for (int i = 0; i < universalSetSize; i++) {
            if (input[firstRow][i] && !input[secondRow][i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a set representing the current solution.
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

    private boolean isCover1() {
        return includedCount == universalSetSize;
    }

    private boolean[] getUnion3() {
        boolean[] ret = new boolean[boolIncluded.length];
        System.arraycopy(boolIncluded, 0, ret, 0, boolIncluded.length);
        return ret;
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