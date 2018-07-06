/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.ir;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import github.ellisthealice.alarmsystem.util.AlarmLogger;

/**
 *
 * @author EllisTheAlice
 */
public class MotionDetector {

    final GpioController gpio = GpioFactory.getInstance();
    private static final Pin DEFAULT_PIN = RaspiPin.GPIO_02;
    private Pin pirPin = null;
    private GpioPinDigitalInput pirInput;
    private MotionDetectionInterface detector;
    private final AlarmLogger logger = AlarmLogger.getLogger(MotionDetector.class);

    public MotionDetector(MotionDetectionInterface parent) {
        this(DEFAULT_PIN, parent);
    }

    public MotionDetector(Pin p, MotionDetectionInterface parent) {
        this.pirPin = p;
        this.detector = parent;

        init();
    }

    private void init() {
        this.pirInput = gpio.provisionDigitalInputPin(pirPin, "Motion");
        this.pirInput.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                //if ("true".equals(System.getProperty("pir.verbose", "false"))) {
                System.out.println("Yeah!");
                logger.logInfo(" >>> GPIO pin state changed: time=" + System.currentTimeMillis() + ", " + event.getPin() + " = " + event.getState());
                //}
                if (event.getState().isHigh()) {
                    detector.motionDetected();
                }
            }
        });
    }

    public void shutdown() {
        gpio.shutdown();
    }
}
