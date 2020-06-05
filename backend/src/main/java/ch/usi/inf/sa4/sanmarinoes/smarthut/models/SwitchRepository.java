package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.transaction.Transactional;

public interface SwitchRepository extends DeviceRepository<Switch> {

    @Transactional
    void deleteAllByRoomId(long roomId);
}
