package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Sensor;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.socket.SensorSocketEndpoint;
import ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThermostatService {

    @Autowired private SensorSocketEndpoint endpoint;

    @Autowired private DeviceService deviceService;

    @Autowired private ThermostatPopulationService thermostatPopulationService;

    @Autowired private ThermostatRepository thermostatRepository;

    private void randomJitter(Thermostat thermostat) {
        updateValueForThermostat(
                thermostat,
                Sensor.TYPICAL_VALUES
                        .get(Sensor.SensorType.TEMPERATURE)
                        .multiply(BigDecimal.valueOf(0.975 + Math.random() / 20)));
    }

    private void updateValueForThermostat(Thermostat thermostat, BigDecimal value) {
        thermostat.setInternalSensorTemperature(value);
        deviceService.saveAsOwner(
                thermostat, thermostatRepository.findUser(thermostat.getId()).getUsername());
    }

    public void fakeUpdateAll() {
        thermostatRepository.findAll().forEach(this::randomJitter);
        updateStates();
    }

    public List<Thermostat> findAll(String username) {
        Iterable<Thermostat> all = thermostatRepository.findAllByUsername(username);
        all.forEach(thermostatPopulationService::populateMeasuredTemperature);
        return Utils.toList(all);
    }

    public void computeState(Thermostat t) {
        thermostatPopulationService.populateMeasuredTemperature(t);
        t.computeState();
    }

    private void updateState(Thermostat t) {
        this.computeState(t);

        deviceService.saveAsOwner(t, thermostatRepository.findUser(t.getId()).getUsername());
        endpoint.queueDeviceUpdate(t, thermostatRepository.findUser(t.getId()), false, null, false);
    }

    public void updateStates() {
        Iterable<Thermostat> ts = thermostatRepository.findAll();
        ts.forEach(this::updateState);
    }

    public Optional<Thermostat> findById(Long thermostat, String username) {
        Optional<Thermostat> t = thermostatRepository.findByIdAndUsername(thermostat, username);

        if (t.isPresent()) {
            Thermostat u = t.get();
            thermostatPopulationService.populateMeasuredTemperature(u);
            t = Optional.of(u);
        }
        return t;
    }
}
