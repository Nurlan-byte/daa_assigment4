package daa.search;

import java.util.List;

public interface StringSearch {
    List<Integer> search(String text, String pattern);

    long getComparisons();

    void reset();
}
