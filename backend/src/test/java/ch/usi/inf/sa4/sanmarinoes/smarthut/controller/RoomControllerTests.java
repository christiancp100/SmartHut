package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.RoomSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.service.DeviceService;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "user")
public class RoomControllerTests {
    @InjectMocks private RoomController roomController;

    @Mock private UserRepository userRepository;

    @Mock private RoomRepository roomRepository;

    @Mock private Principal mockPrincipal;

    @Mock private SwitchRepository switchRepository;

    @Mock private KnobDimmerRepository knobDimmerRepository;

    @Mock private ButtonDimmerRepository buttonDimmerRepository;

    @Mock private DeviceService deviceService;

    private final User u;

    public RoomControllerTests() {
        u = new User();
        u.setName("user");
        u.setId(1L);
    }

    @Test
    public void testGetAll() throws NotFoundException {
        when(mockPrincipal.getName()).thenReturn("user");
        when(roomRepository.findByUsername("user")).thenReturn(List.of());
        assertThat(roomController.findAll(null, mockPrincipal)).isEmpty();
    }

    @Test
    public void testGet() throws NotFoundException {
        Room r = new Room();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(r));
        when(roomRepository.findById(2L)).thenReturn(Optional.empty());

        assertThat(roomController.findById(1L, mockPrincipal, null)).isSameAs(r);
        assertThatThrownBy(() -> roomController.findById(2L, mockPrincipal, null))
                .isInstanceOf(NotFoundException.class);
    }

    private void equalToRequest(Room created, RoomSaveRequest a) {
        assertThat(created.getName()).isEqualTo(a.getName());
        assertThat(created.getUserId()).isEqualTo(1L);
        assertThat(created.getIcon()).isEqualTo(a.getIcon());
        assertThat(created.getImage()).isEqualTo(a.getImage());
    }

    @Test
    public void testCreate() {
        when(mockPrincipal.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(u);
        when(roomRepository.save(any(Room.class))).thenAnswer(i -> i.getArguments()[0]);

        RoomSaveRequest roomSaveRequest = new RoomSaveRequest(0, Icon.BEER, null, "New Room");
        Room created = roomController.create(roomSaveRequest, mockPrincipal);
        assertThat(created.getId()).isNull();
        equalToRequest(created, roomSaveRequest);
    }

    @Test
    public void testUpdate() throws NotFoundException {
        when(mockPrincipal.getName()).thenReturn("user");
        final Room old = new Room();
        old.setId(42L);
        old.setUserId(1L);
        old.setName("Old Name");

        when(roomRepository.save(any(Room.class))).thenAnswer(i -> i.getArguments()[0]);
        when(roomRepository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(old));
        when(roomRepository.findByIdAndUsername(43L, "user")).thenReturn(Optional.empty());

        RoomSaveRequest roomSaveRequest = new RoomSaveRequest(42L, Icon.BEER, null, "New Room");

        Room created =
                roomController.update(roomSaveRequest.getId(), roomSaveRequest, mockPrincipal);
        assertThat(created.getId()).isEqualTo(42L);
        equalToRequest(created, roomSaveRequest);

        roomSaveRequest.setId(43L);
        assertThatThrownBy(
                        () ->
                                roomController.update(
                                        roomSaveRequest.getId(), roomSaveRequest, mockPrincipal))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testDelete() throws NotFoundException {
        when(mockPrincipal.getName()).thenReturn("user");

        final Room toDelete = new Room();
        toDelete.setId(42L);

        doNothing().when(switchRepository).deleteAllByRoomId(42L);
        doNothing().when(knobDimmerRepository).deleteAllByRoomId(42L);
        doNothing().when(buttonDimmerRepository).deleteAllByRoomId(42L);
        when(roomRepository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(toDelete));
        when(deviceService.findAll(42L, null, "user")).thenReturn(List.of());
        doNothing().when(roomRepository).delete(toDelete);

        try {
            roomController.deleteById(42L, mockPrincipal);
        } catch (NotFoundException e) {
            fail(e.getMessage());
        }
    }
}
