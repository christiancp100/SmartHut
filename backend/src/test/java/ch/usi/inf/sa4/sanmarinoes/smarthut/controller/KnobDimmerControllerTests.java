package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.GenericDeviceSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.KnobDimmerDimRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "user")
@DisplayName("KnobDimmer controller test")
public class KnobDimmerControllerTests {
    @InjectMocks private KnobDimmerController controller;

    @Mock private Principal principal;

    @Mock private DeviceService deviceService;

    @Mock private KnobDimmerRepository knobDimmerRepository;

    @Test
    public void testGetId() throws NotFoundException {
        KnobDimmer dimmer = new KnobDimmer();
        dimmer.setId(42L);
        when(knobDimmerRepository.findById(42L)).thenReturn(Optional.of(dimmer));
        when(knobDimmerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThat(controller.findById(42L)).isSameAs(dimmer);
        assertThatThrownBy(() -> controller.findById(1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testCreate() {
        when(principal.getName()).thenReturn("user");
        doNothing().when(deviceService).throwIfRoomNotOwned(anyLong(), eq("user"));
        when(deviceService.saveAsOwner(any(KnobDimmer.class), eq("user")))
                .thenAnswer(i -> i.getArguments()[0]);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        GenericDeviceSaveRequest toSend = new GenericDeviceSaveRequest();
        toSend.setName("dimmer");
        toSend.setRoomId(42L);
        KnobDimmer dimmer = controller.create(toSend, principal);
        assertThat(dimmer.getName()).isEqualTo(toSend.getName());
        assertThat(dimmer.getRoomId()).isEqualTo(toSend.getRoomId());
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testDelete() {
        when(principal.getName()).thenReturn("user");
        doNothing().when(deviceService).deleteByIdAsOwner(eq(42L), eq("user"));
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        controller.delete(42L, principal);
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testDimTo() {
        when(principal.getName()).thenReturn("user");
        KnobDimmer dimmer = new KnobDimmer();
        DimmableLight light = new DimmableLight();
        Curtains curtains = new Curtains();
        dimmer.addDimmable(light);
        dimmer.addDimmable(curtains);
        when(knobDimmerRepository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(dimmer));
        KnobDimmerDimRequest toSend = new KnobDimmerDimRequest();
        toSend.setIntensity(12);
        toSend.setId(42L);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Set<Dimmable> set = controller.dimTo(toSend, principal);
        assertThat(set.size()).isEqualTo(dimmer.getOutputs().size());
    }
}
