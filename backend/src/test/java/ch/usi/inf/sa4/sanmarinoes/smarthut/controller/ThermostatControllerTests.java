package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.ThermostatSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.SceneRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.State;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.StateRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Thermostat;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.ThermostatRepository;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.ThermostatPopulationService;
import java.math.BigDecimal;
import java.security.Principal;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class ThermostatControllerTests {

    @InjectMocks private ThermostatController thermostatController;

    @Mock private StateRepository<State> stateRepository;
    @Mock private SceneRepository sceneRepository;
    @Mock private ThermostatPopulationService thermostatService;
    @Mock private ThermostatRepository thermostatRepository;
    @Mock private DeviceService deviceService;
    @Mock private Principal mockPrincipal;

    @BeforeEach
    public void setup() {
        when(mockPrincipal.getName()).thenReturn("user");
    }

    private void checkThermostatAgainstRequest(
            final Thermostat toCheck, final ThermostatSaveRequest request) {
        assertThat(toCheck).isNotNull();
        assertThat(toCheck.isOn()).isEqualTo(request.isTurnOn());
        assertThat(toCheck.getTargetTemperature()).isEqualTo(request.getTargetTemperature());
        assertThat(toCheck.isUseExternalSensors()).isEqualTo(request.isUseExternalSensors());
        assertThat(toCheck.getName()).isEqualTo(request.getName());
        assertThat(toCheck.getRoomId()).isEqualTo(request.getRoomId());
    }

    @Test
    @DisplayName("when creating should return the same object")
    @SneakyThrows(NotFoundException.class)
    public void testCreate() {
        doNothing().when(deviceService).throwIfRoomNotOwned(anyLong(), eq("user"));
        doNothing().when(thermostatService).populateMeasuredTemperature(any(Thermostat.class));
        when(deviceService.saveAsOwner(any(Thermostat.class), eq("user")))
                .thenAnswer(i -> i.getArguments()[0]);
        when(thermostatRepository.save(any(Thermostat.class))).thenAnswer(i -> i.getArguments()[0]);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        final ThermostatSaveRequest toSend = new ThermostatSaveRequest();
        toSend.setRoomId(42L);
        toSend.setTargetTemperature(new BigDecimal(40));
        toSend.setName("Thermostat Test");
        toSend.setUseExternalSensors(true);

        final Thermostat thermostat = thermostatController.create(toSend, mockPrincipal);

        checkThermostatAgainstRequest(thermostat, toSend);
    }

    @Test
    @DisplayName("when updating should return the same object")
    @SneakyThrows(NotFoundException.class)
    public void testUpdate() {

        final ThermostatSaveRequest toSend = new ThermostatSaveRequest();
        toSend.setRoomId(42L);
        toSend.setTargetTemperature(new BigDecimal(40));
        toSend.setName("Thermostat Test");
        toSend.setUseExternalSensors(true);

        final Thermostat toUpdate = new Thermostat();
        toSend.setRoomId(20L);
        toSend.setTargetTemperature(new BigDecimal(50));
        toSend.setName("Thermostat");
        toSend.setUseExternalSensors(false);

        when(thermostatRepository.findByIdAndUsername(anyLong(), any(String.class)))
                .thenReturn(java.util.Optional.of(toUpdate));
        when(thermostatRepository.save(any(Thermostat.class))).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(thermostatService).populateMeasuredTemperature(any(Thermostat.class));
        when(deviceService.saveAsOwner(any(Thermostat.class), eq("user")))
                .thenAnswer(i -> i.getArguments()[0]);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        final Thermostat thermostat = thermostatController.update(toSend, mockPrincipal);

        checkThermostatAgainstRequest(thermostat, toSend);
    }

    @Test
    @DisplayName("an existing id should succeed")
    @SneakyThrows(NotFoundException.class)
    public void testDelete() {

        doNothing().when(deviceService).deleteByIdAsOwner(eq(42L), eq("user"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Assertions.assertDoesNotThrow(() -> thermostatController.deleteById(42L, mockPrincipal));
    }
}
