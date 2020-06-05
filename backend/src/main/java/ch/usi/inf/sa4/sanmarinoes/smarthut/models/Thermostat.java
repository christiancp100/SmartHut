package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;

/** A thermostat capable of controlling cooling and heating. */
@Entity
@EqualsAndHashCode(callSuper = true)
public class Thermostat extends Switchable implements BooleanTriggerable {

    @Override
    public boolean isOn() {
        return mode != Mode.OFF;
    }

    @Override
    public void setOn(boolean on) {
        mode = on ? Mode.IDLE : Mode.OFF;
        computeState();
    }

    /** Computes the new thermostat state, for when the thermostat is on */
    public void computeState() {
        if (mode == Thermostat.Mode.OFF) {
            return;
        }

        BigDecimal measured = this.getMeasuredTemperature();
        BigDecimal target = this.getTargetTemperature();

        if (measured == null) {
            this.setMode(Thermostat.Mode.IDLE);
            return;
        }

        BigDecimal delta = target.subtract(measured);

        if (delta.abs().doubleValue() < 0.25) {
            this.setMode(Thermostat.Mode.IDLE);
        } else if (delta.signum() > 0) {
            this.setMode(Thermostat.Mode.HEATING);
        } else {
            this.setMode(Thermostat.Mode.COOLING);
        }
    }

    @Override
    public boolean readTriggerState() {
        return mode != Mode.OFF;
    }

    public enum Mode {
        @SerializedName("OFF")
        OFF,
        @SerializedName("IDLE")
        IDLE,
        @SerializedName("COOLING")
        COOLING,
        @SerializedName("HEATING")
        HEATING
    }

    /** Temperature to be reached */
    @Column private BigDecimal targetTemperature;

    /** The temperature detected by the embedded sensor */
    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal internalSensorTemperature =
            Sensor.TYPICAL_VALUES.get(Sensor.SensorType.TEMPERATURE);

    /** State of this thermostat */
    @Column private Thermostat.Mode mode;

    @Transient private BigDecimal measuredTemperature;

    @Column private boolean useExternalSensors = false;

    /** Creates a thermostat with a temperature sensor and its initial OFF state */
    public Thermostat() {
        super("thermostat");
        this.mode = Mode.OFF;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Thermostat{");
        sb.append("targetTemperature=").append(targetTemperature);
        sb.append(", internalSensorTemperature=").append(internalSensorTemperature);
        sb.append(", mode=").append(mode);
        sb.append(", measuredTemperature=").append(measuredTemperature);
        sb.append(", useExternalSensors=").append(useExternalSensors);
        sb.append('}');
        return sb.toString();
    }

    public void setMode(Mode state) {
        this.mode = state;
    }

    public Mode getMode() {
        return this.mode;
    }

    public BigDecimal getTargetTemperature() {
        return this.targetTemperature;
    }

    public BigDecimal getInternalSensorTemperature() {
        return internalSensorTemperature;
    }

    public boolean isUseExternalSensors() {
        return useExternalSensors;
    }

    public BigDecimal getMeasuredTemperature() {
        return measuredTemperature;
    }

    public void setMeasuredTemperature(BigDecimal measuredTemperature) {
        this.measuredTemperature = measuredTemperature;
    }

    public void setTargetTemperature(BigDecimal targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public void setInternalSensorTemperature(BigDecimal internalSensorTemperature) {
        this.internalSensorTemperature = internalSensorTemperature;
    }

    public void setUseExternalSensors(boolean useExternalSensors) {
        this.useExternalSensors = useExternalSensors;
    }
}
