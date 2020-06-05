package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;

/**
 * Represents a dimmer able to set absolute intensity values (i.e. knowing the absolute intensity
 * value, like a knob)
 */
@Entity
@EqualsAndHashCode(callSuper = true)
public class KnobDimmer extends Dimmer implements RangeTriggerable {

    @Column private int intensity = 0;

    public KnobDimmer() {
        super("knobDimmer");
    }

    /**
     * Sets absolutely the intensity level of all lights connected
     *
     * @param intensity the intensity (must be from 0 to 100)
     */
    public void setLightIntensity(int intensity) {
        this.intensity = intensity;
        for (Dimmable dl : getOutputs()) {
            dl.setIntensity(intensity);
        }
    }

    @Override
    public double readTriggerState() {
        return intensity;
    }
}
