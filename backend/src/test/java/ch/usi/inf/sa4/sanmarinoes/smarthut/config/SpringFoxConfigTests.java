package ch.usi.inf.sa4.sanmarinoes.smarthut.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SpringFoxConfigTests {

    private final SpringFoxConfig springFoxConfig = new SpringFoxConfig();

    @Test
    public void testApi() {
        assertThat(springFoxConfig.api()).isNotNull();
    }
}
