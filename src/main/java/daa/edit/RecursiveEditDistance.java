package daa.edit;

// State: d(i, j) = edit distance between the first i chars of a and the first j chars of b.
// Transition: if a[i-1] == b[j-1] then d(i-1, j-1), else 1 + min(d(i-1, j), d(i, j-1), d(i-1, j-1)).
// Base case: d(0, j) = j and d(i, 0) = i.
public class RecursiveEditDistance implements EditDistance {
    private long steps;

    @Override
    public int distance(String a, String b) {
        return solve(a, b, a.length(), b.length());
    }

    private int solve(String a, String b, int i, int j) {
        steps++;
        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return solve(a, b, i - 1, j - 1);
        }
        int delete = solve(a, b, i - 1, j);
        int insert = solve(a, b, i, j - 1);
        int replace = solve(a, b, i - 1, j - 1);
        return 1 + Math.min(delete, Math.min(insert, replace));
    }

    @Override
    public long getSteps() {
        return steps;
    }

    @Override
    public long getMemoryCells() {
        return 0;
    }

    @Override
    public void reset() {
        steps = 0;
    }
}
