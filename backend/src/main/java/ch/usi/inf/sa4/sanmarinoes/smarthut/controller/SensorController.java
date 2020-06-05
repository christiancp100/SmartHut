package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SensorSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Sensor;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SensorRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.SensorService;
import java.math.BigDecimal;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/sensor")
public class SensorController {

    private final DeviceService deviceService;

    private final SensorRepository sensorRepository;

    private final SensorService sensorService;

    @Autowired
    public SensorController(
            DeviceService deviceService,
            SensorRepository sensorRepository,
            SensorService sensorService) {
        this.deviceService = deviceService;
        this.sensorRepository = sensorRepository;
        this.sensorService = sensorService;
    }

    @PostMapping
    public Sensor create(@Valid @RequestBody SensorSaveRequest s, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(s.getRoomId(), principal.getName());

        Sensor newSensor = new Sensor();
        newSensor.setSensor(s.getSensor());
        newSensor.setName(s.getName());
        newSensor.setRoomId(s.getRoomId());
        newSensor.setValue(s.getValue());

        return deviceService.saveAsOwner(newSensor, principal.getName());
    }

    @PutMapping("/{id}/value")
    public Sensor updateValue(
            @PathVariable("id") Long sensorId,
            @RequestParam("value") BigDecimal value,
            final Principal principal)
            throws NotFoundException {
        return sensorService.updateValueFromSensor(
                sensorRepository
                        .findByIdAndUsername(sensorId, principal.getName())
                        .orElseThrow(NotFoundException::new),
                value);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        deviceService.deleteByIdAsOwner(id, principal.getName());
    }
}
