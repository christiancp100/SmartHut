package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import ch.usi.inf.sa4.sanmarinoes.smarthut.config.SocketGsonExclude;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.EqualsAndHashCode;

/** A device that can be turned either on or off */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Switchable extends OutputDevice {

    @ManyToMany(
            mappedBy = "switchables",
            cascade = {
                CascadeType.DETACH,
                CascadeType.MERGE,
                CascadeType.REFRESH,
                CascadeType.PERSIST
            })
    @GsonExclude
    @SocketGsonExclude
    @EqualsAndHashCode.Exclude
    private Set<Switch> inputs = new HashSet<>();

    protected Switchable(String kind) {
        super(kind);
    }

    /**
     * Returns whether the device is on (true) or not (false)
     *
     * @return whether the device is on (true) or not (false)
     */
    public abstract boolean isOn();

    /**
     * Sets the on status of the device
     *
     * @param on the new on status: true for on, false for off
     */
    public abstract void setOn(boolean on);

    public Set<Switch> getSwitches() {
        return inputs;
    }

    public void readStateAndSet(SwitchableState state) {
        setOn(state.isOn());
    }

    public State cloneState() {
        final SwitchableState newState = new SwitchableState();
        newState.setDeviceId(getId());
        newState.setOn(isOn());
        return newState;
    }
}
