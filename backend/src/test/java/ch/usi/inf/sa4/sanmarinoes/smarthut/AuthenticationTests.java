package ch.usi.inf.sa4.sanmarinoes.smarthut;

import static org.assertj.core.api.Assertions.assertThat;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.JWTRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.JWTResponse;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.UserRegistrationRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.UnauthorizedException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ConfirmationTokenRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class AuthenticationTests extends SmartHutTest {

    @Autowired private TestRestTemplate restTemplate;

    @Autowired private UserRepository userRepository;

    @Autowired private ConfirmationTokenRepository tokenRepository;

    private UserRegistrationRequest getDisabledUser() {
        final UserRegistrationRequest disabledUser = new UserRegistrationRequest();
        disabledUser.setName("Disabled User");
        disabledUser.setEmail("disabled@example.com");
        disabledUser.setUsername("disabled");
        disabledUser.setPassword("password");
        return disabledUser;
    }

    @Override
    protected void setUp() {
        final ResponseEntity<Object> res =
                this.restTemplate.postForEntity(
                        this.url("/register"), getDisabledUser(), Object.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        registerTestUser(restTemplate, userRepository, tokenRepository);
    }

    @Test
    public void registrationShouldReturnBadRequestWithIncorrectFields() {
        final Map<String, Object> badJSON = Map.of("luciano", "goretti", "danilo", "malusa");

        assertThat(
                        this.restTemplate
                                .postForEntity(url("/register"), badJSON, JWTResponse.class)
                                .getStatusCode()
                                .equals(HttpStatus.BAD_REQUEST))
                .isTrue();
    }

    @Test
    public void registrationShouldReturnBadRequestWithShortPassword() {
        final UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Mario Goretti");
        request.setEmail("test@example.com");
        request.setUsername("mgo");
        request.setPassword("passw");

        final ResponseEntity<JsonObject> res =
                this.restTemplate.postForEntity(url("/register"), request, JsonObject.class);
        assertThat(res.getStatusCode().equals(HttpStatus.BAD_REQUEST)).isTrue();
        assertThat(res.getBody()).isNotNull();

        final JsonArray errors = res.getBody().getAsJsonArray("errors");
        assertThat(errors)
                .allSatisfy(
                        e ->
                                assertThat(e.getAsJsonObject().get("field").getAsString())
                                        .isEqualTo("password"));
    }

    @Test
    public void registrationShouldReturnBadRequestWithWrongEmail() {
        final UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Mario Goretti");
        request.setEmail("test@example");
        request.setUsername("mgo");
        request.setPassword("password");

        final ResponseEntity<JsonObject> res =
                this.restTemplate.postForEntity(url("/register"), request, JsonObject.class);
        assertThat(res.getStatusCode().equals(HttpStatus.BAD_REQUEST)).isTrue();
        assertThat(res.getBody()).isNotNull();

        final JsonArray errors = res.getBody().getAsJsonArray("errors");
        assertThat(errors)
                .allSatisfy(
                        e ->
                                assertThat(e.getAsJsonObject().get("field").getAsString())
                                        .isEqualTo("email"));
    }

    @Test
    public void registrationShouldReturnBadRequestWithNoName() {
        final UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("test@example.com");
        request.setUsername("mgo");
        request.setPassword("password");

        final ResponseEntity<JsonObject> res =
                this.restTemplate.postForEntity(url("/register"), request, JsonObject.class);
        assertThat(res.getStatusCode().equals(HttpStatus.BAD_REQUEST)).isTrue();
        assertThat(res.getBody() != null).isTrue();

        final JsonArray errors = res.getBody().getAsJsonArray("errors");
        assertThat(errors)
                .allSatisfy(
                        e ->
                                assertThat(e.getAsJsonObject().get("field").getAsString())
                                        .isEqualTo("name"));
    }

    @Test
    public void registrationShouldReturnBadRequestWithNoUsername() {
        final UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Mario Goretti");
        request.setEmail("test@example.com");
        request.setPassword("password");

        final ResponseEntity<JsonObject> res =
                this.restTemplate.postForEntity(url("/register"), request, JsonObject.class);
        assertThat(res.getStatusCode().equals(HttpStatus.BAD_REQUEST)).isTrue();
        assertThat(res.getBody() != null).isTrue();

        final JsonArray errors = res.getBody().getAsJsonArray("errors");
        assertThat(errors)
                .allSatisfy(
                        j ->
                                assertThat(j.getAsJsonObject().get("field").getAsString())
                                        .isEqualTo("username"));
    }

    @Test
    public void registrationShouldReturnOkWithCorrectData() {
        final UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Registration Test");
        request.setUsername("smarthut");
        request.setEmail("smarthut.sm@example.com");
        request.setPassword("password");

        final ResponseEntity<Object> res =
                this.restTemplate.postForEntity(url("/register"), request, Object.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void loginShouldReturnBadRequestWithIncorrectFields() {
        final Map<String, Object> badJSON = Map.of("badkey", 3, "password", "ciaomamma");

        assertThat(
                        this.restTemplate
                                .postForEntity(url("/auth/login"), badJSON, JWTResponse.class)
                                .getStatusCode()
                                .equals(HttpStatus.BAD_REQUEST))
                .isTrue();
    }

    @Test
    public void loginShouldReturnUnauthorizedWithNonExistantUser() {
        final JWTRequest request = new JWTRequest();
        request.setUsernameOrEmail("roberto");
        request.setPassword("ciaomamma");

        final ResponseEntity<UnauthorizedException> res =
                this.restTemplate.postForEntity(
                        url("/auth/login"), request, UnauthorizedException.class);
        assertThat(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED)).isTrue();
        assertThat(res.getBody() != null).isTrue();
        assertThat(!res.getBody().isUserDisabled()).isTrue();
    }

    @Test
    public void loginShouldReturnUnauthorizedWithDisabledUser() {
        final JWTRequest request = new JWTRequest();
        request.setUsernameOrEmail("disabled");
        request.setPassword("password");

        final ResponseEntity<UnauthorizedException> res =
                this.restTemplate.postForEntity(
                        url("/auth/login"), request, UnauthorizedException.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(res.getBody()).isNotNull();
    }
}
