package org.nknsd.teamcode.components.utility;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.nknsd.teamcode.frameworks.NKNComponent;
import org.nknsd.teamcode.helperClasses.AdvancedTelemetry;
import org.nknsd.teamcode.helperClasses.EventPair;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GamePadHandler implements NKNComponent {
    // TreeMap of the button to list for, and the event to trigger based on that
    // The key is the button + the name of the event
    private final ArrayList<EventPair> eventListeners = new ArrayList<EventPair>();
    private Telemetry telemetry;
    private AdvancedTelemetry advancedTelemetry;
    private Gamepad gamePad1;
    private Gamepad gamePad2;


    private void iterateListeners() {
        for (EventPair eventListener : eventListeners) {
            try {
                if (eventListener.listener.call()) {
                    eventListener.event.run();
                    advancedTelemetry.addData("Event run", eventListener.name);
                }
            } catch (Exception e) {
                telemetry.addData("Caught an exception!! REALLY BAD!! GET DILLON!! Event Name", eventListener.name);
                telemetry.addData("Error", e);
            }
        }

        telemetry.update();
    }

    @Override
    public boolean init(Telemetry telemetry, HardwareMap hardwareMap, Gamepad gamePad1, Gamepad gamePad2) {
        this.gamePad1 = gamePad1;
        this.gamePad2 = gamePad2;
        this.telemetry = telemetry;
        advancedTelemetry = new AdvancedTelemetry(telemetry);

        return true;
    }

    @Override
    public void init_loop(ElapsedTime runtime, Telemetry telemetry) {
        iterateListeners();
    }

    @Override
    public void start(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public void stop(ElapsedTime runtime, Telemetry telemetry) {

    }

    @Override
    public String getName() {
        return "GamePadHandler";
    }

    @Override
    public void loop(ElapsedTime runtime, Telemetry telemetry) {
        iterateListeners();
    }

    @Override
    public void doTelemetry(Telemetry telemetry) {
        eventListeners.forEach((n) -> telemetry.addData("Event Found", n.name));
        advancedTelemetry.printData();
    }

    public Gamepad getGamePad1() {
        return this.gamePad1;
    }

    public Gamepad getGamePad2() {
        return this.gamePad2;
    }

    public void addListener(Callable<Boolean> listener, Runnable event, String name) {
        eventListeners.add(new EventPair(listener, event, name));
    }

    public void removeListener(String name) {
        // Application of lambdas
        // equivalent to looping through eventListeners, and for every eventListener checking if it's equal to the name
        // and then if it *is* equal, then remove it, because that's the return value of the lambda
        eventListeners.removeIf(eventListener -> eventListener.isEqualTo(name));
    }

    public enum GamepadButtons {
        BACK {
            @Override
            public boolean detect(Gamepad gamepad){
                return (gamepad.back);
            }
        }, START {
            @Override
            public boolean detect(Gamepad gamepad) {
                return (gamepad.start);
            }
        }, LEFT_TRIGGER {
            @Override
            public boolean detect(Gamepad gamepad) {
                return (gamepad.left_trigger > 0.5);
            }
        }, RIGHT_TRIGGER {
            @Override
            public boolean detect(Gamepad gamepad) {
                return (gamepad.right_trigger > 0.5);
            }
        }, LEFT_BUMPER {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.left_bumper;
            }
        }, RIGHT_BUMPER {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.right_bumper;
            }
        }, DPAD_LEFT {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.dpad_left;
            }
        }, DPAD_DOWN {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.dpad_down;
            }
        }, DPAD_RIGHT {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.dpad_right;
            }
        }, DPAD_UP {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.dpad_up;
            }
        }, A {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.a;
            }
        }, B {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.b;
            }
        }, X {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.x;
            }
        }, Y {
            @Override
            public boolean detect(Gamepad gamepad) {
                return gamepad.y;
            }
        };

        public abstract boolean detect(Gamepad gamepad);
    }

    public enum GamepadSticks {
        LEFT_JOYSTICK_X {
            @Override
            public float getValue(Gamepad gamepad) {
                return gamepad.left_stick_x;
            }
        },
        LEFT_JOYSTICK_Y {
            @Override
            public float getValue(Gamepad gamepad) {
                return -gamepad.left_stick_y;
            }
        },
        RIGHT_JOYSTICK_X {
            @Override
            public float getValue(Gamepad gamepad) {
                return gamepad.right_stick_x;
            }
        },
        RIGHT_JOYSTICK_Y {
            @Override
            public float getValue(Gamepad gamepad) {
                return -gamepad.right_stick_y;
            }
        };

        public abstract float getValue(Gamepad gamepad);
    }
}
