package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/** Represent a state for an IDimmable device */
@Entity
public class DimmableState extends State {

    public void setDevice(Dimmable device) {
        setInnerDevice(device);
    }

    /** The light intensity value. Goes from 0 (off) to 100 (on) */
    @Min(0)
    @Max(100)
    @Getter
    @Setter
    private int intensity = 0;

    @Override
    public void apply() {
        ((Dimmable) getDevice()).readStateAndSet(this);
    }

    @Override
    protected DimmableState copy() {
        final DimmableState d = new DimmableState();
        d.setIntensity(intensity);
        return d;
    }
}
