package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("automation tests")
public class AutomationTests {
    private Automation automation;

    @BeforeEach
    private void createAutomation() {
        this.automation = new Automation();
    }

    @Test
    @DisplayName("test setName")
    public void testSetName() {
        automation.setName("Automation1");
        assertEquals("Automation1", automation.getName());
    }

    @Test
    @DisplayName("test setId")
    public void testSetId() {
        automation.setId(34l);
        assertEquals(34l, automation.getId());
    }

    @Test
    @DisplayName("test setUserId")
    public void testSetUserId() {
        automation.setUserId(43l);
        assertEquals(43l, automation.getUserId());
    }

    @Test
    @DisplayName("test setUser")
    public void testSetUser() {
        User user = new User();
        automation.setUser(user);
        user.setUsername("xXcoolNameXx");
        user.setId(42l);
        user.setName("John RealName");
        assertTrue(user.equals(automation.getUser()));
    }

    @Test
    @DisplayName("test setTriggers")
    public void testSetTrigger() {
        BooleanTrigger trigger1 = new BooleanTrigger();
        BooleanTrigger trigger2 = new BooleanTrigger();
        automation.getTriggers().add(trigger1);
        automation.getTriggers().add(trigger2);
        assertEquals(2, automation.getTriggers().size());
    }

    @Test
    @DisplayName("test setScene")
    public void testSetScene() {
        ScenePriority priority1 = new ScenePriority();
        priority1.setPriority(1);
        ScenePriority priority2 = new ScenePriority();
        priority2.setPriority(2);
        ScenePriority priority3 = new ScenePriority();
        priority3.setPriority(3);
        automation.getScenes().add(priority1);
        automation.getScenes().add(priority2);
        automation.getScenes().add(priority3);
        assertEquals(3, automation.getScenes().size());
    }

    @Test
    @DisplayName("test setCondition")
    public void testSetCondition() {
        BooleanCondition bool = new BooleanCondition();
        RangeCondition range = new RangeCondition();
        bool.setOn(true);
        range.setRange(10l);
        automation.getConditions().add(bool);
        automation.getConditions().add(range);
        assertEquals(2, automation.getConditions().size());
    }
}
