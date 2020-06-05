package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;

public interface ThermostatRepository extends DeviceRepository<Thermostat> {

    /**
     * Finds all devices belonging to a user
     *
     * @param username the User's username
     * @return all devices of that user
     */
    @Transactional
    @Query("SELECT t FROM Thermostat t JOIN t.room r JOIN r.user u WHERE u.username = ?1")
    List<Thermostat> findAllByUsername(String username);

    /**
     * Computes the average temperature of all temperature sensors in the room
     *
     * @param thermostatRoomId room ID of the thermostat
     * @return an optional big decimal, empty if none found
     */
    @Query("SELECT AVG(s.value) FROM Sensor s JOIN s.room r WHERE s.sensor = ?2 AND r.id = ?1")
    Optional<BigDecimal> getAverageTemperature(Long thermostatRoomId, Sensor.SensorType sensorType);
}
