package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import ch.usi.inf.sa4.sanmarinoes.smarthut.config.SocketGsonExclude;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Dimmable extends Switchable implements RangeTriggerable {

    protected Dimmable(String kind) {
        super(kind);
    }

    @ManyToMany(mappedBy = "dimmables", cascade = CascadeType.DETACH)
    @GsonExclude
    @SocketGsonExclude
    @Getter
    @Setter
    private Set<Dimmer> dimmers = new HashSet<>();

    /** The light intensity value. Goes from 0 (off) to 100 (on) */
    @Column(nullable = false)
    @Min(0)
    @Max(100)
    @Getter
    private Integer intensity = 0;

    @Column(nullable = false)
    @Getter
    @Setter
    private Integer oldIntensity = 100;

    /**
     * Sets the intensity to a certain level. Out of bound values are corrected to the respective
     * extremums. An intensity level of 0 turns the light off, but keeps the old intensity level
     * stored.
     *
     * @param intensity the intensity level (may be out of bounds)
     */
    public void setIntensity(Integer intensity) {
        if (intensity <= 0) {
            this.intensity = 0;
        } else if (intensity > 100) {
            this.intensity = 100;
            this.oldIntensity = 100;
        } else {
            this.intensity = intensity;
            this.oldIntensity = intensity;
        }
    }

    @Override
    public boolean isOn() {
        return intensity != 0;
    }

    @Override
    public void setOn(boolean on) {
        intensity = on ? oldIntensity : 0;
    }

    public void readStateAndSet(DimmableState state) {
        setIntensity(state.getIntensity());
    }

    @Override
    public State cloneState() {
        final DimmableState newState = new DimmableState();
        newState.setDeviceId(getId());
        newState.setIntensity(getIntensity());
        return newState;
    }

    @Override
    public double readTriggerState() {
        return intensity;
    }
}
