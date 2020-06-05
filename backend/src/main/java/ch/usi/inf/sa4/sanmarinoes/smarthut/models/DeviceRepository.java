package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * DeviceRepository acts as a superclass for the other repositories so to mirror in the database the
 * class inheritance present among the various devices.
 */
public interface DeviceRepository<T extends Device> extends CrudRepository<T, Long> {
    List<T> findByRoomId(@Param("roomId") long roomId);

    @Query(
            "SELECT COUNT(d) FROM Device d JOIN d.room r JOIN r.user u WHERE d.name = ?1 AND u.username = ?2")
    Integer findDuplicates(String name, String username);

    /**
     * Finds devices by their id and a username
     *
     * @param id the device id
     * @param username a User's username
     * @return an optional device, empty if none found
     */
    @Transactional
    @Query("SELECT d FROM Device d JOIN d.room r JOIN r.user u WHERE d.id = ?1 AND u.username = ?2")
    Optional<T> findByIdAndUsername(Long id, String username);

    /**
     * Finds devices by their id and a user id
     *
     * @param id the device id
     * @param userId a User's id
     * @return an optional device, empty if none found
     */
    @Query("SELECT d FROM Device d JOIN d.room r JOIN r.user u WHERE d.id = ?1 AND u.id = ?2")
    Optional<T> findByIdAndUserId(Long id, Long userId);

    /**
     * Finds all devices belonging to a user
     *
     * @param username the User's username
     * @return all devices of that user
     */
    @Transactional
    @Query("SELECT d FROM Device d JOIN d.room r JOIN r.user u WHERE u.username = ?1")
    List<T> findAllByUsername(String username);

    /**
     * Find the user associated with a device through a room
     *
     * @param deviceId the device id
     * @return a user object
     */
    @Transactional
    @Query("SELECT u FROM Device d JOIN d.room r JOIN r.user u WHERE d.id = ?1")
    User findUser(Long deviceId);
}
