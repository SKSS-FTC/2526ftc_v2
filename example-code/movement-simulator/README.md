# Movement Simulator

## Overview

The Movement Simulator shows how robot wheels can be steered and rotated when all four wheels are driving and independently steer.

## How to Build

To build, just run the `./build.sh` script from a command line. That will generate all Java `.class` files in a `bin/` directory.

## How to Run

When you execute the `./build.sh` script, the program will automatically run.

If you want to run the program without first building, run the following command from the Movement Simulator root directory:

```java
java -cp bin Main
```

## How to Use

### Turn wheels and adjust speed

Use `arrow` keys to steer robot wheels and increase/decrease speed.

### Use field-oriented drive

`W`, `A`, `S`, and `D` keys will support this, this feature is not included yet.

### Pivot entire robot

Use the `1` or `2` keys to pivot the robot, this will be useful when the field-oriented drive feature is available.
