package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import com.google.gson.annotations.SerializedName;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class ThermostatCondition extends Condition<Thermostat> {

    public ThermostatCondition() {
        super("thermostatCondition");
    }

    public enum Operator {
        @SerializedName("EQUAL")
        EQUAL,
        @SerializedName("NOTEQUAL")
        NOTEQUAL,
    }

    @Column(nullable = false)
    private ThermostatCondition.Operator operator;

    @Column(nullable = false)
    private Thermostat.Mode mode;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Thermostat.Mode getMode() {
        return mode;
    }

    public void setMode(Thermostat.Mode mode) {
        this.mode = mode;
    }

    @Override
    public boolean triggered() {
        switch (operator) {
            case EQUAL:
                return getDevice().getMode() == mode;
            case NOTEQUAL:
                return getDevice().getMode() != mode;
            default:
                return false;
        }
    }
}
