package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Sensor;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThermostatPopulationService {

    @Autowired private ThermostatRepository thermostatRepository;

    private BigDecimal measureTemperature(final Thermostat thermostat) {
        Optional<BigDecimal> average;

        if (thermostat.isUseExternalSensors()) {
            average =
                    thermostatRepository.getAverageTemperature(
                            thermostat.getRoomId(), Sensor.SensorType.TEMPERATURE);

        } else {
            return thermostat.getInternalSensorTemperature();
        }

        return average.orElse(null);
    }

    public void populateMeasuredTemperature(Thermostat thermostat) {
        thermostat.setMeasuredTemperature(measureTemperature(thermostat));
    }
}
