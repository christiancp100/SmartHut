package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import javax.transaction.Transactional;

public interface ButtonDimmerRepository extends DeviceRepository<ButtonDimmer> {

    @Transactional
    void deleteAllByRoomId(long roomId);
}
