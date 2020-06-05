package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@EqualsAndHashCode(callSuper = true)
public class RangeCondition extends Condition<RangeTriggerable> {

    public RangeCondition() {
        super("rangeCondition");
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
