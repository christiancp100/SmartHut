package ch.usi.inf.sa4.sanmarinoes.smarthut.models;

import java.util.List;

public interface SensorRepository extends DeviceRepository<Sensor> {
    List<Sensor> findAllBySensor(Sensor.SensorType sensor);
}
