package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StateRepository<T extends State> extends CrudRepository<T, Long> {

    @Transactional
    void deleteAllBySceneId(long roomId);

    List<T> findBySceneId(@Param("sceneId") long sceneId);

    Integer countByDeviceIdAndSceneId(long deviceId, long sceneId);
}
