package daa.edit;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EditDistanceTest {
    private final List<EditDistance> versions = List.of(
            new RecursiveEditDistance(),
            new TableEditDistance(),
            new TwoRowsEditDistance()
    );

    @Test
    void referenceValues() {
        for (EditDistance ed : versions) {
            assertEquals(3, ed.distance("kitten", "sitting"));
            assertEquals(2, ed.distance("flaw", "lawn"));
            assertEquals(5, ed.distance("intention", "execution"));
            assertEquals(3, ed.distance("", "abc"));
            assertEquals(0, ed.distance("abc", "abc"));
        }
    }

    @Test
    void emptyStrings() {
        for (EditDistance ed : versions) {
            assertEquals(0, ed.distance("", ""));
            assertEquals(3, ed.distance("abc", ""));
            assertEquals(4, ed.distance("", "abcd"));
        }
    }

    @Test
    void streetNameTypo() {
        for (EditDistance ed : versions) {
            assertEquals(2, ed.distance("Abylai Han", "Abylai Khan"));
        }
    }

    @Test
    void allVersionsAgreeOnRandomStrings() {
        Random random = new Random(7);
        for (int t = 0; t < 100; t++) {
            String a = randomString(random, random.nextInt(9));
            String b = randomString(random, random.nextInt(9));
            int expected = versions.get(1).distance(a, b);
            for (EditDistance ed : versions) {
                assertEquals(expected, ed.distance(a, b), "a=" + a + " b=" + b);
            }
        }
    }

    private String randomString(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(3)));
        }
        return sb.toString();
    }
}
