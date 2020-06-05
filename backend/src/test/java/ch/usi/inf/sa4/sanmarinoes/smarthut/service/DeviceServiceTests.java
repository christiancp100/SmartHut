package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTests {

    @Mock private DeviceRepository<Device> deviceRepository;

    @Mock private SceneService sceneService;

    @Mock private RoomRepository roomRepository;

    @Mock private AutomationService automationService;

    @Mock private EagerUserRepository userRepository;

    @Mock private DevicePopulationService devicePopulationService;

    @Mock private DevicePropagationService devicePropagationService;

    @Mock private ConditionRepository<Condition<?>> conditionRepository;

    @InjectMocks private DeviceService deviceService;

    @Test
    public void testThrowIfRoomNotOwned() {
        final Room r = new Room();
        r.setId(1L);

        when(roomRepository.findByIdAndUsername(1L, "user")).thenReturn(Optional.of(r));
        when(roomRepository.findByIdAndUsername(2L, "user")).thenReturn(Optional.empty());

        try {
            deviceService.throwIfRoomNotOwned(1L, "user");
        } catch (NotFoundException e) {
            fail(e.getMessage());
        }

        assertThatThrownBy(() -> deviceService.throwIfRoomNotOwned(2L, "user"))
                .isInstanceOf(NotFoundException.class);

        r.setId(2L);

        assertThatThrownBy(() -> deviceService.throwIfRoomNotOwned(1L, "user"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void triggerTriggers() {
        final RegularLight r = new RegularLight();
        r.setId(1L);
        r.setOn(true);

        final BooleanTrigger b = new BooleanTrigger();
        b.setId(2L);
        b.setDevice(r);
        b.setOn(true);
        b.setDeviceId(r.getId());

        final BooleanCondition c = new BooleanCondition();
        c.setId(3L);
        c.setDevice(r);
        c.setOn(true);
        c.setDeviceId(r.getId());

        final Scene s = new Scene();
        s.setId(4L);

        final Automation a = new Automation();
        a.setId(5L);

        final ScenePriority sp = new ScenePriority();
        sp.setSceneId(s.getId());
        sp.setScene(s);
        sp.setAutomationId(a.getId());
        sp.setAutomation(a);

        a.getTriggers().add(b);
        b.setAutomation(a);
        b.setAutomationId(a.getId());

        a.getConditions().add(c);
        c.setAutomation(a);
        c.setAutomationId(a.getId());

        a.getScenes().add(sp);

        when(automationService.findTriggersByDeviceId(1L)).thenReturn(List.of(b));
        when(conditionRepository.findAllByAutomationId(5L)).thenReturn(List.of(c));
        when(automationService.findByVerifiedId(5L)).thenReturn(a);
        when(sceneService.findByValidatedId(4L)).thenReturn(s);

        final boolean[] passed = new boolean[1];
        when(sceneService.apply(s, "user", true))
                .thenAnswer(
                        invocation -> {
                            passed[0] = true;
                            return null;
                        });

        deviceService.triggerTriggers(r, "user");

        assertThat(passed[0]).isTrue();
    }

    @Test
    public void testSaveAsGuest() throws NotFoundException {
        Thermostat t = new Thermostat();

        User u = new User();
        u.setUsername("user");

        User host = new User();
        host.setId(1L);
        host.setUsername("host");
        host.getGuests().add(u);
        u.getHosts().add(host);

        when(userRepository.findByUsername("user")).thenReturn(u);
        when(userRepository.findById(1L)).thenReturn(Optional.of(host));
        doNothing().when(devicePropagationService).renameIfDuplicate(t, "host");
        when(deviceRepository.save(t)).thenAnswer(ta -> ta.getArguments()[0]);

        assertThat(deviceService.saveAsGuest(t, "user", 1L)).isEqualTo(t);
    }

    @Test
    public void testDeleteIdAsOwner() {
        try {
            doNothing().when(devicePropagationService).deleteByIdAsOwner(1L, "user");
            deviceService.deleteByIdAsOwner(1L, "user");
        } catch (NotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void populateComputedFields() {
        doNothing().when(devicePopulationService).populateComputedFields(List.of());
        Assertions.assertDoesNotThrow(() -> deviceService.populateComputedFields(List.of()));
    }

    @Test
    public void testSaveAllAsOwner() {
        final DeviceService currentDeviceService = Mockito.spy(deviceService);
        List<Device> devices = List.of(new RegularLight(), new ButtonDimmer());
        when(devicePropagationService.saveAllAsOwner(
                        eq(devices), eq("user"), anyBoolean(), eq(false)))
                .thenReturn(devices);

        final int[] count = new int[1];

        doAnswer(i -> count[0]++)
                .when(currentDeviceService)
                .triggerTriggers(any(Device.class), eq("user"));

        currentDeviceService.saveAllAsOwner(devices, "user", true, false);
        assertThat(count[0]).isEqualTo(0);

        currentDeviceService.saveAllAsOwner(devices, "user", false, false);
        assertThat(count[0]).isEqualTo(2);
    }

    @Test
    public void testSaveAsOwner() {
        final DeviceService currentDeviceService = Mockito.spy(deviceService);
        Device device = new ButtonDimmer();

        final boolean[] count = new boolean[1];

        doAnswer(i -> count[0] = true).when(currentDeviceService).triggerTriggers(device, "user");
        when(devicePropagationService.saveAsOwner(device, "user")).thenReturn(device);

        assertThat(currentDeviceService.saveAsOwner(device, "user")).isEqualTo(device);
        assertThat(count[0]).isTrue();
    }

    @Test
    public void testFindAll() throws NotFoundException {
        final DeviceService currentDeviceService = Mockito.spy(deviceService);
        final SecurityCamera gerryScotti = new SecurityCamera();
        doNothing().when(currentDeviceService).throwIfRoomNotOwned(1L, "user");
        doNothing().when(devicePopulationService).populateComputedFields(any());

        when(deviceRepository.findByRoomId(1L)).thenReturn(List.of(gerryScotti));
        when(deviceRepository.findAllByUsername("user")).thenReturn(List.of(gerryScotti));

        final User user = new User();
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setName("User");
        user.setId(1L);
        user.setCameraEnabled(true);

        final User guest = new User();
        guest.setUsername("guest");
        guest.setEmail("guest@example.com");
        guest.setName("Guest");
        guest.setId(2L);
        guest.getHosts().add(user);
        user.getGuests().add(guest);

        when(userRepository.findByUsername("guest")).thenReturn(guest);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        final Room r = new Room();
        r.setUserId(1L);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(r));
        when(roomRepository.findById(3L)).thenReturn(Optional.empty());

        assertThat(currentDeviceService.findAll(1L, null, "user")).containsExactly(gerryScotti);
        assertThat(currentDeviceService.findAll(null, "user")).containsExactly(gerryScotti);

        assertThatThrownBy(() -> currentDeviceService.findAll(1L, 3L, "guest"))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> currentDeviceService.findAll(3L, 1L, "guest"))
                .isInstanceOf(NotFoundException.class);

        assertThat(currentDeviceService.findAll(1L, 1L, "guest")).containsExactly(gerryScotti);

        user.setCameraEnabled(false);

        assertThat(currentDeviceService.findAll(1L, 1L, "guest")).isEmpty();
    }
}
