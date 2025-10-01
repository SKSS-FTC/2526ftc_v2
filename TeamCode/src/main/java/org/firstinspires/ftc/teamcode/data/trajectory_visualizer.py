#!/usr/bin/env python3
"""
trajectory_visualizer.py
Read TinyDB (default: trajectories.json) and produce a 3D interactive visualization.
Outputs an HTML file (default: trajectory_plot.html). Uses plotly if available,
falls back to matplotlib (static) otherwise.

Expected record fields in DB:
- distance (float)
- tangential_speed (float)
- angle_deg (float)
- hit (bool)
"""
import argparse
import datetime
import os
import pandas as pd
import sys
from tinydb import TinyDB


def load_db(db_path):
    if not os.path.exists(db_path):
        print(f"[ERROR] DB file not found: {db_path}")
        sys.exit(1)
    db = TinyDB(db_path)
    recs = db.all()
    if not recs:
        print("[WARN] DB is empty.")
    df = pd.DataFrame(recs)
    # ensure canonical columns
    want = ["distance", "tangential_speed", "angle_deg", "hit", "timestamp"]
    for w in want:
        if w not in df.columns:
            df[w] = None
    df = df.dropna(subset=["distance", "tangential_speed", "angle_deg", "hit"])
    # coerce types
    df["distance"] = df["distance"].astype(float)
    df["tangential_speed"] = df["tangential_speed"].astype(float)
    df["angle_deg"] = df["angle_deg"].astype(float)
    df["hit"] = df["hit"].astype(bool)
    return df


def plot_plotly(df, out_html, show):
    try:
        import plotly.express as px
    except Exception:
        print("[PLOTLY MISSING] falling back to matplotlib")
        return False

    df_plot = df.copy()
    df_plot["hit_label"] = df_plot["hit"].map({True: "hit", False: "miss"})
    color_map = {"hit": "green", "miss": "red"}

    fig = px.scatter_3d(
        df_plot,
        x="distance",
        y="angle_deg",
        z="tangential_speed",
        color="hit_label",
        color_discrete_map=color_map,
        symbol="hit_label",
        hover_data=["timestamp"],
    )
    fig.update_layout(
        scene=dict(
            xaxis_title="Distance (m)",
            yaxis_title="Angle (deg)",
            zaxis_title="Tangential speed (m/s)",
        ),
        title=f"Trajectory outcomes ({len(df_plot)} records)",
    )
    fig.write_html(out_html, include_plotlyjs="cdn")
    print(f"[OUTPUT] wrote {out_html}")
    if show:
        fig.show()
    return True


def plot_matplotlib(df, out_png, show):
    import matplotlib.pyplot as plt
    from mpl_toolkits.mplot3d import Axes3D  # noqa: F401

    fig = plt.figure(figsize=(10, 7))
    ax = fig.add_subplot(111, projection="3d")
    hits = df[df["hit"] == True]
    misses = df[df["hit"] == False]
    ax.scatter(
        hits["distance"],
        hits["angle_deg"],
        hits["tangential_speed"],
        marker="o",
        label="hit",
    )
    ax.scatter(
        misses["distance"],
        misses["angle_deg"],
        misses["tangential_speed"],
        marker="x",
        label="miss",
    )
    ax.set_xlabel("Distance (m)")
    ax.set_ylabel("Angle (deg)")
    ax.set_zlabel("Tangential speed (m/s)")
    ax.legend()
    ax.set_title(f"Trajectory outcomes ({len(df)} records)")
    plt.tight_layout()
    plt.savefig(out_png)
    print(f"[OUTPUT] wrote {out_png}")
    if show:
        plt.show()
    return True


def main():
    p = argparse.ArgumentParser(prog="trajectory_visualizer.py")
    p.add_argument("--db", default="trajectory_db.json", help="TinyDB JSON path")
    p.add_argument(
        "--out",
        default=None,
        help="Output file (html for interactive, png for fallback)",
    )
    p.add_argument(
        "--show", action="store_true", help="Open plot after creation if supported"
    )
    p.add_argument(
        "--min-distance", type=float, default=None, help="Filter min distance (m)"
    )
    p.add_argument(
        "--max-distance", type=float, default=None, help="Filter max distance (m)"
    )
    args = p.parse_args()

    df = load_db(args.db)
    if df.empty:
        print("[EXIT] no usable records")
        return

    if args.min_distance is not None:
        df = df[df["distance"] >= args.min_distance]
    if args.max_distance is not None:
        df = df[df["distance"] <= args.max_distance]

    if args.out is None:
        # default: interactive html if plotly installed, else png
        try:
            import plotly

            out = f"trajectory_plot_{datetime.datetime.utcnow().strftime('%Y%m%dT%H%M%SZ')}.html"
        except Exception:
            out = f"trajectory_plot_{datetime.datetime.utcnow().strftime('%Y%m%dT%H%M%SZ')}.png"
    else:
        out = args.out

    if out.lower().endswith(".html"):
        ok = plot_plotly(df, out, args.show)
        if not ok:
            # fallback to matplotlib png
            fallback_out = out.rsplit(".", 1)[0] + ".png"
            plot_matplotlib(df, fallback_out, args.show)
    else:
        # prefer plotly if available but user requested non-html output
        try:
            import plotly

            ok = plot_plotly(
                df, out if out.lower().endswith(".html") else out + ".html", args.show
            )
            if not ok:
                plot_matplotlib(df, out, args.show)
        except Exception:
            plot_matplotlib(df, out, args.show)


if __name__ == "__main__":
    main()
