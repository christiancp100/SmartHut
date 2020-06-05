package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.ButtonDimmerDimRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.GenericDeviceSaveRequest;
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
public class ButtonDimmerControllerTests {
    @InjectMocks private ButtonDimmerController controller;

    @Mock private DeviceService service;

    @Mock private ButtonDimmerRepository repository;

    @Mock Principal principal;

    @Test
    public void testGetId() throws NotFoundException {
        ButtonDimmer dimmer = new ButtonDimmer();
        dimmer.setId(42L);
        when(repository.findById(42L)).thenReturn(Optional.of(dimmer));
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThat(controller.findById(42L)).isSameAs(dimmer);
        assertThatThrownBy(() -> controller.findById(1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testCreate() {
        when(principal.getName()).thenReturn("user");
        doNothing().when(service).throwIfRoomNotOwned(anyLong(), eq("user"));
        when(service.saveAsOwner(any(ButtonDimmer.class), eq("user")))
                .thenAnswer(i -> i.getArguments()[0]);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        GenericDeviceSaveRequest toSend = new GenericDeviceSaveRequest();
        toSend.setName("dimmer");
        toSend.setRoomId(42L);
        ButtonDimmer dimmer = controller.create(toSend, principal);
        assertThat(dimmer.getName()).isEqualTo(toSend.getName());
        assertThat(dimmer.getRoomId()).isEqualTo(toSend.getRoomId());
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testDelete() {
        when(principal.getName()).thenReturn("user");
        doNothing().when(service).deleteByIdAsOwner(eq(42L), eq("user"));
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        controller.delete(42L, principal);
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testDimUp() {
        when(principal.getName()).thenReturn("user");
        ButtonDimmer button = new ButtonDimmer();
        Curtains curtains = new Curtains();
        DimmableLight light = new DimmableLight();
        button.addDimmable(curtains);
        button.addDimmable(light);
        ButtonDimmerDimRequest toSend = new ButtonDimmerDimRequest();
        toSend.setId(42L);
        toSend.setDimType(ButtonDimmerDimRequest.DimType.UP);
        when(repository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(button));
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Set<Dimmable> set = controller.dim(toSend, principal);
        assertThat(set.size()).isEqualTo(button.getOutputs().size());
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testDimDown() {
        when(principal.getName()).thenReturn("user");
        ButtonDimmer button = new ButtonDimmer();
        Curtains curtains = new Curtains();
        DimmableLight light = new DimmableLight();
        button.addDimmable(curtains);
        button.addDimmable(light);
        ButtonDimmerDimRequest toSend = new ButtonDimmerDimRequest();
        toSend.setId(42L);
        toSend.setDimType(ButtonDimmerDimRequest.DimType.DOWN);
        when(repository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(button));
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Set<Dimmable> set = controller.dim(toSend, principal);
        assertThat(set.size()).isEqualTo(button.getOutputs().size());
    }
}
