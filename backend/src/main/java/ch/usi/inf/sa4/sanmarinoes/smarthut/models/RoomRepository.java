package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long> {

    /**
     * Finds a room by their id and a username
     *
     * @param id the room id
     * @param username a User's username
     * @return an optional device, empty if none found
     */
    @Query("SELECT r FROM Room r JOIN r.user u WHERE r.id = ?1 AND u.username = ?2")
    Optional<Room> findByIdAndUsername(Long id, String username);

    @Query("SELECT r FROM Room r JOIN r.user u WHERE u.username = ?1")
    List<Room> findByUsername(String username);

    @Query("SELECT r FROM Room r JOIN r.user u WHERE u.id = ?1")
    List<Room> findByUserId(Long hostId);
}
