package daa.experiment;

import daa.edit.EditDistance;
import daa.edit.RecursiveEditDistance;
import daa.edit.TableEditDistance;
import daa.edit.TwoRowsEditDistance;
import daa.search.KmpSearch;
import daa.search.NaiveSearch;
import daa.search.StringSearch;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class Experiment {
    private static final long SEED = 42;
    private static final int RUNS = 5;
    private static final int PATTERN_LENGTH = 100;

    private static final int[] RECURSIVE_SIZES = {3, 6, 9, 12, 13};
    private static final int[] DP_SIZES = {500, 1000, 2000, 4000};
    private static final int[] SEARCH_SIZES = {100_000, 200_000, 400_000, 800_000, 1_600_000};

    private static final String[] EDIT_INPUTS = {"random", "nomatch", "typo"};
    private static final String[] SEARCH_INPUTS = {"random", "worst"};

    private static PrintWriter out;

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Path.of("results"));
        out = new PrintWriter(new FileWriter("results/results.csv"));
        out.println("algorithm,input,n,m,run,time_ms,steps,memory_cells,comparisons,result");

        runEdit("recursive", new RecursiveEditDistance(), RECURSIVE_SIZES);
        runEdit("table", new TableEditDistance(), DP_SIZES);
        runEdit("two_rows", new TwoRowsEditDistance(), DP_SIZES);

        runSearch("naive", new NaiveSearch());
        runSearch("kmp", new KmpSearch());

        out.close();
        System.out.println("Saved results/results.csv");
    }

    private static void runEdit(String name, EditDistance algo, int[] sizes) {
        for (String input : EDIT_INPUTS) {
            for (int n : sizes) {
                InputGenerator gen = new InputGenerator(SEED + n);
                String a;
                String b;
                if (input.equals("random")) {
                    a = gen.randomString(n, InputGenerator.LETTERS);
                    b = gen.randomString(n, InputGenerator.LETTERS);
                } else if (input.equals("nomatch")) {
                    a = gen.repeat('a', n);
                    b = gen.repeat('b', n);
                } else {
                    a = gen.randomString(n, InputGenerator.LETTERS);
                    b = gen.withOneTypo(a);
                }

                algo.distance(a, b);
                for (int run = 1; run <= RUNS; run++) {
                    algo.reset();
                    long start = System.nanoTime();
                    int d = algo.distance(a, b);
                    double ms = (System.nanoTime() - start) / 1_000_000.0;
                    out.printf(Locale.US, "%s,%s,%d,%d,%d,%.4f,%d,%d,0,%d%n",
                            name, input, n, n, run, ms, algo.getSteps(), algo.getMemoryCells(), d);
                }
                System.out.println(name + " " + input + " n=" + n + " done");
            }
        }
    }

    private static void runSearch(String name, StringSearch algo) {
        for (String input : SEARCH_INPUTS) {
            for (int n : SEARCH_SIZES) {
                InputGenerator gen = new InputGenerator(SEED + n);
                String text;
                String pattern;
                if (input.equals("random")) {
                    text = gen.randomString(n, InputGenerator.LETTERS);
                    pattern = gen.randomString(PATTERN_LENGTH, InputGenerator.LETTERS);
                } else {
                    text = gen.repeat('a', n);
                    pattern = gen.repeat('a', PATTERN_LENGTH - 1) + "b";
                }

                algo.search(text, pattern);
                for (int run = 1; run <= RUNS; run++) {
                    algo.reset();
                    long start = System.nanoTime();
                    List<Integer> matches = algo.search(text, pattern);
                    double ms = (System.nanoTime() - start) / 1_000_000.0;
                    out.printf(Locale.US, "%s,%s,%d,%d,%d,%.4f,0,0,%d,%d%n",
                            name, input, n, PATTERN_LENGTH, run, ms, algo.getComparisons(), matches.size());
                }
                System.out.println(name + " " + input + " n=" + n + " done");
            }
        }
    }
}
