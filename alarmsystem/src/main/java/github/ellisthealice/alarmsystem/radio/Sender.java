/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.radio;

import github.ellisthealice.alarmsystem.util.AlarmLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EllisTheAlice
 */
public class Sender {

    private static final AlarmLogger LOGGER = AlarmLogger.getLogger(Sender.class);
    private static final Map<Switches, Boolean> SWITCH_STATUS = new HashMap<>();
    private static final Map<Switches, String> SWITCH_CODES = new HashMap<>();
    private static final String A_ON = "310 4960 300 1020 300 1020 950 370 300 1020 300 1020 950 370 950 370 300 1020 950 370 300 1020 300 1020 300 1020 950 370 300 1020 300 1020 950 370 950 370 950 370 300 1020 950 370 300 1020 950 370 300 1020 950 370 950 370 950 370 950 370 300 1020";
    private static final String A_OFF = "310 4960 300 1020 300 1020 950 370 300 1020 300 1020 950 370 300 1020 950 370 300 1020 950 370 300 1020 950 370 950 370 300 1020 950 370 950 370 950 370 300 1020 300 1020 300 1020 300 1020 950 370 950 370 950 370 950 370 300 1020 950 370 300 1020";
    private static final String B_ON = "310 4960 300 1020 300 1020 950 370 300 1020 300 1020 950 370 300 1020 300 1020 300 1020 300 1020 950 370 300 1020 300 1020 950 370 300 1020 950 370 950 370 950 370 300 1020 950 370 950 370 950 370 300 1020 950 370 300 1020 300 1020 300 1020 950 370";
    private static final String B_OFF = "310 4960 300 1020 300 1020 950 370 300 1020 300 1020 950 370 300 1020 300 1020 300 1020 950 370 300 1020 950 370 950 370 950 370 300 1020 950 370 950 370 300 1020 300 1020 300 1020 950 370 950 370 300 1020 950 370 950 370 950 370 300 1020 950 370";
    private static final String C_ON = "310 4960 950 370 950 370 950 370 300 1020 300 1020 950 370 950 370 300 1020 300 1020 950 370 300 1020 300 1020 300 1020 300 1020 300 1020 300 1020 300 1020 300 1020 950 370 300 1020 950 370 950 370 300 1020 950 370 950 370 300 1020 300 1020 950 370";
    private static final String C_OFF = "310 4960 950 370 950 370 950 370 300 1020 300 1020 950 370 950 370 300 1020 950 370 300 1020 950 370 300 1020 300 1020 300 1020 300 1020 950 370 300 1020 300 1020 300 1020 950 370 300 1020 950 370 300 1020 950 370 300 1020 950 370 300 1020 950 370";

    public Sender() {
        SWITCH_CODES.put(Switches.A, "");
        SWITCH_CODES.put(Switches.B, "");
        SWITCH_CODES.put(Switches.C, "");
    }

    public static void switchOn(Switches s) {
        command(true, s);
    }

    public static void switchOnAll() {
        for (Switches s : Switches.values()) {
            command(true, s);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                LOGGER.logError(ex.getMessage());
            }
        }
    }

    public static void switchOff(Switches s) {
        command(false, s);
    }

    public static void switchOffAll() {
        for (Switches s : Switches.values()) {
            command(false, s);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                LOGGER.logError(ex.getMessage());
            }
        }
    }

    public static void toggle(Switches s) {
        command(!SWITCH_STATUS.get(s), s);
    }

    private static void command(boolean on, Switches s) {
        try {
            LOGGER.logInfo("Set state of switch " + s.toString() + " to state " + (on ? "on" : "off"));
            String switchCode = "";
            if (on) {
                switch (s) {
                    case A:
                        switchCode = A_ON;
                        break;
                    case B:
                        switchCode = B_ON;
                        break;
                    case C:
                        switchCode = C_ON;
                        break;
                }
            } else {
                switch (s) {
                    case A:
                        switchCode = A_OFF;
                        break;
                    case B:
                        switchCode = B_OFF;
                        break;
                    case C:
                        switchCode = C_OFF;
                        break;
                }
            }
            LOGGER.logInfo("Executing command pilight-send -p raw -c \"" + switchCode + "\"");
            //Process p = Runtime.getRuntime().exec(new String[]{"pilight-send", "-p raw", "-c " + switchCode});
            Process p = Runtime.getRuntime().exec("/bin/bash /home/pi/pilight.sh " + s.toString().toUpperCase() + " " + (on ? "ON" : "OFF"));
            p.waitFor();
            LOGGER.logInfo("Exitcode of pilight process was " + p.exitValue());
            SWITCH_STATUS.put(s, on);
        } catch (InterruptedException | IOException ex) {
            LOGGER.logError(ex.getMessage());
        }
    }
}
