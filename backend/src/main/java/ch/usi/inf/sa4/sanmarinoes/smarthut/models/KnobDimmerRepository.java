package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.transaction.Transactional;

public interface KnobDimmerRepository extends DeviceRepository<KnobDimmer> {

    @Transactional
    void deleteAllByRoomId(long roomId);
}
