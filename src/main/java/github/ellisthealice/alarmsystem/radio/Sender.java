/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.radio;

import github.ellisthealice.alarmsystem.util.AlarmLogger;
import java.io.IOException;
import github.ellisthealice.alarmsystem.util.Props;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author EllisTheAlice
 */
public class Sender {

    private static final AlarmLogger LOGGER = AlarmLogger.getLogger(Sender.class);
    private static final Map<Switches, Boolean> SWITCH_STATUS = new HashMap<>();

    public static void switchOn(Switches s) {
        command(true, s);
    }

    public static void switchOnAll() {
        for (Switches s : Switches.values()) {
            command(true, s);
        }
    }

    public static void switchOff(Switches s) {
        command(false, s);
    }

    public static void switchOffAll() {
        for (Switches s : Switches.values()) {
            command(false, s);
        }
    }

    public static void toggle(Switches s) {
        command(!SWITCH_STATUS.get(s), s);
    }

    private static void command(boolean on, Switches s) {
        try {
            String switchCode = "";

            switch (s) {
                case A:
                    switchCode = Props.SWITCH_A_CODE;
                    break;
                case B:
                    switchCode = Props.SWITCH_B_CODE;
                    break;
                case C:
                    switchCode = Props.SWITCH_C_CODE;
                    break;
            }

            Process p = Runtime.getRuntime().exec("./codesend " + switchCode);
            p.waitFor();
            SWITCH_STATUS.put(s, on);
        } catch (InterruptedException | IOException ex) {
            LOGGER.logError(ex.getMessage());
        }
    }
}
