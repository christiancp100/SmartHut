package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;

/** A smart plug that can be turned either on or off */
@Entity
@EqualsAndHashCode(callSuper = true)
public class SmartPlug extends Switchable implements BooleanTriggerable {

    /** The average consumption of an active plug when on in Watt */
    public static final Double AVERAGE_CONSUMPTION_KW = 200.0;

    /** The total amount of power that the smart plug has consumed represented in W/h */
    @Column(precision = 13, scale = 3)
    private BigDecimal totalConsumption = BigDecimal.ZERO;

    /** Whether the smart plug is on */
    @Column(name = "smart_plug_on", nullable = false)
    private boolean on;

    public BigDecimal getTotalConsumption() {
        return totalConsumption;
    }

    /** Resets the consuption meter */
    public void resetTotalConsumption() {
        totalConsumption = BigDecimal.ZERO;
    }

    @Override
    public boolean isOn() {
        return on;
    }

    @Override
    public void setOn(boolean on) {
        this.on = on;
    }

    public SmartPlug() {
        super("smartPlug");
    }

    @Override
    public boolean readTriggerState() {
        return on;
    }
}
