package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Represents a generic output device, i.e. something that causes some behaviour (light, smartPlugs,
 * ...).
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OutputDevice extends Device {
    public OutputDevice(String kind) {
        super(kind, FlowType.OUTPUT);
    }

    /**
     * Creates a State object initialized to point at this device and with values copied from this
     * device's state
     *
     * @return a new State object
     */
    public abstract State cloneState();
}
