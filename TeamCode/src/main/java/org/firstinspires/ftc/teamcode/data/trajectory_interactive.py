#!/usr/bin/env python3
"""
interactive_trajectory_counter.py

Fully interactive session-based shot analyzer.
- Reads TinyDB (default: trajectory_db.json)
- Prompts user for session gap threshold, weighting scheme
- Computes weighted hit/miss stats
- Generates interactive visualizations with Plotly

Requirements:
pip install tinydb pandas plotly
"""

import os
import pandas as pd
import sys
from tinydb import TinyDB


def load_db(db_path: str) -> pd.DataFrame:
    if not os.path.exists(db_path):
        print(f"[ERROR] DB file not found: {db_path}")
        sys.exit(1)
    db = TinyDB(db_path)
    recs = db.all()
    if not recs:
        print("[WARN] DB is empty.")
    df = pd.DataFrame(recs)
    if "hit" not in df.columns or "timestamp" not in df.columns:
        print("[ERROR] DB missing required fields: hit, timestamp")
        sys.exit(1)
    df = df.dropna(subset=["hit", "timestamp"])
    df["hit"] = df["hit"].astype(bool)
    df["timestamp"] = pd.to_datetime(df["timestamp"], errors="coerce")
    df = df.dropna(subset=["timestamp"])
    df = df.sort_values("timestamp").reset_index(drop=True)
    return df


def group_sessions(df: pd.DataFrame, gap_minutes: float) -> pd.DataFrame:
    gap = pd.Timedelta(minutes=gap_minutes)
    df["prev_time"] = df["timestamp"].shift(1)
    df["gap"] = df["timestamp"] - df["prev_time"]
    df["new_session"] = (df["gap"] > gap) | (df["gap"].isna())
    df["session_id"] = df["new_session"].cumsum()
    return df


def apply_weights(df: pd.DataFrame, max_weight: float) -> pd.DataFrame:
    """Assign linear weights from 1 to max_weight per session."""

    def weight_series(subdf: pd.DataFrame) -> pd.Series:
        n = len(subdf)
        if n == 1:
            return pd.Series([1.0], index=subdf.index)
        return pd.Series(
            [1.0 + i * (max_weight - 1.0) / (n - 1) for i in range(n)],
            index=subdf.index,
        )

    df["weight"] = df.groupby("session_id", group_keys=False).apply(weight_series)
    return df


def summarize(df: pd.DataFrame) -> pd.DataFrame:
    weighted = df.groupby("session_id").apply(
        lambda g: pd.Series(
            {
                "total_weight": g["weight"].sum(),
                "hits_weighted": (g["hit"] * g["weight"]).sum(),
                "shots": len(g),
            }
        )
    )
    weighted["misses_weighted"] = weighted["total_weight"] - weighted["hits_weighted"]
    weighted["hit_pct"] = 100 * weighted["hits_weighted"] / weighted["total_weight"]
    weighted["miss_pct"] = 100 * weighted["misses_weighted"] / weighted["total_weight"]
    weighted = weighted.reset_index()
    return weighted


import pandas as pd
import plotly.graph_objects as go
import plotly.express as px


def interactive_plot(df: pd.DataFrame, summary: pd.DataFrame):
    fig = go.Figure()

    # Compute session rank for color scaling
    session_acc = df.groupby("session_id")["hit"].mean().reset_index()
    session_acc["rank"] = session_acc["hit"].rank(method="dense", ascending=False)
    session_acc["rank_norm"] = (
        (session_acc["rank"] - 1) / (session_acc["rank"].max() - 1)
        if session_acc["rank"].max() > 1
        else 0
    )

    colorscale = px.colors.diverging.PiYG

    def rank_to_color(r):
        idx = int(r * (len(colorscale) - 1))
        return colorscale[idx]

    rank_color_map = {
        row["session_id"]: rank_to_color(row["rank_norm"])
        for _, row in session_acc.iterrows()
    }

    # Bars: color-scaled by session rank
    for _, row in summary.iterrows():
        fig.add_trace(
            go.Bar(
                x=[row["session_id"]],
                y=[row["hit_pct"]],
                width=0.4,
                name=f"Session {int(row['session_id'])} Hit %",
                opacity=0.6,
                marker_color=rank_color_map.get(row["session_id"], "gray"),
            )
        )

    # Scatter: red/green by hit
    df["color"] = df["hit"].map({True: "green", False: "red"})
    df["shot_idx"] = df.groupby("session_id").cumcount() + 1

    fig.add_trace(
        go.Scatter(
            x=df["session_id"],
            y=df["shot_idx"],
            mode="markers",
            marker=dict(
                color=df["color"],  # red/green hit/miss
                size=df["weight"] * 5,
                line=dict(width=1, color="black"),
            ),
            text=[
                f"Hit: {h}, Weight: {w:.2f}" for h, w in zip(df["hit"], df["weight"])
            ],
            hoverinfo="text",
            name="Shots",
        )
    )

    fig.update_layout(
        xaxis_title="Prototype",
        yaxis_title="Accuracy / Shot Index",
        title="Per-Prototype Accuracy with Hit/Miss Scatter",
        showlegend=True,
    )

    fig.show()


def main():
    db_path = (
        input("Enter TinyDB path (default: trajectory_db.json): ").strip()
        or "trajectory_db.json"
    )
    gap_min = input("Enter session gap in minutes (default: 3): ").strip()
    gap_min = float(gap_min) if gap_min else 3.0
    max_weight = input("Enter max weight for last shot (default: 2.0): ").strip()
    max_weight = float(max_weight) if max_weight else 2.0

    df = load_db(db_path)
    if df.empty:
        print("[EXIT] No usable records.")
        return

    df = group_sessions(df, gap_min)
    df = apply_weights(df, max_weight)
    summary = summarize(df)
    print(summary.to_string(index=False))

    interactive_plot(df, summary)


if __name__ == "__main__":
    main()
