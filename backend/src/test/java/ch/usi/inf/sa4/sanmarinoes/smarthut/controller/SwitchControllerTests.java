package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.GenericDeviceSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SwitchOperationRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.RegularLight;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Switch;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SwitchRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Switchable;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class SwitchControllerTests {
    @InjectMocks private SwitchController controller;

    @Mock private DeviceService deviceService;

    @Mock private SwitchRepository repository;

    @Mock private Principal principal;

    @Test
    public void testGetId() throws NotFoundException {
        Switch aSwitch = new Switch();
        aSwitch.setId(42L);
        when(repository.findById(42L)).thenReturn(Optional.of(aSwitch));
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThat(controller.findById(42L)).isSameAs(aSwitch);
        assertThatThrownBy(() -> controller.findById(1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testCreate() {
        when(principal.getName()).thenReturn("user");
        doNothing().when(deviceService).throwIfRoomNotOwned(anyLong(), eq("user"));
        when(deviceService.saveAsOwner(any(Switch.class), eq("user")))
                .thenAnswer(i -> i.getArguments()[0]);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        GenericDeviceSaveRequest toSend = new GenericDeviceSaveRequest();
        toSend.setName("dimmer");
        toSend.setRoomId(42L);
        Switch aSwitch = controller.create(toSend, principal);
        assertThat(aSwitch.getName()).isEqualTo(toSend.getName());
        assertThat(aSwitch.getRoomId()).isEqualTo(toSend.getRoomId());
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testDelete() {
        when(principal.getName()).thenReturn("user");
        doNothing().when(deviceService).deleteByIdAsOwner(eq(42L), eq("user"));
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        controller.deleteById(42L, principal);
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testOperate() {
        when(principal.getName()).thenReturn("user");
        Switch aSwitch = new Switch();
        aSwitch.setOn(true);
        RegularLight light = new RegularLight();
        RegularLight light2 = new RegularLight();
        aSwitch.connect(light, true);
        aSwitch.connect(light2, true);
        SwitchOperationRequest toSend = new SwitchOperationRequest();
        toSend.setType(SwitchOperationRequest.OperationType.ON);
        toSend.setId(42L);
        when(repository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(aSwitch));
        ArrayList<Switchable> helper = new ArrayList<Switchable>();
        helper.add(light);
        helper.add(light2);
        when(deviceService.saveAllAsOwner(aSwitch.getOutputs(), principal.getName()))
                .thenReturn(helper);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        List<Switchable> list = controller.operate(toSend, principal);
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testOperateOFF() {
        when(principal.getName()).thenReturn("user");
        Switch aSwitch = new Switch();
        aSwitch.setOn(true);
        RegularLight light = new RegularLight();
        RegularLight light2 = new RegularLight();
        aSwitch.connect(light, true);
        aSwitch.connect(light2, true);
        SwitchOperationRequest toSend = new SwitchOperationRequest();
        toSend.setType(SwitchOperationRequest.OperationType.OFF);
        toSend.setId(42L);
        when(repository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(aSwitch));
        ArrayList<Switchable> helper = new ArrayList<Switchable>();
        helper.add(light);
        helper.add(light2);
        when(deviceService.saveAllAsOwner(aSwitch.getOutputs(), principal.getName()))
                .thenReturn(helper);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        List<Switchable> list = controller.operate(toSend, principal);
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    @SneakyThrows(NotFoundException.class)
    public void testOperateToggle() {
        when(principal.getName()).thenReturn("user");
        Switch aSwitch = new Switch();
        aSwitch.setOn(true);
        RegularLight light = new RegularLight();
        RegularLight light2 = new RegularLight();
        aSwitch.connect(light, true);
        aSwitch.connect(light2, true);
        SwitchOperationRequest toSend = new SwitchOperationRequest();
        toSend.setType(SwitchOperationRequest.OperationType.TOGGLE);
        toSend.setId(42L);
        when(repository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(aSwitch));
        ArrayList<Switchable> helper = new ArrayList<Switchable>();
        helper.add(light);
        helper.add(light2);
        when(deviceService.saveAllAsOwner(aSwitch.getOutputs(), principal.getName()))
                .thenReturn(helper);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        List<Switchable> list = controller.operate(toSend, principal);
        assertThat(list.size()).isEqualTo(2);
    }
}
