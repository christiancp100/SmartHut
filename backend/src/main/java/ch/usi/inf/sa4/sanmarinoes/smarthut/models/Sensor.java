package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.EqualsAndHashCode;

/** A sensor input device that measures a quantity in a continuous scale (e.g. temperature) */
@Entity
@EqualsAndHashCode(callSuper = true)
public class Sensor extends InputDevice implements RangeTriggerable {

    public static final Map<SensorType, BigDecimal> TYPICAL_VALUES =
            Map.of(
                    SensorType.TEMPERATURE, BigDecimal.valueOf(17.0),
                    SensorType.HUMIDITY, BigDecimal.valueOf(40.0),
                    SensorType.LIGHT, BigDecimal.valueOf(1000));

    @Override
    public double readTriggerState() {
        return value.doubleValue();
    }

    /** Type of sensor, i.e. of the thing the sensor measures. */
    public enum SensorType {
        /** A sensor that measures temperature in degrees celsius */
        @SerializedName("TEMPERATURE")
        TEMPERATURE,

        /** A sensor that measures relative humidity in percentage points */
        @SerializedName("HUMIDITY")
        HUMIDITY,

        /** A sensor that measures light in degrees */
        @SerializedName("LIGHT")
        LIGHT
    }

    /** The value of this sensor according to its sensor type */
    @Column(nullable = false, precision = 11, scale = 1)
    private BigDecimal value;

    /** The type of this sensor */
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SensorType sensor;

    public SensorType getSensor() {
        return sensor;
    }

    public void setSensor(SensorType sensor) {
        this.sensor = sensor;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal newValue) {
        this.value = newValue;
    }

    public Sensor() {
        super("sensor");
    }

    @Override
    public String toString() {
        return "Sensor{" + "value=" + value + ", sensor=" + sensor + '}';
    }
}
