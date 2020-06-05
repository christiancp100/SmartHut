package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.mockito.Mockito.when;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.UserRegistrationRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.DuplicateRegistrationException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "user")
public class UserAccountControllerTests {

    @InjectMocks private UserAccountController userAccountController;

    @Mock private UserRepository userRepository;

    @Test
    public void testRegisterUser() {
        final UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        registrationRequest.setEmail("info@theshell.ch");
        registrationRequest.setUsername("username");

        when(userRepository.findByEmailIgnoreCase("info@theshell.ch")).thenReturn(null);
        when(userRepository.findByEmailIgnoreCase("info@vimtok.com")).thenReturn(new User());
        when(userRepository.findByUsername("username")).thenReturn(new User());
        when(userRepository.findByUsername("simoneriva")).thenReturn(null);

        Assertions.assertThatThrownBy(() -> userAccountController.registerUser(registrationRequest))
                .isInstanceOf(DuplicateRegistrationException.class);

        registrationRequest.setUsername("simoneriva");
        registrationRequest.setEmail("info@vimtok.com");

        Assertions.assertThatThrownBy(() -> userAccountController.registerUser(registrationRequest))
                .isInstanceOf(DuplicateRegistrationException.class);
    }
}
