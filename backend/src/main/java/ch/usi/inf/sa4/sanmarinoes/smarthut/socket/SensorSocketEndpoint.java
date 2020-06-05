package ch.usi.inf.sa4.sanmarinoes.smarthut.socket;

import ch.usi.inf.sa4.sanmarinoes.smarthut.config.GsonConfig;
import ch.usi.inf.sa4.sanmarinoes.smarthut.config.JWTTokenUtils;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Device;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.User;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.UserRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DevicePopulationService;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.*;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Endpoint of socket at URL /sensor-socket used to update the client with sensor information */
@Component
public class SensorSocketEndpoint extends Endpoint {

    private static final Logger logger = LoggerFactory.getLogger(SensorSocketEndpoint.class);

    private final Gson gson = GsonConfig.socketGson();

    private final DevicePopulationService deviceService;

    private final UserRepository userRepository;

    private final JWTTokenUtils jwtTokenUtils;

    private final Multimap<User, Session> authorizedClients =
            Multimaps.synchronizedMultimap(HashMultimap.create());

    // messages are now stored as strings as a "hack" to capture and clone the state of the device,
    // since
    // fromHost and fromGuest are just mutable properties and hibernate caches the object.
    private final Map<User, Map<Long, String>> messages = new HashMap<>();

    @Autowired
    public SensorSocketEndpoint(
            UserRepository userRepository,
            JWTTokenUtils jwtTokenUtils,
            DevicePopulationService deviceService) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.userRepository = userRepository;
        this.deviceService = deviceService;
    }

    /**
     * Queues a single device update for a certain user to be sent
     *
     * @param device the device update to be sent
     * @param u the user the device belongs
     * @param fromGuest value for device.fromGuest. This will be put in the device passed.
     * @param fromHostId value for device.fromHostId. This will be put in the device passed.
     * @param deleted value for device.deleted. This will be put in the device passed.
     */
    public void queueDeviceUpdate(
            Device device, User u, boolean fromGuest, Long fromHostId, boolean deleted) {
        synchronized (messages) {
            device.setFromGuest(fromGuest);
            device.setFromHostId(fromHostId);
            device.setDeleted(deleted);

            // sort of an hack: force the population of thermostat measureTemperature and other
            // possible
            // computed fields in the future. This should already be done by the callers of this
            // method but for
            // whatever reason they don't do it.
            deviceService.populateComputedFields(List.of(device));

            messages.putIfAbsent(u, new HashMap<>());
            messages.get(u).put(device.getId(), gson.toJson(device));
        }
    }

    /** Sends all device updates queued to be sent in a unique WebSocket message */
    public void flushDeviceUpdates() {
        synchronized (messages) {
            for (Map.Entry<User, Map<Long, String>> batchForUser : messages.entrySet()) {
                broadcast(batchForUser.getKey(), batchForUser.getValue().values());
                batchForUser.getValue().clear();
            }
        }
    }

    /**
     * Given a collection of messages and a user, broadcasts that message in json to all associated
     * clients
     *
     * @param messages the message batch to send
     * @param u the user to which to send the message
     */
    void broadcast(User u, Collection<String> messages) {
        if (messages.isEmpty()) return;
        final HashSet<Session> sessions = new HashSet<>(authorizedClients.get(u));
        for (Session s : sessions) {
            try {
                if (s.isOpen()) {
                    s.getBasicRemote().sendText("[" + String.join(",", messages) + "]");
                } else {
                    authorizedClients.remove(u, s);
                }
            } catch (IOException e) {
                logger.warn(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * Handles the opening of a socket session with a client
     *
     * @param session the newly born session
     * @param config endpoint configuration
     */
    @Override
    public void onOpen(Session session, EndpointConfig config) {
        final List<String> tokenQuery = session.getRequestParameterMap().get("token");
        User u;
        if (!tokenQuery.isEmpty() && (u = checkToken(tokenQuery.get(0))) != null) {
            authorizedClients.put(u, session);
        } else {
            try {
                session.close();
            } catch (IOException e) {
                logger.warn(e.getLocalizedMessage(), e);
            }
        }
    }

    private User checkToken(String protocolString) {
        String username;

        try {
            username = jwtTokenUtils.getUsernameFromToken(protocolString);
        } catch (Exception ignored) {
            logger.info("Token format not valid");
            return null;
        }

        final User user = userRepository.findByUsername(username);
        if (user != null && !jwtTokenUtils.isTokenExpired(protocolString)) {
            return user;
        } else {
            return null;
        }
    }
}
