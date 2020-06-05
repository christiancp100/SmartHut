package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import static ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils.toList;

import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceService {

    private final DeviceRepository<Device> deviceRepository;
    private final SceneService sceneService;
    private final RoomRepository roomRepository;
    private final AutomationService automationService;
    private final EagerUserRepository userRepository;
    private final DevicePopulationService devicePopulationService;
    private final DevicePropagationService devicePropagationService;
    private final ConditionRepository<Condition<?>> conditionRepository;

    @Autowired
    public DeviceService(
            DeviceRepository<Device> deviceRepository,
            SceneService sceneService,
            RoomRepository roomRepository,
            AutomationService automationService,
            EagerUserRepository userRepository,
            DevicePopulationService devicePopulationService,
            DevicePropagationService devicePropagationService,
            ConditionRepository<Condition<?>> conditionRepository) {
        this.deviceRepository = deviceRepository;
        this.sceneService = sceneService;
        this.roomRepository = roomRepository;
        this.automationService = automationService;
        this.userRepository = userRepository;
        this.devicePopulationService = devicePopulationService;
        this.devicePropagationService = devicePropagationService;
        this.conditionRepository = conditionRepository;
    }

    public void throwIfRoomNotOwned(Long roomId, String username) throws NotFoundException {
        final Room r =
                roomRepository
                        .findByIdAndUsername(roomId, username)
                        .orElseThrow(NotFoundException::new);
        if (!r.getId().equals(roomId)) throw new IllegalStateException();
    }

    public void triggerTriggers(Device device, final String username) {

        final long deviceId = device.getId();
        final List<Trigger<?>> triggers = automationService.findTriggersByDeviceId(deviceId);

        triggers.stream()
                .filter(Trigger::triggered)
                .map(Trigger::getAutomationId)
                .map(automationService::findByVerifiedId)
                .distinct()
                .filter(
                        a -> {
                            final List<Condition<?>> conditions =
                                    conditionRepository.findAllByAutomationId(a.getId());
                            if (conditions.isEmpty()) return true;
                            return conditions.stream().allMatch(Condition::triggered);
                        })
                .map(Automation::getScenes)
                .flatMap(Collection::stream)
                .distinct()
                .sorted(Comparator.comparing(ScenePriority::getPriority))
                .map(t -> sceneService.findByValidatedId(t.getSceneId()))
                .forEach(s -> sceneService.apply(s, username, true));
    }

    public List<Device> findAll(Long hostId, String username) throws NotFoundException {
        return findAll(null, hostId, username);
    }

    public <T extends Device> T saveAsGuest(T device, String guestUsername, Long hostId)
            throws NotFoundException {
        final User currentUser = userRepository.findByUsername(guestUsername);
        final User host = userRepository.findById(hostId).orElseThrow(NotFoundException::new);

        if (!host.getGuests().contains(currentUser)) throw new NotFoundException();
        devicePropagationService.renameIfDuplicate(device, host.getUsername());

        device = deviceRepository.save(device);
        devicePropagationService.propagateUpdateAsGuest(device, host, currentUser);
        return device;
    }

    public void deleteByIdAsOwner(Long id, String username) throws NotFoundException {
        devicePropagationService.deleteByIdAsOwner(id, username);
    }

    public void populateComputedFields(Iterable<Device> devices) {
        devicePopulationService.populateComputedFields(devices);
    }

    /**
     * Saves all the devices given in devices assuming that the owner updated them in one way or
     * another. Takes care of the appropriate websocket updates and trigger checking as well. No
     * checking is done to verify that the user whose username is given is in fact the owner of
     * these devices
     *
     * @param devices the list of devices to save
     * @param username the username of the owner of these devices
     * @param fromScene true if the update comes from the a scene application side effect. Disables
     *     trigger checking to avoid recursive invocations of automations
     * @param fromTrigger true if the update comes from a scene application executed by an
     *     automation. Propagates updated through socket to owner as well. No effect if fromScene is
     *     false.
     * @param <T> the type of device contained in the list
     * @return the updated list of devices, ready to be fed to GSON
     */
    public <T extends Device> List<T> saveAllAsOwner(
            Iterable<T> devices, String username, boolean fromScene, boolean fromTrigger) {
        List<T> toReturn =
                devicePropagationService.saveAllAsOwner(devices, username, fromScene, fromTrigger);
        if (!fromScene) {
            toReturn.forEach(d -> this.triggerTriggers(d, username));
        }
        return toReturn;
    }

    public <T extends Device> List<T> saveAllAsOwner(Iterable<T> devices, String username) {
        return saveAllAsOwner(devices, username, false, false);
    }

    public <T extends Device> T saveAsOwner(T device, String username) {
        T toReturn = devicePropagationService.saveAsOwner(device, username);
        triggerTriggers(toReturn, username);
        return device;
    }

    public List<Device> findAll(Long roomId, Long hostId, String username)
            throws NotFoundException {
        Iterable<Device> devices;
        User host = null;
        if (hostId == null) {
            if (roomId != null) {
                throwIfRoomNotOwned(roomId, username);
                devices = deviceRepository.findByRoomId(roomId);
            } else {
                devices = deviceRepository.findAllByUsername(username);
            }
        } else {
            final User guest = userRepository.findByUsername(username);
            host = userRepository.findById(hostId).orElseThrow(NotFoundException::new);

            if (!guest.getHosts().contains(host)) {
                throw new NotFoundException();
            }

            if (roomId != null) {
                Room r = roomRepository.findById(roomId).orElseThrow(NotFoundException::new);
                if (!r.getUserId().equals(hostId)) {
                    throw new NotFoundException();
                }
                devices = deviceRepository.findByRoomId(roomId);
            } else {
                devices = deviceRepository.findAllByUsername(host.getUsername());
            }
        }

        devicePopulationService.populateComputedFields(devices);

        return filterOutCamerasIfNeeded(host, devices);
    }

    private List<Device> filterOutCamerasIfNeeded(User host, Iterable<Device> devices) {
        if (host != null && !host.isCameraEnabled()) {
            return StreamSupport.stream(devices.spliterator(), true)
                    .filter(d -> !(d instanceof SecurityCamera))
                    .collect(Collectors.toList());
        } else {
            return toList(devices);
        }
    }
}
