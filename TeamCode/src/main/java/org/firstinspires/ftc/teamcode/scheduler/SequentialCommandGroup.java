package org.firstinspires.ftc.teamcode.scheduler;

import java.util.ArrayList;
import java.util.List;

public class SequentialCommandGroup implements Command {

    private List<Command> commands = new ArrayList<>();
    private int currentIndex = 0; // which command is active

    public SequentialCommandGroup(Command... cmds) {
        for (Command c : cmds) {
            commands.add(c);
        }
    }

    @Override
    public void start() {
        if (!commands.isEmpty()) {
            commands.get(0).start(); // start the first command
        }
    }

    @Override
    public void update() {
        if (currentIndex >= commands.size()) return; // nothing left to do

        Command current = commands.get(currentIndex);
        current.update();

        if (current.isFinished()) {
            current.end();
            currentIndex++;

            // start the next one, if it exists
            if (currentIndex < commands.size()) {
                commands.get(currentIndex).start();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return currentIndex >= commands.size();
    }

    @Override
    public void end() {
        // cleanup: make sure all remaining commands are ended
        while (currentIndex < commands.size()) {
            commands.get(currentIndex).end();
            currentIndex++;
        }
    }
}
