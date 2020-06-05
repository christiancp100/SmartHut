package ch.usi.inf.sa4.sanmarinoes.smarthut.utils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UtilsTests {

    @Test
    public void testToList() {
        List<String> hormannTitles = List.of("Prof.", "Dr.", "Kai (spiritual leader)");
        assertThat(Utils.toList(hormannTitles))
                .containsExactly(hormannTitles.get(0), hormannTitles.get(1), hormannTitles.get(2));
    }

    @Test
    public void testReturnIfGuest() {
        Principal principal = Mockito.mock(Principal.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        User host = new User();
        User guest = new User();
        host.getGuests().add(guest);

        when(userRepository.findById(1L)).thenReturn(Optional.of(host));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(guest);
        when(principal.getName()).thenReturn("user");

        try {
            assertThat(Utils.returnIfGuest(userRepository, "toReturn", 1L, principal))
                    .isEqualTo("toReturn");
        } catch (NotFoundException e) {
            fail(e.getMessage());
        }

        assertThatThrownBy(() -> Utils.returnIfGuest(userRepository, "toReturn", 2L, principal))
                .isInstanceOf(NotFoundException.class);
    }
}
