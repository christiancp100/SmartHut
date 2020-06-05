package ch.usi.inf.sa4.sanmarinoes.smarthut.utils;

import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** A class with a bunch of useful static methods */
public final class Utils {
    private Utils() {}

    public static <U> U returnIfGuest(
            UserRepository userRepository, U toReturn, Long hostId, Principal principal)
            throws NotFoundException {
        User host = userRepository.findById(hostId).orElseThrow(NotFoundException::new);
        User guest = userRepository.findByUsername(principal.getName());
        if (!host.getGuests().contains(guest)) {
            throw new NotFoundException();
        } else {
            return toReturn;
        }
    }

    public static <T> List<T> toList(Iterable<? extends T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }
}
