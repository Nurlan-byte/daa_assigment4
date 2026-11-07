# Assignment 4 Report: Dynamic Programming and String Algorithms

Design and Analysis of Algorithms. Yussupov Nurlan, group SE-2526. Instructor: Taubakabyl Nurlybek. November 2026.

## 1. Experiment Design Card

Written before the first run.

| Field | Edit Distance | String Search |
|---|---|---|
| **Question** | Does the recursive version really grow exponentially, and do the table and two-row versions grow as n·m with the same work but different memory? | Does Naive really do about n·m comparisons on a bad input while KMP stays linear (at most 2n)? |
| **Sizes (n)** | Recursive: 3, 6, 9, 12 (small, because the cost explodes). Table and two rows: 500, 1000, 2000, 4000 (each step doubles n, so n² should grow 4x). Both strings have length n. | Text length: 100 000, 200 000, 400 000, 800 000, 1 600 000 (16x range). Pattern length m = 100 for all runs, so only n changes. |
| **Inputs** | `random`: two random strings over a–z, the average case. `nomatch`: "aaa…a" vs "bbb…b", no letter ever matches, so the recursion always splits into 3 calls (worst case for recursion). `typo`: a random string and the same string with one letter changed, like "Abylai Han" vs "Abylai Khan" (best case for recursion, realistic for the city platform). | `random`: random text and random pattern over a–z, almost every first comparison fails (the usual case). `worst`: text "aaa…a" and pattern "aa…ab" (99 a's and one b): Naive matches 99 letters at every position before it fails (worst case for Naive). |
| **Metrics** | `steps`: recursive calls (recursive) or filled cells (table, two rows), counted inside the code; this is the unit of work in the DP. `memory_cells`: int cells allocated by the version, counted when the arrays are created. `time_ms`: median of 5 runs after 1 warm-up run. | `comparisons`: every character comparison, in the search and in the LPS build, counted inside the code. `time_ms`: median of 5 runs after 1 warm-up run. |
| **Prediction** | Recursive on `nomatch`: about ×5–6 calls per extra letter (straight line on a log axis); `typo` stays small because matching letters go straight down the diagonal. Table and two rows: steps = n² exactly for every input, time ×4 when n doubles. Two rows uses about n/2 times less memory. | Naive `random`: ≈ n comparisons. Naive `worst`: ≈ n·m = 100n. KMP: between n and 2n for both inputs. On the worst input Naive should be many times slower in time too. |
| **Plots** | (1) Recursive calls vs n, log y axis: is the growth exponential? (2) Time vs n for table and two rows: is it n² and which is faster? (3) Allocated cells vs n, log y: how much memory do two rows save? | (4) Comparisons / n vs n (a ratio, log axes): does each algorithm do a constant amount of work per character, and how big is that constant? (5) Time vs n, log axes: does the clock agree with the counters? |

Reproducibility: `InputGenerator` uses `java.util.Random` with seed `42 + n`, so the same command gives the same strings and the same counters. Only the times change a little between runs.
