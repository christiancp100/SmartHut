package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/** Represents a standard non-dimmable light */
@Entity
@EqualsAndHashCode(callSuper = true)
public class RegularLight extends Switchable implements BooleanTriggerable {

    /** Whether the light is on or not */
    @Column(name = "light_on", nullable = false)
    @Getter
    @Setter
    boolean on;

    public RegularLight() {
        super("regularLight");
    }

    @Override
    public boolean readTriggerState() {
        return on;
    }
}
