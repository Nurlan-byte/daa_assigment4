package daa.search;

import java.util.ArrayList;
import java.util.List;

public class NaiveSearch implements StringSearch {
    private long comparisons;

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern must not be empty");
        }
        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i + m <= n; i++) {
            int j = 0;
            while (j < m) {
                comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                result.add(i);
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
