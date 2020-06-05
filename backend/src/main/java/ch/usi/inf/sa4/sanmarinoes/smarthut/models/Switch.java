package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import ch.usi.inf.sa4.sanmarinoes.smarthut.config.SocketGsonExclude;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.EqualsAndHashCode;

/** A switch input device */
@Entity
@EqualsAndHashCode(callSuper = true)
public class Switch extends InputDevice implements BooleanTriggerable, Connectable<Switchable> {

    @ManyToMany(
            cascade = {
                CascadeType.DETACH,
                CascadeType.MERGE,
                CascadeType.REFRESH,
                CascadeType.PERSIST
            })
    @GsonExclude
    @SocketGsonExclude
    @EqualsAndHashCode.Exclude
    @JoinTable(
            name = "switch_switchable",
            joinColumns = @JoinColumn(name = "switch_id"),
            inverseJoinColumns = @JoinColumn(name = "switchable_id"))
    private Set<Switchable> switchables = new HashSet<>();

    /** The state of this switch */
    @Column(nullable = false, name = "switch_on")
    private boolean on;

    public Switch() {
        super("switch");
    }

    /**
     * Setter method for this Switch
     *
     * @param state The state to be set
     */
    public void setOn(boolean state) {
        on = state;

        for (final Switchable s : switchables) {
            s.setOn(on);
        }
    }

    /** Toggle between on and off state */
    public void toggle() {
        setOn(!isOn());
    }

    /**
     * Getter method for this Switch
     *
     * @return This Switch on state
     */
    public boolean isOn() {
        return on;
    }

    @Override
    public Set<Switchable> getOutputs() {
        return switchables;
    }

    public void connect(Switchable output, boolean connect) {
        if (connect) {
            output.getSwitches().add(this);
            getOutputs().add(output);
        } else {
            output.getSwitches().remove(this);
            getOutputs().remove(output);
        }
    }

    @Override
    public boolean readTriggerState() {
        return on;
    }
}
