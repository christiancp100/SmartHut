package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.MotionSensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Motion Sensor Tests")
public class MotionSensorTests {

    private MotionSensor motionSensor;

    @BeforeEach
    public void createMotionSensor() {
        motionSensor = new MotionSensor();
    }

    @Test
    @DisplayName("set and get detected")
    public void setAndGetDetected() {
        this.motionSensor.setDetected(true);
        assertTrue(this.motionSensor.isDetected());
    }
}
