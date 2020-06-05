package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * A generic abstraction for an input device, i.e. something that captures input either from the
 * environment (sensor) or the user (switch / dimmer).
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class InputDevice extends Device {
    public InputDevice(String kind) {
        super(kind, FlowType.INPUT);
    }

    public Set<? extends OutputDevice> getOutputs() {
        return Set.of();
    }
}
