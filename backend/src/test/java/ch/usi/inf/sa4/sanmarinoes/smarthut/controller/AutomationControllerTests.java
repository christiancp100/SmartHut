package ch.usi.inf.sa4.sanmarinoes.smarthut.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.AutomationFastUpdateRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.AutomationSaveRequest;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.BooleanConditionDTO;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.BooleanTriggerDTO;
import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.ScenePriorityDTO;
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
public class AutomationControllerTests {
    @InjectMocks private AutomationController automationController;

    @Mock private UserRepository userRepository;

    @Mock private AutomationRepository automationRepository;

    @Mock private Principal mockPrincipal;

    @Mock private TriggerRepository<Trigger<?>> triggerRepository;

    @Mock private ConditionRepository<Condition<?>> conditionRepository;

    @Mock private ScenePriorityRepository scenePriorityRepository;

    private final User u;

    public AutomationControllerTests() {
        u = new User();
        u.setName("user");
        u.setId(1L);
    }

    @Test
    public void testGetAll() {
        when(mockPrincipal.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(u);
        when(automationRepository.findAllByUserId(1L)).thenReturn(List.of());
        assertThat(automationController.getAll(null, mockPrincipal)).isEmpty();
    }

    @Test
    public void testGet() throws NotFoundException {
        Automation a = new Automation();
        when(automationRepository.findById(1L)).thenReturn(Optional.of(a));
        when(automationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThat(automationController.get(1L)).isSameAs(a);
        assertThatThrownBy(() -> automationController.get(2L))
                .isInstanceOf(NotFoundException.class);
    }

    private void equalToRequest(Automation created, AutomationSaveRequest a) {
        assertThat(created.getName()).isEqualTo(a.getName());
        assertThat(created.getUserId()).isEqualTo(1L);
    }

    @Test
    public void testCreate() {
        when(mockPrincipal.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(u);
        when(automationRepository.save(any(Automation.class))).thenAnswer(i -> i.getArguments()[0]);

        AutomationSaveRequest a = new AutomationSaveRequest(26, "Automation");
        Automation created = automationController.create(a, mockPrincipal);
        assertThat(created.getId()).isEqualTo(0L);
        equalToRequest(created, a);
    }

    @Test
    public void testUpdate() throws NotFoundException {
        when(mockPrincipal.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(u);
        final Automation old = new Automation();
        old.setId(42L);
        old.setName("Old Name");

        when(automationRepository.save(any(Automation.class))).thenAnswer(i -> i.getArguments()[0]);
        when(automationRepository.findById(42L)).thenReturn(Optional.of(old));
        when(automationRepository.findById(43L)).thenReturn(Optional.empty());

        AutomationSaveRequest a = new AutomationSaveRequest(42L, "New Name");

        Automation created = automationController.update(a, mockPrincipal);
        assertThat(created.getId()).isEqualTo(42L);
        equalToRequest(created, a);

        a.setId(43L);
        assertThatThrownBy(() -> automationController.update(a, mockPrincipal))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testFastUpdate() throws NotFoundException {
        when(mockPrincipal.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(u);

        final Automation a = new Automation();
        a.setId(42L);
        a.setName("Old Name");

        final RangeTrigger rt = new RangeTrigger();
        final RangeCondition co = new RangeCondition();

        a.getTriggers().add(rt);
        a.getConditions().add(co);

        final AutomationFastUpdateRequest f = new AutomationFastUpdateRequest();
        f.setName("New name");
        f.setId(43L);
        f.setScenes(List.of(new ScenePriorityDTO(30L, 1)));

        final BooleanConditionDTO b = new BooleanConditionDTO(true);
        b.setDeviceId(1L);
        f.setConditions(List.of(b));

        final BooleanTriggerDTO c = new BooleanTriggerDTO(true);
        c.setDeviceId(1L);
        f.setTriggers(List.of(c));

        doAnswer(
                        i -> {
                            a.getTriggers().clear();
                            return null;
                        })
                .when(triggerRepository)
                .deleteAllByAutomationId(42L);
        doAnswer(
                        i -> {
                            a.getConditions().clear();
                            return null;
                        })
                .when(conditionRepository)
                .deleteAllByAutomationId(42L);
        doAnswer(
                        i -> {
                            a.getScenes().clear();
                            return null;
                        })
                .when(scenePriorityRepository)
                .deleteAllByAutomationId(42L);

        when(automationRepository.findByIdAndUserId(42L, 1L)).thenReturn(Optional.of(a));
        when(automationRepository.findByIdAndUserId(43L, 1L)).thenReturn(Optional.empty());

        when(conditionRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);
        when(triggerRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);
        when(scenePriorityRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);
        when(automationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        assertThatThrownBy(() -> automationController.fastUpdate(f, mockPrincipal))
                .isInstanceOf(NotFoundException.class);

        f.setId(42L);

        Automation saved = automationController.fastUpdate(f, mockPrincipal);

        assertThat(saved.getId()).isEqualTo(42L);
        assertThat(saved.getName()).isEqualTo("New name");
        assertThat(saved.getTriggers()).doesNotContain(rt);
        assertThat(saved.getConditions()).doesNotContain(co);
        assertThat(saved.getTriggers().size()).isEqualTo(1);
        assertThat(saved.getConditions().size()).isEqualTo(1);
        assertThat(saved.getScenes().size()).isEqualTo(1);
    }

    @Test
    public void testDelete() {
        final Automation old = new Automation();
        old.setId(42L);
        old.setName("Old Name");
        doNothing().when(automationRepository).deleteById(42L);
        Assertions.assertDoesNotThrow(() -> automationController.delete(42L));
    }
}
