package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.GenericDeviceSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.MotionSensor;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.MotionSensorRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.MotionSensorService;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/motionSensor")
public class MotionSensorController {

    @Autowired private DeviceService deviceService;
    @Autowired private MotionSensorService motionSensorService;
    @Autowired private MotionSensorRepository motionSensorRepository;

    @PostMapping
    public MotionSensor create(
            @Valid @RequestBody GenericDeviceSaveRequest ms, final Principal principal)
            throws NotFoundException {
        deviceService.throwIfRoomNotOwned(ms.getRoomId(), principal.getName());
        MotionSensor newMS = new MotionSensor();
        newMS.setName(ms.getName());
        newMS.setRoomId(ms.getRoomId());

        return deviceService.saveAsOwner(newMS, principal.getName());
    }

    @PutMapping("/{id}/detect")
    public MotionSensor updateDetection(
            @PathVariable("id") Long sensorId,
            @RequestParam("detected") boolean detected,
            final Principal principal)
            throws NotFoundException {

        return motionSensorService.updateDetectionFromMotionSensor(
                motionSensorRepository
                        .findByIdAndUsername(sensorId, principal.getName())
                        .orElseThrow(NotFoundException::new),
                detected,
                principal.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        deviceService.deleteByIdAsOwner(id, principal.getName());
    }
}
