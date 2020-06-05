package ch.usi.inf.sa4.sanmarinoes.smarthut.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.socket.SensorSocketEndpoint;
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
public class DevicePropagationServiceTests {
    @InjectMocks private DevicePropagationService devicePropagationService;

    @Mock private SensorSocketEndpoint endpoint;

    @Mock private DeviceRepository<Device> deviceRepository;

    @Mock private EagerUserRepository userRepository;

    private User host;
    private User guest;

    private void initHostGuest() {
        host = new User();
        host.setName("host");
        host.setUsername("host");
        host.setId(1L);

        guest = new User();
        guest.setName("guest");
        guest.setUsername("guest");
        guest.setId(2L);

        guest.getHosts().add(host);
        host.getGuests().add(guest);
    }

    @Test
    public void testPropagateUpdateAsGuest() {
        Device toPropagate = new SecurityCamera();

        User host = new User();
        host.setName("host");
        host.setId(1L);

        User guest = new User();
        guest.setName("guest");
        guest.setId(2L);

        User guest2 = new User();
        guest.setName("guest2");
        guest.setId(3L);

        guest.getHosts().add(host);
        host.getGuests().add(guest);
        guest2.getHosts().add(host);
        host.getGuests().add(guest2);

        doNothing().when(endpoint).queueDeviceUpdate(toPropagate, host, true, null, false);

        boolean[] done = new boolean[1];

        doAnswer(i -> done[0] = true)
                .when(endpoint)
                .queueDeviceUpdate(toPropagate, guest2, false, host.getId(), false);

        devicePropagationService.propagateUpdateAsGuest(toPropagate, host, guest);
        assertThat(done[0]).isTrue();
    }

    @Test
    public void saveAllAsGuestSceneApplication() {
        initHostGuest();
        when(userRepository.findById(1L)).thenReturn(Optional.of(host));
        when(userRepository.findByUsername("guest")).thenReturn(guest);

        int[] done = new int[1];

        final DevicePropagationService devicePropagationService1 =
                Mockito.spy(devicePropagationService);
        doAnswer(i -> done[0]++)
                .when(devicePropagationService1)
                .propagateUpdateAsGuest(any(), eq(host), eq(guest));
        when(deviceRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);

        devicePropagationService1.saveAllAsGuestSceneApplication(
                List.of(new SecurityCamera(), new ButtonDimmer()), "guest", 1L);

        assertThat(done[0]).isEqualTo(2);
    }

    @Test
    public void testRenameIfDuplicate() {
        when(deviceRepository.findDuplicates("Device", "user")).thenReturn(2);
        when(deviceRepository.findDuplicates("Device (new)", "user")).thenReturn(1);
        when(deviceRepository.findDuplicates("New Device", "user")).thenReturn(1);
        when(deviceRepository.findDuplicates("New Device (new)", "user")).thenReturn(0);

        Device d = new RegularLight();
        d.setName("Device");
        d.setId(42L);

        devicePropagationService.renameIfDuplicate(d, "user");

        assertThat(d.getName()).isEqualTo("Device (new)");

        d.setName("New Device");
        d.setId(0L);

        devicePropagationService.renameIfDuplicate(d, "user");

        assertThat(d.getName()).isEqualTo("New Device (new)");
    }

    @Test
    public void testSaveAllAsOwner() {
        final DevicePropagationService dps = Mockito.spy(devicePropagationService);
        final Device d = new RegularLight();
        final List<Device> dl = List.of(d);
        doNothing().when(dps).renameIfDuplicate(d, "user");
        when(deviceRepository.saveAll(dl)).thenReturn(dl);
        doNothing().when(dps).propagateUpdateAsOwner(d, "user", false);
        doNothing().when(dps).propagateUpdateAsOwner(d, "user", true);

        assertThat(dps.saveAllAsOwner(dl, "user")).containsExactly(d);
        assertThat(dps.saveAllAsOwner(dl, "user", true, true)).containsExactly(d);
        assertThat(dps.saveAllAsOwner(dl, "user", true, false)).containsExactly(d);
        assertThat(dps.saveAllAsOwner(dl, "user", false, true)).containsExactly(d);
    }

    @Test
    public void testSaveAllAsGuest() {
        final DevicePropagationService dps = Mockito.spy(devicePropagationService);

        initHostGuest();
        when(userRepository.findById(1L)).thenReturn(Optional.of(host));
        when(userRepository.findByUsername("guest")).thenReturn(guest);
        when(userRepository.findById(42L)).thenReturn(Optional.empty());

        final User phonyGuest = new User();
        phonyGuest.setName("phonyguest");

        when(userRepository.findByUsername("phonyguest")).thenReturn(phonyGuest);

        final Device d = new ButtonDimmer();

        doNothing().when(dps).renameIfDuplicate(d, "host");

        assertThatThrownBy(() -> dps.saveAsGuest(d, "phonyguest", 1L))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> dps.saveAsGuest(d, "guest", 42L))
                .isInstanceOf(NotFoundException.class);

        when(deviceRepository.save(d)).thenReturn(d);
        doNothing().when(dps).propagateUpdateAsGuest(d, host, guest);

        Assertions.assertDoesNotThrow(() -> dps.saveAsGuest(d, "guest", 1L));
    }

    @Test
    public void testSaveAsOwner() {
        final DevicePropagationService dps = Mockito.spy(devicePropagationService);
        final Device d = new ButtonDimmer();
        doNothing().when(dps).renameIfDuplicate(d, "user");
        doNothing().when(dps).propagateUpdateAsOwner(d, "user", false);
        when(deviceRepository.save(d)).thenReturn(d);
        assertThat(dps.saveAsOwner(d, "user")).isSameAs(d);
    }

    @Test
    public void testDeleteByIdAsOwner() {
        initHostGuest();

        final Device d = new ButtonDimmer();

        boolean[] done = new boolean[1];
        when(userRepository.findByUsername("host")).thenReturn(host);
        when(deviceRepository.findByIdAndUsername(42L, "host")).thenReturn(Optional.of(d));
        when(deviceRepository.findByIdAndUsername(43L, "host")).thenReturn(Optional.empty());
        doAnswer(i -> done[0] = true).when(deviceRepository).delete(d);
        doNothing().when(endpoint).queueDeviceUpdate(d, guest, false, host.getId(), true);

        assertThatThrownBy(() -> devicePropagationService.deleteByIdAsOwner(43L, "host"))
                .isInstanceOf(NotFoundException.class);
        Assertions.assertDoesNotThrow(
                () -> devicePropagationService.deleteByIdAsOwner(42L, "host"));

        assertThat(done[0]).isTrue();
    }

    @Test
    public void testPropagateUpdateAsOwner() {
        initHostGuest();
        when(userRepository.findByUsername("host")).thenReturn(host);

        final Device d = new ButtonDimmer();

        int[] counter = new int[1];

        doAnswer(i -> counter[0]++)
                .when(endpoint)
                .queueDeviceUpdate(d, guest, false, host.getId(), false);
        doAnswer(i -> counter[0]++)
                .when(endpoint)
                .queueDeviceUpdate(d, host, false, host.getId(), false);

        devicePropagationService.propagateUpdateAsOwner(d, "host", false);
        assertThat(counter[0]).isEqualTo(1);
        counter[0] = 0;
        devicePropagationService.propagateUpdateAsOwner(d, "host", true);
        assertThat(counter[0]).isEqualTo(2);
    }
}
