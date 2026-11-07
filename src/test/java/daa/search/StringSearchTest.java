package daa.search;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringSearchTest {
    private final List<StringSearch> searches = List.of(new NaiveSearch(), new KmpSearch());

    @Test
    void lpsReferenceArrays() {
        KmpSearch kmp = new KmpSearch();
        assertArrayEquals(new int[]{0, 1, 2, 0, 1, 2, 3, 3}, kmp.lps("AAACAAAA"));
        assertArrayEquals(new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4}, kmp.lps("ABABCABAB"));
    }

    @Test
    void referenceMatches() {
        for (StringSearch s : searches) {
            assertEquals(List.of(10), s.search("ABABDABACDABABCABAB", "ABABCABAB"));
            assertEquals(List.of(0, 1, 2, 3), s.search("AAAAA", "AA"));
            assertEquals(List.of(0, 2, 4), s.search("ABABABA", "ABA"));
        }
    }

    @Test
    void noMatchReturnsEmptyList() {
        for (StringSearch s : searches) {
            assertTrue(s.search("Abylai Khan", "Satpayev").isEmpty());
        }
    }

    @Test
    void emptyPatternThrows() {
        for (StringSearch s : searches) {
            assertThrows(IllegalArgumentException.class, () -> s.search("abc", ""));
        }
    }

    @Test
    void patternLongerThanTextReturnsEmptyList() {
        for (StringSearch s : searches) {
            assertTrue(s.search("ab", "abc").isEmpty());
        }
    }

    @Test
    void naiveAndKmpAgreeOnRandomTexts() {
        Random random = new Random(11);
        NaiveSearch naive = new NaiveSearch();
        KmpSearch kmp = new KmpSearch();
        for (int t = 0; t < 100; t++) {
            String text = randomString(random, random.nextInt(50));
            String pattern = randomString(random, 1 + random.nextInt(4));
            assertEquals(naive.search(text, pattern), kmp.search(text, pattern),
                    "text=" + text + " pattern=" + pattern);
        }
    }

    private String randomString(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(2)));
        }
        return sb.toString();
    }
}
