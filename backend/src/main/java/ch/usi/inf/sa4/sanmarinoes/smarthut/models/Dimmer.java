package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonExclude;
import ch.usi.inf.sa4.sanmarinoes.smarthut.config.SocketGsonExclude;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.EqualsAndHashCode;

/** Represents a generic dimmer input device */
@Entity
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Dimmer extends InputDevice implements Connectable<Dimmable> {
    protected Dimmer(String kind) {
        super(kind);
    }

    @ManyToMany(cascade = CascadeType.DETACH)
    @GsonExclude
    @SocketGsonExclude
    @EqualsAndHashCode.Exclude
    @JoinTable(
            name = "dimmer_dimmable",
            joinColumns = @JoinColumn(name = "dimmer_id"),
            inverseJoinColumns = @JoinColumn(name = "dimmable_id"))
    private Set<Dimmable> dimmables = new HashSet<>();

    /**
     * Get the lights connected to this dimmer
     *
     * @return duh
     */
    @Override
    public Set<Dimmable> getOutputs() {
        return this.dimmables;
    }

    /** Add a light to be controller by this dimmer */
    public void addDimmable(Dimmable dimmable) {
        dimmables.add(dimmable);
    }

    public void connect(Dimmable output, boolean connect) {
        if (connect) {
            output.getDimmers().add(this);
            getOutputs().add(output);
        } else {
            output.getDimmers().remove(this);
            getOutputs().remove(output);
        }
    }
}
