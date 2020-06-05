package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static ch.usi.inf.sa4.sanmarinoes.smarthut.utils.Utils.toList;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.GuestPermissionsRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.GuestsUpdateRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.UserResponse;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.EagerUserRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
public class GuestController {

    @Autowired private EagerUserRepository userRepository;

    @GetMapping
    public List<UserResponse> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    @GetMapping("/hosts")
    public List<UserResponse> findHosts(final Principal principal) {
        final User u = userRepository.findByUsername(principal.getName());
        return u.getHosts().stream().map(UserResponse::fromUser).collect(Collectors.toList());
    }

    @GetMapping("/guests")
    public List<UserResponse> findGuests(final Principal principal) {
        final User u = userRepository.findByUsername(principal.getName());
        return u.getGuests().stream().map(UserResponse::fromUser).collect(Collectors.toList());
    }

    @PutMapping("/guests")
    public List<User> setGuests(
            @RequestBody @Valid GuestsUpdateRequest g, final Principal principal) {
        Iterable<User> guests = userRepository.findAllById(g.getIds());
        User host = userRepository.findByUsername(principal.getName());

        for (final User oldGuest : host.getGuests()) {
            oldGuest.getHosts().remove(host);
        }

        final Set<User> oldGuests = Set.copyOf(host.getGuests());

        for (final User guest : guests) {
            host.addGuest(guest);
            guest.addHost(host);
        }

        userRepository.saveAll(oldGuests);
        userRepository.save(host);
        return toList(userRepository.saveAll(guests));
    }

    @PutMapping("/permissions")
    public User updatePermissions(
            @Valid @RequestBody GuestPermissionsRequest g, final Principal principal) {
        final User currentUser = userRepository.findByUsername(principal.getName());
        currentUser.setCameraEnabled(g.isCameraEnabled());
        return userRepository.save(currentUser);
    }
}
