package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SwitchableSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "user")
public class SmartPlugControllerTests {
    @InjectMocks private SmartPlugController smartPlugController;

    @Mock private UserRepository userRepository;

    @Mock private SmartPlugRepository smartPlugRepository;

    @Mock private Principal mockPrincipal;

    @Mock private DeviceService deviceService;

    private final User u;

    public SmartPlugControllerTests() {
        u = new User();
        u.setName("user");
        u.setId(1L);
    }

    private void equalToRequest(Switchable created, SwitchableSaveRequest a) {
        assertThat(created.getName()).isEqualTo(a.getName());
        assertThat(created.getRoomId()).isEqualTo(30L);
        assertThat(created.isOn()).isEqualTo(a.isOn());
    }

    @Test
    public void testCreate() {
        when(mockPrincipal.getName()).thenReturn("user");
        when(deviceService.saveAsOwner(any(), eq("user"))).thenAnswer(i -> i.getArguments()[0]);

        SwitchableSaveRequest a = new SwitchableSaveRequest(true, 1L, 30L, "New SmartPlug");
        try {
            Switchable created = smartPlugController.create(a, mockPrincipal);
            assertThat(created.getId()).isEqualTo(0L);
            equalToRequest(created, a);
        } catch (NotFoundException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testUpdate() throws NotFoundException {
        when(mockPrincipal.getName()).thenReturn("user");
        final SmartPlug old = new SmartPlug();
        old.setId(42L);

        when(deviceService.saveAsOwner(any(), eq("user"))).thenAnswer(i -> i.getArguments()[0]);
        when(smartPlugRepository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(old));
        when(smartPlugRepository.findByIdAndUsername(43L, "user")).thenReturn(Optional.empty());

        SwitchableSaveRequest a = new SwitchableSaveRequest(true, 42L, 30L, "New SmartPlug");

        SmartPlug created = smartPlugController.update(a, mockPrincipal);
        assertThat(created.getId()).isEqualTo(42L);
        equalToRequest(created, a);

        a.setId(43L);
        assertThatThrownBy(() -> smartPlugController.update(a, mockPrincipal))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testDelete() throws NotFoundException {
        when(mockPrincipal.getName()).thenReturn("user");
        doNothing().when(deviceService).deleteByIdAsOwner(42L, "user");
        Assertions.assertDoesNotThrow(() -> smartPlugController.deleteById(42L, mockPrincipal));
    }
}
