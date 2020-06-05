package ch.usi.inf.sa4.sanmarinoes.smarthut.scheduled;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.MotionSensorService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.SensorService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.ThermostatService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.socket.SensorSocketEndpoint;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Generates fake sensor (and motion sensor) and smart plug consumption updates as required by
 * milestone one
 */
@Component
public class UpdateTasks {

    @Autowired private SensorRepository sensorRepository;

    @Autowired private MotionSensorRepository motionSensorRepository;

    @Autowired private SmartPlugRepository smartPlugRepository;

    @Autowired private SensorService sensorService;

    @Autowired private ThermostatService thermostatService;

    @Autowired private MotionSensorService motionSensorService;

    @Autowired private SensorSocketEndpoint sensorSocketEndpoint;

    /** Generates fake sensor updates every two seconds with a +/- 2.5% error */
    @Scheduled(fixedRate = 2000)
    public void sensorFakeUpdate() {
        sensorService.sensorFakeUpdate();
    }

    /** Generates fake sensor updates every two seconds with a +/- 2.5% error */
    @Scheduled(fixedRate = 2000)
    public void thermostatInteralSensorFakeUpdate() {
        thermostatService.fakeUpdateAll();
    }

    /**
     * Generate fake motion detections in all motion detectors every 20 seconds for 2 seconds at
     * most
     */
    @Scheduled(fixedDelay = 20000)
    public void motionSensorFakeUpdate() {
        StreamSupport.stream(motionSensorRepository.findAll().spliterator(), true)
                .forEach(
                        sensor -> {
                            final User owner = motionSensorRepository.findUser(sensor.getId());
                            motionSensorService.updateDetectionFromMotionSensor(
                                    sensor, true, owner.getUsername());
                            CompletableFuture.delayedExecutor(
                                            (long) (Math.random() * 2000), TimeUnit.MILLISECONDS)
                                    .execute(
                                            () ->
                                                    motionSensorService
                                                            .updateDetectionFromMotionSensor(
                                                                    sensor,
                                                                    false,
                                                                    owner.getUsername()));
                        });
    }

    /** Updates power consumption of all activated smart plugs every second */
    @Scheduled(fixedDelay = 1000)
    public void smartPlugConsumptionFakeUpdate() {
        smartPlugRepository.updateTotalConsumption(SmartPlug.AVERAGE_CONSUMPTION_KW);
        final Collection<SmartPlug> c = smartPlugRepository.findByOn(true);
        c.forEach(
                s ->
                        sensorSocketEndpoint.queueDeviceUpdate(
                                s, sensorRepository.findUser(s.getId()), false, null, false));
    }

    /** Sends device updates through sensor socket in batch every one second */
    @Scheduled(fixedDelay = 1000)
    public void socketFlush() {
        sensorSocketEndpoint.flushDeviceUpdates();
    }
}
