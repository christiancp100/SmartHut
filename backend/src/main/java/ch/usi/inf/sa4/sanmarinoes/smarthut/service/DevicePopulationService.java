package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Device;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevicePopulationService {

    @Autowired private ThermostatPopulationService thermostatService;

    public void populateComputedFields(Iterable<Device> devices) {
        for (Device d : devices) {
            if (d instanceof Thermostat) {
                thermostatService.populateMeasuredTemperature((Thermostat) d);
            }
        }
    }
}
