package org.firstinspires.ftc.teamcode.scheduler;

import java.util.ArrayList;
import java.util.List;

public class ParallelCommandGroup implements Command {
    private List<Command> commands = new ArrayList<>();

    public ParallelCommandGroup(Command... cmds) {
        for (Command c : cmds) {
            commands.add(c);
        }
    }

    @Override
    public void start() {
        for (Command c : commands) {
            c.start();
        }
    }

    @Override
    public void update() {
        // Each child still gets its own update() tick!
        for (Command c : commands) {
            if (!c.isFinished()) {
                c.update();
            }
        }
    }

    @Override
    public boolean isFinished() {
        // finish when ALL children are finished
        for (Command c : commands) {
            if (!c.isFinished()) return false;
        }
        return true;
    }

    @Override
    public void end() {
        for (Command c : commands) {
            c.end();
        }
    }
}