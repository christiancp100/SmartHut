package ch.usi.inf.sa4.sanmarinoes.smarthut.socket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.getField;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonConfig;
import ch.usi.inf.sa4.sanmarinoes.smarthut.config.JWTTokenUtils;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ButtonDimmer;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Device;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DevicePopulationService;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SensorSocketEndpointTests {

    @InjectMocks private SensorSocketEndpoint sensorSocketEndpoint;

    private final Gson gson = GsonConfig.socketGson();

    @Mock private DevicePopulationService deviceService;

    @Mock private Session session;

    @Mock private JWTTokenUtils jwtTokenUtils;

    @Mock private UserRepository userRepository;

    private final User u;

    public SensorSocketEndpointTests() {
        u = new User();
        u.setName("user");
        u.setId(1L);
    }

    @Test
    public void testQueueDeviceUpdate() {
        doNothing().when(deviceService).populateComputedFields(any());
        Device d = new ButtonDimmer();

        User u = new User();
        u.setId(1L);

        sensorSocketEndpoint.queueDeviceUpdate(d, u, true, 42L, true);
        assertThat(d.isFromGuest()).isTrue();
        assertThat(d.getFromHostId()).isEqualTo(42L);
        assertThat(d.isDeleted()).isTrue();

        final boolean[] done = new boolean[1];

        final SensorSocketEndpoint endpoint = Mockito.spy(sensorSocketEndpoint);

        doAnswer(
                        i -> {
                            if (done[0]) fail("Broadcast called more than once");
                            final User us = (User) i.getArguments()[0];
                            @SuppressWarnings("unchecked")
                            final Collection<String> jsons =
                                    (Collection<String>) i.getArguments()[1];
                            assertThat(us).isSameAs(u);
                            assertThat(jsons).containsExactly(gson.toJson(d));
                            done[0] = true;
                            return null;
                        })
                .when(endpoint)
                .broadcast(eq(u), any());

        endpoint.flushDeviceUpdates();

        assertThat(done[0]).isTrue();
    }

    @Test
    public void testCheckToken() throws IOException {
        when(userRepository.findByUsername("user")).thenReturn(u);
        when(session.getRequestParameterMap())
                .thenReturn(Map.of("token", List.of("randomgarbage")));
        when(jwtTokenUtils.getUsernameFromToken("randomgarbage")).thenReturn("user");
        when(jwtTokenUtils.isTokenExpired("randomgarbage")).thenReturn(false);
        sensorSocketEndpoint.onOpen(session, null);
        @SuppressWarnings("unchecked")
        Multimap<User, Session> map =
                (Multimap<User, Session>)
                        getField(
                                sensorSocketEndpoint,
                                SensorSocketEndpoint.class,
                                "authorizedClients");
        assertThat(map).isNotNull();
        assertThat(map.size()).isEqualTo(1);
        assertThat(map.entries().iterator().next().getKey()).isSameAs(u);
        assertThat(map.entries().iterator().next().getValue()).isSameAs(session);
        final Session closedSession = Mockito.mock(Session.class);
        when(closedSession.isOpen()).thenReturn(false);
        when(session.isOpen()).thenReturn(true);
        map.put(u, closedSession);

        boolean[] sent = new boolean[1];
        final RemoteEndpoint.Basic b = Mockito.mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(b);

        doAnswer(
                        i -> {
                            sent[0] = true;
                            return null;
                        })
                .when(b)
                .sendText("[\"Malusa\",\"Luciano\"]");

        sensorSocketEndpoint.broadcast(null, List.of());
        sensorSocketEndpoint.broadcast(u, List.of("\"Malusa\"", "\"Luciano\""));
        assertThat(sent[0]).isTrue();
        assertThat(map.get(u)).containsExactly(session);
    }
}
