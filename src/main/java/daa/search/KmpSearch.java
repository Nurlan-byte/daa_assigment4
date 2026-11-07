package daa.search;

public class KmpSearch {
    private long comparisons;

    public int[] lps(String p) {
        int[] lps = new int[p.length()];
        int len = 0;
        int i = 1;

        while (i < p.length()) {
            comparisons++;
            if (p.charAt(i) == p.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len > 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
        return lps;
    }

    public long getComparisons() {
        return comparisons;
    }

    public void reset() {
        comparisons = 0;
    }
}
