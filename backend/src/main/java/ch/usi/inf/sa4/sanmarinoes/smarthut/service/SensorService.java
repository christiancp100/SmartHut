package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Sensor;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SensorRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.socket.SensorSocketEndpoint;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SensorService {

    @Autowired private SensorRepository sensorRepository;

    @Autowired private DeviceService deviceService;

    @Autowired private ThermostatService thermostatService;

    @Autowired private SensorSocketEndpoint endpoint;

    private void randomJitter(Sensor sensor) {
        updateValueFromSensor(
                sensor,
                Sensor.TYPICAL_VALUES
                        .get(sensor.getSensor())
                        .multiply(BigDecimal.valueOf(0.975 + Math.random() / 20)));
    }

    public void sensorFakeUpdate() {
        sensorRepository.findAll().forEach(this::randomJitter);
        thermostatService.updateStates();
    }

    /**
     * Updates the sensor with new measurement and propagates update through websocket
     *
     * @param sensor the sensor to update
     * @param value the new measurement
     * @return the updated sensor
     */
    public Sensor updateValueFromSensor(Sensor sensor, BigDecimal value) {
        sensor.setValue(value);
        sensor =
                deviceService.saveAsOwner(
                        sensor, sensorRepository.findUser(sensor.getId()).getUsername());
        endpoint.queueDeviceUpdate(
                sensor, sensorRepository.findUser(sensor.getId()), false, null, false);
        return sensor;
    }
}
