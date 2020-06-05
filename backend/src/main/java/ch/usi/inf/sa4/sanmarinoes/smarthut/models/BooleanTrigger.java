package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
public class BooleanTrigger extends Trigger<BooleanTriggerable> {

    @Column(name = "switchable_on")
    @Getter
    @Setter
    private boolean on;

    public BooleanTrigger() {
        super("booleanTrigger");
    }

    @Override
    public boolean triggered() {
        return getDevice().readTriggerState() == isOn();
    }
}
