package ch.usi.inf.sa4.sanmarinoes.smarthut;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class SmarthutApplicationTests extends SmartHutTest {

    @Autowired private TestRestTemplate restTemplate;

    @Test
    public void anonymousGreetingShouldNotBeAuthorized() {
        assertThat(this.restTemplate.getForEntity(getBaseURL(), Void.class).getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
