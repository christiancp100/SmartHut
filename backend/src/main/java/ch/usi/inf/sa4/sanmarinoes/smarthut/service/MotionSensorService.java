package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.MotionSensor;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.MotionSensorRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.socket.SensorSocketEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MotionSensorService {

    @Autowired private SensorSocketEndpoint sensorSocketEndpoint;
    @Autowired private DeviceService deviceService;
    @Autowired private MotionSensorRepository motionSensorRepository;

    /**
     * Updates detection status of given motion sensor and propagates update through socket
     *
     * @param sensor the motion sensor to update
     * @param detected the new detection status
     * @return the updated motion sensor
     */
    public MotionSensor updateDetectionFromMotionSensor(
            MotionSensor sensor, boolean detected, String username) {
        sensor.setDetected(detected);
        final MotionSensor toReturn = deviceService.saveAsOwner(sensor, username);

        sensorSocketEndpoint.queueDeviceUpdate(
                sensor, motionSensorRepository.findUser(sensor.getId()), false, null, false);

        return toReturn;
    }
}
