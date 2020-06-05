package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
public class RangeTrigger extends Trigger<RangeTriggerable> {

    public RangeTrigger() {
        super("rangeTrigger");
    }

    @Getter
    @Setter
    @Column(nullable = false)
    private Operator operator;

    @Getter
    @Setter
    @Column(nullable = false)
    private double range;

    @Override
    public boolean triggered() {
        return operator.checkAgainst(getDevice().readTriggerState(), range);
    }
}
