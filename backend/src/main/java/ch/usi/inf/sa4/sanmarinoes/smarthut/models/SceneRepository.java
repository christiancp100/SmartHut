package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SceneRepository extends CrudRepository<Scene, Long> {

    /**
     * Finds a room by their id and a username
     *
     * @param id the scene id
     * @param username a User's username
     * @return an optional scene, empty if none found
     */
    @Query("SELECT s FROM Scene s JOIN s.user u WHERE s.id = ?1 AND u.username = ?2")
    Optional<Scene> findByIdAndUsername(Long id, String username);

    @Query("SELECT s FROM Scene s JOIN s.user u WHERE u.username = ?1")
    List<Scene> findByUsername(String username);

    @Query("SELECT s FROM Scene s JOIN s.user u WHERE u.id = ?1 AND s.guestAccessEnabled = true")
    List<Scene> findByHostId(Long hostId);

    Optional<Scene> findByIdAndUserIdAndGuestAccessEnabled(
            Long id, Long userId, boolean guestAccessEnabled);
}
