package daa.experiment;

import java.util.Random;

public class InputGenerator {
    public static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private final Random random;

    public InputGenerator(long seed) {
        random = new Random(seed);
    }

    public String randomString(int length, String alphabet) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    public String repeat(char c, int length) {
        return String.valueOf(c).repeat(length);
    }

    public String withOneTypo(String s) {
        if (s.isEmpty()) {
            return s;
        }
        char[] chars = s.toCharArray();
        int pos = random.nextInt(chars.length);
        chars[pos] = chars[pos] == 'z' ? 'a' : (char) (chars[pos] + 1);
        return new String(chars);
    }
}
