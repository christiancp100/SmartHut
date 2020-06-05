package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/** Represents a state for a Switchable device */
@Entity
public class SwitchableState extends State {

    public void setDevice(Switchable device) {
        setInnerDevice(device);
    }

    @Column(name = "switchable_on")
    @Getter
    @Setter
    private boolean on;

    @Override
    public void apply() {
        ((Switchable) getDevice()).readStateAndSet(this);
    }

    @Override
    protected SwitchableState copy() {
        final SwitchableState d = new SwitchableState();
        d.setOn(on);
        return d;
    }
}
