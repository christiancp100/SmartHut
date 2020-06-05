package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Represents a motion sensor device */
@Entity
@EqualsAndHashCode(callSuper = true)
public class MotionSensor extends InputDevice implements BooleanTriggerable {

    @Getter
    @Setter
    @Column(nullable = false)
    private boolean detected;

    public MotionSensor() {
        super("motionSensor");
    }

    @Override
    public boolean readTriggerState() {
        return detected;
    }
}
