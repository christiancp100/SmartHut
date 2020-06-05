package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils.toList;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.RoomSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/room")
public class RoomController {

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    private final DeviceService deviceService;

    private final SwitchRepository switchRepository;

    private final ButtonDimmerRepository buttonDimmerRepository;

    private final KnobDimmerRepository knobDimmerRepository;

    @Autowired
    public RoomController(
            RoomRepository roomRepository,
            UserRepository userRepository,
            DeviceService deviceService,
            SwitchRepository switchRepository,
            ButtonDimmerRepository buttonDimmerRepository,
            KnobDimmerRepository knobDimmerRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.deviceService = deviceService;
        this.switchRepository = switchRepository;
        this.buttonDimmerRepository = buttonDimmerRepository;
        this.knobDimmerRepository = knobDimmerRepository;
    }

    private <T> List<T> fetchOwnerOrGuest(
            final List<T> list, Long hostId, final Principal principal) throws NotFoundException {
        if (hostId == null) {
            return list;
        } else {
            return Utils.returnIfGuest(userRepository, list, hostId, principal);
        }
    }

    @GetMapping
    public List<Room> findAll(
            @RequestParam(value = "hostId", required = false) Long hostId,
            final Principal principal)
            throws NotFoundException {

        List<Room> rooms =
                toList(
                        hostId != null
                                ? roomRepository.findByUserId(hostId)
                                : roomRepository.findByUsername(principal.getName()));
        return fetchOwnerOrGuest(rooms, hostId, principal);
    }

    @GetMapping("/{id}")
    public @ResponseBody Room findById(
            @PathVariable("id") long id,
            final Principal principal,
            @RequestParam(value = "hostId", required = false) Long hostId)
            throws NotFoundException {
        Room room = roomRepository.findById(id).orElseThrow(NotFoundException::new);
        fetchOwnerOrGuest(null, hostId, principal);
        return room;
    }

    @PostMapping
    public @ResponseBody Room create(
            @Valid @RequestBody RoomSaveRequest r, final Principal principal) {

        final String username = principal.getName();
        final Long userId = userRepository.findByUsername(username).getId();
        final String img = r.getImage();
        final Icon icon = r.getIcon();

        final Room newRoom = new Room();
        newRoom.setUserId(userId);
        newRoom.setName(r.getName());
        newRoom.setImage(img);
        newRoom.setIcon(icon);

        return roomRepository.save(newRoom);
    }

    @PutMapping("/{id}")
    public @ResponseBody Room update(
            @PathVariable("id") long id, @RequestBody RoomSaveRequest r, final Principal principal)
            throws NotFoundException {
        final Room newRoom =
                roomRepository
                        .findByIdAndUsername(id, principal.getName())
                        .orElseThrow(NotFoundException::new);
        final String img = r.getImage();
        final Icon icon = r.getIcon();

        if (r.getName() != null) {
            newRoom.setName(r.getName());
        }

        if ("".equals(img)) {
            newRoom.setImage(null);
        } else if (img != null) {
            newRoom.setImage(img);
        }

        if (icon != null) {
            newRoom.setIcon(icon);
        }

        return roomRepository.save(newRoom);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id, final Principal principal)
            throws NotFoundException {
        switchRepository.deleteAllByRoomId(id);
        knobDimmerRepository.deleteAllByRoomId(id);
        buttonDimmerRepository.deleteAllByRoomId(id);
        final Room r =
                roomRepository
                        .findByIdAndUsername(id, principal.getName())
                        .orElseThrow(NotFoundException::new);
        List<Device> devices = deviceService.findAll(r.getId(), null, principal.getName());
        for (Device d : devices) {
            deviceService.deleteByIdAsOwner(d.getId(), principal.getName());
        }

        roomRepository.delete(r);
    }

    /**
     * Returns a List<Device> of all Devices that are present in a given room (identified by its
     * id).
     */
    @GetMapping(path = "/{roomId}/devices")
    public List<Device> getDevices(
            @PathVariable("roomId") long roomId,
            final Principal principal,
            @RequestParam(value = "hostId", required = false) Long hostId)
            throws NotFoundException {
        return deviceService.findAll(roomId, hostId, principal.getName());
    }
}
