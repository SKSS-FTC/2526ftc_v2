#!/usr/bin/env python3
"""
manual_trajectory_logger.py
An interactive command-line tool to manually log projectile trajectory data.

This script prompts the user for key physical parameters and allows them to
log successful ('In') or missed ('Out') shots. It calculates the final
distance, speed, and angle values and saves them to a TinyDB database.
"""
import datetime
import math
import os
import sys
from tinydb import TinyDB

# CONFIG
DEFAULT_DB = "trajectory_db.json"


def save_trajectory(distance, tangential_speed, angle_deg, hit, db_path=DEFAULT_DB):
    """Validate inputs, normalize, and save a record to TinyDB."""
    # Simple validation to ensure we don't save incomplete data
    if not all(
        isinstance(v, (int, float)) for v in [distance, tangential_speed, angle_deg]
    ):
        print("[SAVE ERROR] Invalid numeric data for calculation.")
        return None
    if not isinstance(hit, bool):
        print("[SAVE ERROR] Invalid 'hit' status.")
        return None

    rec = {
        "distance": float(distance),
        "tangential_speed": float(tangential_speed),
        "angle_deg": float(angle_deg),
        "hit": hit,
        "timestamp": datetime.datetime.utcnow().isoformat() + "Z",
    }

    db = TinyDB(db_path)
    doc_id = db.insert(rec)
    print(
        f"\n[SAVED] doc_id={doc_id}: dist={rec['distance']:.2f}m, "
        f"speed={rec['tangential_speed']:.2f}m/s, "
        f"angle={rec['angle_deg']:.2f}°, hit={rec['hit']}"
    )
    return doc_id


def get_float_input(prompt):
    """Robustly gets a float value from the user."""
    while True:
        try:
            return float(input(prompt))
        except ValueError:
            print("Invalid input. Please enter a number.")


def clear_screen():
    """Clears the console screen."""
    os.system("cls" if os.name == "nt" else "clear")


def calculate_tangential_speed(rpm, wheel_diameter_m):
    """Calculates tangential speed (m/s) from RPM and wheel diameter (m)."""
    # Formula: v = ω * r = (RPM * 2π / 60) * (diameter / 2)
    # Simplified: v = π * diameter * RPM / 60
    return (math.pi * wheel_diameter_m * rpm) / 60.0


def calculate_geometry(launcher_depth_m, height_difference_m):
    """Calculates hypotenuse distance (m) and launch angle (degrees)."""
    # Use Pythagorean theorem for the direct distance to the goal
    distance_m = math.sqrt(launcher_depth_m**2 + height_difference_m**2)

    # Use arctangent to find the angle
    # angle = atan(opposite / adjacent)
    angle_rad = math.atan2(height_difference_m, launcher_depth_m)
    angle_deg = math.degrees(angle_rad)

    return distance_m, angle_deg


def main():
    """Main program loop for data entry."""
    clear_screen()
    print("--- Manual Trajectory Data Collector ---")
    print("First, let's set up the initial parameters.")

    # --- Initial One-Time Setup ---
    wheel_diameter_m = get_float_input("Enter shooter wheel diameter (meters): ")
    motor_max_rpm = get_float_input("Enter motor's max theoretical RPM: ")
    launcher_height_m = get_float_input("Enter launcher height from ground (meters): ")
    goal_height_m = get_float_input("Enter goal height from ground (meters): ")

    # This difference is constant for the session
    height_difference_m = goal_height_m - launcher_height_m

    # --- Default Loop Variables ---
    # These can be changed during the loop
    commanded_speed_ratio = 0.8  # Default to 80% speed
    launcher_depth_m = 2.0  # Default to 2 meters from goal

    while True:
        clear_screen()

        # --- Calculate current state from variables ---
        current_rpm = motor_max_rpm * commanded_speed_ratio

        tangential_speed_ms = calculate_tangential_speed(current_rpm, wheel_diameter_m)

        distance_m, angle_deg = calculate_geometry(
            launcher_depth_m, height_difference_m
        )

        # --- Display current state ---
        print("--- CURRENT TRAJECTORY PARAMETERS ---")
        print(
            f"  [S] Motor Commanded Speed: {commanded_speed_ratio:.2f} ({current_rpm:.0f} RPM)"
        )
        print(f"  [D] Launcher Depth from Goal: {launcher_depth_m:.2f} meters")
        print("---------------------------------------")
        print("           CALCULATED VALUES")
        print(f"           Tangential Speed: {tangential_speed_ms:.2f} m/s")
        print(f"           Distance to Goal: {distance_m:.2f} m")
        print(f"           Launch Angle:     {angle_deg:.2f}°")
        print("---------------------------------------")

        # --- Get user action ---
        print("ACTIONS: [I]n (Goal), [O]ut (Miss), [S]et Speed, [D]et Depth, [Q]uit")
        action = input("Enter your action: ").lower().strip()

        if action == "i" or action == "o":
            was_hit = action == "i"
            save_trajectory(distance_m, tangential_speed_ms, angle_deg, was_hit)
            input("\nRecord saved. Press Enter to continue...")

        elif action == "s":
            print("\nEnter new commanded speed (e.g., 0.75 for 75%).")
            new_speed = get_float_input("New speed [0.0 to 1.0]: ")
            if 0.0 <= new_speed <= 1.0:
                commanded_speed_ratio = new_speed
            else:
                print("Value out of range. Keeping previous value.")
                input("Press Enter to continue...")

        elif action == "d":
            print("\nEnter new launcher depth from the goal.")
            launcher_depth_m = get_float_input("New depth (meters): ")

        elif action == "q":
            print("Exiting program.")
            break

        else:
            print(f"Unknown command: '{action}'")
            input("Press Enter to continue...")


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\n[STOP] Interrupted by user. Exiting.")
        sys.exit(0)
