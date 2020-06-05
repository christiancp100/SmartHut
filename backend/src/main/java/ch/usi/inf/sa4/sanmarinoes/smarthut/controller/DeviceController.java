package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.DeviceSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.BadDataException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Device;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DeviceRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Room;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RoomRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/device")
public class DeviceController {

    @Autowired private DeviceService deviceService;
    @Autowired private DeviceRepository<Device> deviceRepository;
    @Autowired private RoomRepository roomRepository;

    @GetMapping
    public List<Device> getAll(
            @RequestParam(value = "hostId", required = false) Long hostId, final Principal user)
            throws NotFoundException {
        return deviceService.findAll(hostId, user.getName());
    }

    @PutMapping
    public Device update(
            @Valid @RequestBody DeviceSaveRequest deviceSaveRequest, final Principal principal)
            throws NotFoundException, BadDataException {
        final Device d =
                deviceRepository
                        .findByIdAndUsername(deviceSaveRequest.getId(), principal.getName())
                        .orElseThrow(NotFoundException::new);

        // check if roomId is valid
        final Room r =
                roomRepository
                        .findByIdAndUsername(deviceSaveRequest.getRoomId(), principal.getName())
                        .orElseThrow(() -> new BadDataException("roomId is not a valid room id"));

        d.setRoomId(r.getId());
        d.setName(deviceSaveRequest.getName());

        deviceService.saveAsOwner(d, principal.getName());
        return d;
    }
}
