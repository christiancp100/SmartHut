package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils.returnIfGuest;

import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Device;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.DeviceRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import java.security.Principal;

public abstract class GuestEnabledController<T extends Device> {

    private UserRepository userRepository;
    private DeviceRepository<T> deviceRepository;

    public GuestEnabledController(
            final UserRepository userRepository, final DeviceRepository<T> deviceRepository) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
    }

    protected T fetchIfOwnerOrGuest(final Principal principal, Long id, Long hostId)
            throws NotFoundException {
        if (hostId == null) {
            return deviceRepository
                    .findByIdAndUsername(id, principal.getName())
                    .orElseThrow(NotFoundException::new);
        } else {
            /*
             * Slightly less extremely verbose check through various repositories to control user/guest authorization.
             */
            T device =
                    deviceRepository
                            .findByIdAndUserId(id, hostId)
                            .orElseThrow(NotFoundException::new);
            return returnIfGuest(userRepository, device, hostId, principal);
        }
    }
}
