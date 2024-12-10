package org.nknsd.teamcode.helperClasses;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.LinkedList;

public class AdvancedTelemetry {
    private final Telemetry telemetry;
    private final LinkedList<StringPairs> data = new LinkedList<>();

    private class StringPairs {
        public final String a;
        public final String b;
        public StringPairs(String a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    public AdvancedTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void addData(String caption, Object value) {
        StringPairs dataPair = new StringPairs(caption, String.valueOf(value));
        data.add(dataPair);
    }

    @SuppressWarnings("unused")
    public void addSingleData(Object value) {
        StringPairs dataPair = new StringPairs(String.valueOf(value), "");
        data.add(dataPair);
    }

    public void printData() {
        data.forEach((n) -> telemetry.addData(n.a, n.b));
        data.clear();
    }
}

