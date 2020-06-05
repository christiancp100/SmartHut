package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper = true)
public class BooleanCondition extends Condition<BooleanTriggerable> {

    @Getter
    @Setter
    @Column(name = "switchable_on")
    private boolean on;

    public BooleanCondition() {
        super("booleanCondition");
    }

    @Override
    public boolean triggered() {
        return this.getDevice().readTriggerState() == isOn();
    }
}
