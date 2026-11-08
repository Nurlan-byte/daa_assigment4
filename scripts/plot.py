import csv
import os
import statistics
from collections import defaultdict

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt

CSV_PATH = "results/results.csv"
PLOT_DIR = "results/plots"
COLORS = ["#2a78d6", "#eb6834", "#1baf7a", "#eda100"]
MARKERS = ["o", "s", "^", "D"]
STYLES = ["-", "--", "-.", ":"]


def load():
    times = defaultdict(list)
    rows = {}
    with open(CSV_PATH) as f:
        for r in csv.DictReader(f):
            key = (r["algorithm"], r["input"], int(r["n"]))
            times[key].append(float(r["time_ms"]))
            rows[key] = r
    data = {}
    for key, values in times.items():
        r = rows[key]
        data[key] = {
            "time": statistics.median(values),
            "steps": int(r["steps"]),
            "memory": int(r["memory_cells"]),
            "comparisons": int(r["comparisons"]),
        }
    return data


def series(data, algorithm, input_type, field):
    keys = sorted(k for k in data if k[0] == algorithm and k[1] == input_type)
    xs = [k[2] for k in keys]
    ys = [data[k][field] for k in keys]
    return xs, ys


def draw(filename, title, xlabel, ylabel, lines, takeaway, log_y=False, log_x=False):
    fig, ax = plt.subplots(figsize=(8, 5.2))
    for i, (label, xs, ys) in enumerate(lines):
        ax.plot(xs, ys, color=COLORS[i], marker=MARKERS[i], linestyle=STYLES[i],
                linewidth=2, markersize=7, label=label)
    if log_y:
        ax.set_yscale("log")
    if log_x:
        ax.set_xscale("log")
    ax.set_title(title, fontsize=13, loc="left")
    ax.set_xlabel(xlabel)
    ax.set_ylabel(ylabel)
    ax.grid(True, color="#e5e5e5", linewidth=0.8)
    for side in ("top", "right"):
        ax.spines[side].set_visible(False)
    ax.legend(frameon=False)
    fig.text(0.01, 0.01, takeaway, fontsize=10, color="#333333", wrap=True)
    fig.tight_layout(rect=(0, 0.06, 1, 1))
    fig.savefig(os.path.join(PLOT_DIR, filename), dpi=130)
    plt.close(fig)


def main():
    os.makedirs(PLOT_DIR, exist_ok=True)
    data = load()

    lines = [(f"recursive, {t}",) + series(data, "recursive", t, "steps")
             for t in ["nomatch", "random", "typo"]]
    draw("1_edit_recursive_steps.png",
         "Recursive Edit Distance: calls vs string length",
         "string length n (both strings)", "recursive calls (log scale)", lines,
         "Takeaway: with no matching letters the calls grow about 5.6x per extra letter; one typo stays tiny.",
         log_y=True)

    lines = [("table, nomatch",) + series(data, "table", "nomatch", "time"),
             ("two rows, nomatch",) + series(data, "two_rows", "nomatch", "time"),
             ("table, random",) + series(data, "table", "random", "time"),
             ("two rows, random",) + series(data, "two_rows", "random", "time")]
    draw("2_edit_dp_time.png",
         "Table vs two rows: time vs string length",
         "string length n (both strings)", "median time of 5 runs (ms)", lines,
         "Takeaway: doubling n makes both versions roughly 4x slower (n^2); two rows is as fast or faster than the table.")

    lines = [("table",) + series(data, "table", "random", "memory"),
             ("two rows",) + series(data, "two_rows", "random", "memory")]
    draw("3_edit_memory_cells.png",
         "Table vs two rows: allocated int cells",
         "string length n (both strings)", "allocated int cells (log scale)", lines,
         "Takeaway: at n = 4000 the table keeps 16 million cells, two rows keep 8 thousand (2000x less).",
         log_y=True)

    lines = []
    for algo, name in [("naive", "naive"), ("kmp", "KMP")]:
        for t in ["worst", "random"]:
            xs, ys = series(data, algo, t, "comparisons")
            lines.append((f"{name}, {t}", xs, [y / x for x, y in zip(xs, ys)]))
    draw("4_search_comparisons_ratio.png",
         "String search: comparisons per text character (m = 100)",
         "text length n (characters)", "comparisons / n (log scale)", lines,
         "Takeaway: on 'aaa...ab' naive does ~100 comparisons per character, KMP never more than 2.",
         log_y=True, log_x=True)

    lines = []
    for algo, name in [("naive", "naive"), ("kmp", "KMP")]:
        for t in ["worst", "random"]:
            lines.append((f"{name}, {t}",) + series(data, algo, t, "time"))
    draw("5_search_time.png",
         "String search: time vs text length (m = 100)",
         "text length n (characters)", "median time of 5 runs (ms, log scale)", lines,
         "Takeaway: on random text both take a few ms; on the worst case naive is ~35x slower than KMP.",
         log_y=True, log_x=True)

    print("Saved plots to " + PLOT_DIR)


if __name__ == "__main__":
    main()
