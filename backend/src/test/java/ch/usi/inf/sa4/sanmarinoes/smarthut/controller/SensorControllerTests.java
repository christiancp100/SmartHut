package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SensorSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.Sensor;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
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

@DisplayName("The sensor controller")
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "user")
public class SensorControllerTests {
    @InjectMocks private SensorController sensorController;

    @Mock private DeviceService deviceService;

    @Mock private Principal mockPrincipal;

    @BeforeEach
    public void setup() {
        when(mockPrincipal.getName()).thenReturn("user");
    }

    private void checkSensorAgainstRequest(final Sensor toCheck, final SensorSaveRequest request) {
        assertThat(toCheck).isNotNull();
        assertThat(toCheck.getSensor()).isEqualTo(request.getSensor());
        assertThat(toCheck.getValue()).isEqualTo(request.getValue());
        assertThat(toCheck.getName()).isEqualTo(request.getName());
        assertThat(toCheck.getRoomId()).isEqualTo(request.getRoomId());
    }

    @DisplayName("when creating should return the same object")
    @Test
    @SneakyThrows(NotFoundException.class)
    public void testCreate() {
        doNothing().when(deviceService).throwIfRoomNotOwned(anyLong(), eq("user"));
        when(deviceService.saveAsOwner(any(Sensor.class), eq("user")))
                .thenAnswer(i -> i.getArguments()[0]);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        final SensorSaveRequest toSend =
                new SensorSaveRequest(
                        Sensor.SensorType.TEMPERATURE, BigDecimal.ZERO, 42L, "Test sensor");
        final Sensor created = sensorController.create(toSend, mockPrincipal);

        checkSensorAgainstRequest(created, toSend);
    }

    @DisplayName("when deleting an existing id should succeed")
    @Test
    @SneakyThrows(NotFoundException.class)
    public void testDelete() {
        doNothing().when(deviceService).deleteByIdAsOwner(eq(42L), eq("user"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Assertions.assertDoesNotThrow(() -> sensorController.deleteById(42L, mockPrincipal));
    }
}
