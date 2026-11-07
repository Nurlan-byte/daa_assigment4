package daa.search;

import java.util.ArrayList;
import java.util.List;

public class KmpSearch implements StringSearch {
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

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }
        List<Integer> result = new ArrayList<>();
        if (pattern.length() > text.length()) {
            return result;
        }

        int[] lps = lps(pattern);
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    result.add(i - j);
                    j = lps[j - 1];
                }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return result;
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    @Override
    public void reset() {
        comparisons = 0;
    }
}
