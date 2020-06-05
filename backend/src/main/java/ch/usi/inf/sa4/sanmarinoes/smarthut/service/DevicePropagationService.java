package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import static ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils.toList;

import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Device;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DeviceRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.EagerUserRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.socket.SensorSocketEndpoint;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DevicePropagationService {

    private final SensorSocketEndpoint endpoint;
    private final EagerUserRepository userRepository;
    private final DeviceRepository<Device> deviceRepository;

    @Autowired
    public DevicePropagationService(
            SensorSocketEndpoint endpoint,
            EagerUserRepository userRepository,
            DeviceRepository<Device> deviceRepository) {
        this.endpoint = endpoint;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    void propagateUpdateAsGuest(Device device, User host, User guest) {
        final Set<User> guests = Set.copyOf(host.getGuests());

        // We're telling the host that a guest has modified a device. Therefore, fromGuest becomes
        // true.
        // broadcast device update to host
        endpoint.queueDeviceUpdate(device, host, true, null, false);

        // We're telling all guests that a higher entity has issued a device update. Therefore,
        // fromHost becomes true.
        for (final User aGuest : guests) {
            if (aGuest.equals(guest)) {
                continue;
            }
            // enqueue all device updates for all other guests
            endpoint.queueDeviceUpdate(device, aGuest, false, host.getId(), false);
        }
    }

    void saveAllAsGuestSceneApplication(List<Device> devices, String guestUsername, Long hostId) {
        final User guest = userRepository.findByUsername(guestUsername);
        final User host = userRepository.findById(hostId).orElseThrow(IllegalStateException::new);
        deviceRepository.saveAll(devices);
        devices.forEach(d -> this.propagateUpdateAsGuest(d, host, guest));
    }

    void renameIfDuplicate(Device toCreate, String username) {
        while (deviceRepository.findDuplicates(toCreate.getName(), username)
                        - (toCreate.getId() <= 0 ? 0 : 1)
                > 0) {
            toCreate.setName(toCreate.getName() + " (new)");
        }
    }

    public <T extends Device> T saveAsGuest(T device, String guestUsername, Long hostId)
            throws NotFoundException {
        final User currentUser = userRepository.findByUsername(guestUsername);
        final User host = userRepository.findById(hostId).orElseThrow(NotFoundException::new);
        if (!host.getGuests().contains(currentUser)) throw new NotFoundException();
        renameIfDuplicate(device, host.getUsername());

        device = deviceRepository.save(device);
        propagateUpdateAsGuest(device, host, currentUser);
        return device;
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
        devices.forEach(d -> renameIfDuplicate(d, username));
        devices = deviceRepository.saveAll(devices);
        devices.forEach(d -> propagateUpdateAsOwner(d, username, fromScene && fromTrigger));

        return toList(devices);
    }

    public <T extends Device> List<T> saveAllAsOwner(Iterable<T> devices, String username) {
        return saveAllAsOwner(devices, username, false, false);
    }

    public <T extends Device> T saveAsOwner(T device, String username) {
        renameIfDuplicate(device, username);
        device = deviceRepository.save(device);
        propagateUpdateAsOwner(device, username, false);

        return device;
    }

    public void deleteByIdAsOwner(Long id, String username) throws NotFoundException {
        Device d =
                deviceRepository
                        .findByIdAndUsername(id, username)
                        .orElseThrow(NotFoundException::new);

        final User user = userRepository.findByUsername(username);
        final Set<User> guests = user.getGuests();
        // make sure we're broadcasting from host
        for (final User guest : guests) {
            // broadcast to endpoint the object device, with receiving user set to guest
            endpoint.queueDeviceUpdate(d, guest, false, user.getId(), true);
        }

        deviceRepository.delete(d);
    }

    /**
     * Propagates the update through the socket assuming that the user that modified the device is
     * the owner of that device
     *
     * @param device the updated device
     * @param username the username of the owner of that device
     * @param causedByTrigger if true, send the update to the owner as well
     */
    void propagateUpdateAsOwner(Device device, String username, boolean causedByTrigger) {
        final User user = userRepository.findByUsername(username);
        final Set<User> guests = user.getGuests();
        // make sure we're broadcasting from host
        for (final User guest : guests) {
            // broadcast to endpoint the object device, with receiving user set to guest
            endpoint.queueDeviceUpdate(device, guest, false, user.getId(), false);
        }

        if (causedByTrigger) {
            endpoint.queueDeviceUpdate(device, user, false, user.getId(), false);
        }
    }
}
