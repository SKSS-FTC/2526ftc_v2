package org.firstinspires.ftc.teamcode.scheduler;

import java.util.HashMap;
import java.util.Objects;

public class SwitchCommandGroup implements Command {
    private final HashMap<String, Command> commands = new HashMap<>();
    private String nameCommand;
    private String lastCommand;
    private final Command defaultCommand;

    // Constructor with nameCommand
    @SafeVarargs
    public SwitchCommandGroup(String nameCommand, Command defaultCommand, HashMap<String, Command>... commandGroups) {
        this.nameCommand = (nameCommand != null) ? nameCommand : "NaN";
        this.defaultCommand = defaultCommand;

        for (HashMap<String, Command> group : commandGroups) {
            commands.putAll(group);
        }
    }

    // Constructor without nameCommand (defaults to "NaN")
    @SafeVarargs
    public SwitchCommandGroup(Command defaultCommand, HashMap<String, Command>... commandGroups) {
        this("NaN", defaultCommand, commandGroups);
    }

    public void setCommand(String name) {
        this.nameCommand = (name != null && commands.containsKey(name)) ? name : "NaN";
    }

    @Override
    public void start() {
        lastCommand = nameCommand;
        if ("NaN".equals(nameCommand)) {
            defaultCommand.start();
        } else {
            commands.get(nameCommand).start();
        }
    }

    @Override
    public void update() {
        if (!Objects.equals(nameCommand, lastCommand)) {
            if ("NaN".equals(lastCommand)) {
                defaultCommand.end();
            } else {
                commands.get(lastCommand).end();
            }

            if ("NaN".equals(nameCommand)) {
                defaultCommand.start();
            } else {
                commands.get(nameCommand).start();
            }

            lastCommand = nameCommand;
        }

        if ("NaN".equals(nameCommand)) {
            defaultCommand.update();
        } else {
            commands.get(nameCommand).update();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        if ("NaN".equals(lastCommand)) {
            defaultCommand.end();
        } else {
            commands.get(lastCommand).end();
        }
    }
}
