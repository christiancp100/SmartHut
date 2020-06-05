package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Entity;

/**
 * Represents a dimmer that can only instruct an increase or decrease of intensity (i.e. like a
 * dimmer with a '+' and a '-' button)
 */
@Entity
public class ButtonDimmer extends Dimmer {

    /** The delta amount to apply to a increase or decrease intensity */
    private static final int DIM_INCREMENT = 10;

    public ButtonDimmer() {
        super("buttonDimmer");
    }

    /** Increases the current intensity level of the dimmable light by DIM_INCREMENT */
    public void increaseIntensity() {
        for (Dimmable dl : getOutputs()) {
            dl.setIntensity(dl.getIntensity() + DIM_INCREMENT);
        }
    }

    /** Decreases the current intensity level of the dimmable light by DIM_INCREMENT */
    public void decreaseIntensity() {
        for (Dimmable dl : getOutputs()) {
            dl.setIntensity(dl.getIntensity() - DIM_INCREMENT);
        }
    }
}
