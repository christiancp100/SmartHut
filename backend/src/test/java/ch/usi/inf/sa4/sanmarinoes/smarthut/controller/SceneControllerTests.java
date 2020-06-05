package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.SceneSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.error.NotFoundException;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import java.security.Principal;
import java.util.List;
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
public class SceneControllerTests {
    @InjectMocks private SceneController sceneController;

    @Mock private UserRepository userRepository;

    @Mock private SceneRepository sceneRepository;

    @Mock private Principal mockPrincipal;

    @Mock private StateRepository<State> stateStateRepository;

    private final User u;

    public SceneControllerTests() {
        u = new User();
        u.setName("user");
        u.setId(1L);
    }

    @Test
    public void testGetAll() throws NotFoundException {
        when(mockPrincipal.getName()).thenReturn("user");
        when(sceneRepository.findByUsername("user")).thenReturn(List.of());
        assertThat(sceneController.findAll(mockPrincipal, null)).isEmpty();
    }

    private void equalToRequest(Scene created, SceneSaveRequest a) {
        assertThat(created.getName()).isEqualTo(a.getName());
        assertThat(created.getUserId()).isEqualTo(1L);
        assertThat(created.getIcon()).isEqualTo(a.getIcon());
        assertThat(created.isGuestAccessEnabled()).isEqualTo(a.isGuestAccessEnabled());
    }

    @Test
    public void testCreate() {
        when(mockPrincipal.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(u);
        when(sceneRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        SceneSaveRequest s = new SceneSaveRequest(0, "New Scene", Icon.BATH, true);
        Scene created = sceneController.create(s, mockPrincipal);
        assertThat(created.getId()).isEqualTo(0L);
        equalToRequest(created, s);
    }

    @Test
    public void testUpdate() throws NotFoundException {
        when(mockPrincipal.getName()).thenReturn("user");
        final Scene old = new Scene();
        old.setId(42L);
        old.setUserId(1L);
        old.setName("Old Name");

        when(sceneRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(sceneRepository.findByIdAndUsername(42L, "user")).thenReturn(Optional.of(old));
        when(sceneRepository.findByIdAndUsername(43L, "user")).thenReturn(Optional.empty());

        SceneSaveRequest a = new SceneSaveRequest(42L, "New Scene", Icon.BATH, true);

        Scene created = sceneController.update(a.getId(), a, mockPrincipal);
        assertThat(created.getId()).isEqualTo(42L);
        equalToRequest(created, a);

        a.setId(43L);
        assertThatThrownBy(() -> sceneController.update(a.getId(), a, mockPrincipal))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testDelete() {
        doNothing().when(stateStateRepository).deleteAllBySceneId(42L);
        doNothing().when(sceneRepository).deleteById(42L);
        Assertions.assertDoesNotThrow(() -> sceneController.deleteById(42L));
    }
}
