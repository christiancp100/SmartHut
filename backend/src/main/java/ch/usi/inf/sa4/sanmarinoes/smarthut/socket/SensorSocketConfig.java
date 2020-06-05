package ch.usi.inf.sa4.sanmarinoes.smarthut.socket;

import javax.websocket.server.ServerEndpointConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServerEndpointRegistration;

/** Configures the sensor socket and maps it to the /sensor-socket path */
@Configuration
public class SensorSocketConfig extends ServerEndpointConfig.Configurator {

    private final SensorSocketEndpoint instance;

    @Autowired
    public SensorSocketConfig(SensorSocketEndpoint instance) {
        this.instance = instance;
    }

    /**
     * Registers the sensor socket endpoint to the url /sensor-socket
     *
     * @return an endpoint registration object
     */
    @Bean
    public ServerEndpointRegistration serverEndpointRegistration() {
        return new ServerEndpointRegistration("/sensor-socket", instance);
    }

    /**
     * Returns a new ServerEndpointExporter
     *
     * @return a new ServerEndpointExporter
     */
    @Bean
    public ServerEndpointExporter endpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        try {
            //noinspection unchecked
            return (T) this.instance;
        } catch (ClassCastException e) {
            final var e2 =
                    new InstantiationException("Cannot cast SensorSocketEndpoint to desired type");
            e2.initCause(e);
            throw e2;
        }
    }
}
