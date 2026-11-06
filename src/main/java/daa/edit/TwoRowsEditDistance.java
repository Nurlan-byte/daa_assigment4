package daa.edit;

// State: prev[j] = distance for row i-1, curr[j] = distance for row i (first i chars of a, first j chars of b).
// Transition: curr[j] = prev[j-1] if a[i-1] == b[j-1], else 1 + min(prev[j], curr[j-1], prev[j-1]).
// Base case: prev[j] = j for row 0, and curr[0] = i for every row i.
public class TwoRowsEditDistance implements EditDistance {
    private long steps;
    private long memoryCells;

    @Override
    public int distance(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        memoryCells += 2L * (m + 1);

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                steps++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(prev[j], Math.min(curr[j - 1], prev[j - 1]));
                }
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        return prev[m];
    }

    @Override
    public long getSteps() {
        return steps;
    }

    @Override
    public long getMemoryCells() {
        return memoryCells;
    }

    @Override
    public void reset() {
        steps = 0;
        memoryCells = 0;
    }
}
