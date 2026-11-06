package daa.edit;

// State: dp[i][j] = edit distance between the first i chars of a and the first j chars of b.
// Transition: dp[i][j] = dp[i-1][j-1] if a[i-1] == b[j-1], else 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]).
// Base case: dp[0][j] = j and dp[i][0] = i.
public class TableEditDistance implements EditDistance {
    private long steps;
    private long memoryCells;

    @Override
    public int distance(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        memoryCells += (long) (n + 1) * (m + 1);

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                steps++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        return dp[n][m];
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
