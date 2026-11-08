# DAA Assignment 4: Dynamic Programming and String Algorithms

City Search & Spell-Check: Edit Distance (recursive, table, two rows) and string search (Naive, KMP) with counters, a seeded experiment and plots.

Yussupov Nurlan, group SE-2526.

## Structure

```
src/main/java/daa/edit/         EditDistance interface, RecursiveEditDistance, TableEditDistance, TwoRowsEditDistance
src/main/java/daa/search/       StringSearch interface, NaiveSearch, KmpSearch (public lps)
src/main/java/daa/experiment/   InputGenerator (fixed seed), Experiment (writes results.csv)
src/test/java/                  JUnit 5 tests
scripts/plot.py                 draws the plots from results.csv
results/results.csv             all runs of the experiment
results/plots/                  PNG plots
REPORT.md                       Experiment Design Card, plots, prediction vs result, short answers
```

## Counters

| Family | Counter | What it counts |
|---|---|---|
| Edit Distance | `steps` | recursive calls (recursive) or filled cells (table, two rows) |
| Edit Distance | `memory_cells` | int cells allocated for the table or the two rows |
| String search | `comparisons` | character comparisons (KMP includes the LPS build) |

Time is the median of 5 runs after 1 warm-up run.

## Requirements

- Java 17 or newer
- Maven 3.8 or newer
- Python 3 with matplotlib (only for the plots): `pip install matplotlib`

## Build and run the tests

```
mvn test
```

## Run the experiment

```
mvn compile
java -cp target/classes daa.experiment.Experiment
```

It takes about 1.5 minutes and writes `results/results.csv` (columns: `algorithm,input,n,m,run,time_ms,steps,memory_cells,comparisons,result`). The seed is fixed, so the counters are the same on every run.

## Draw the plots

```
python scripts/plot.py
```

The plots are saved to `results/plots/`.
