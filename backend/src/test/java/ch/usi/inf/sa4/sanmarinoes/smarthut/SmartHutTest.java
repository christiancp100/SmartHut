package ch.usi.inf.sa4.sanmarinoes.smarthut;

import static org.assertj.core.api.Assertions.assertThat;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.UserRegistrationRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ConfirmationToken;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ConfirmationTokenRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class SmartHutTest {
    private static boolean setupDone = false;

    protected final String getBaseURL() {
        return "http://localhost:2000/";
    }

    protected final String url(final String url) {
        return getBaseURL() + url;
    }

    protected void setUp() {}

    protected static final UserRegistrationRequest enabledUser = new UserRegistrationRequest();

    static {
        enabledUser.setName("Enabled User");
        enabledUser.setEmail("enabled@example.com");
        enabledUser.setUsername("enabled");
        enabledUser.setPassword("password");
    }

    protected void registerTestUser(
            final TestRestTemplate restTemplate,
            final UserRepository userRepository,
            final ConfirmationTokenRepository tokenRepository) {
        final ResponseEntity<Object> res2 =
                restTemplate.postForEntity(this.url("/register"), enabledUser, Object.class);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.OK);

        final User persistedEnabledUser = userRepository.findByUsername("enabled");
        final ConfirmationToken token = tokenRepository.findByUser(persistedEnabledUser);

        final ResponseEntity<Void> res3 =
                WebClient.create(getBaseURL())
                        .get()
                        .uri("/register/confirm-account?token=" + token.getConfirmToken())
                        .retrieve()
                        .toBodilessEntity()
                        .block();

        assertThat(res3).isNotNull();
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(userRepository.findByUsername("enabled").isEnabled()).isTrue();
    }

    @BeforeEach
    void setUpHack() {
        if (!setupDone) {
            setUp();
            setupDone = true;
        }
    }
}
