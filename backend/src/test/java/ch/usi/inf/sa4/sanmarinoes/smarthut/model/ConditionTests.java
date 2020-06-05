package ch.usi.inf.sa4.sanmarinoes.smarthut.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.models.BooleanCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Condition tests")
public class ConditionTests {
    private BooleanCondition condition;

    @BeforeEach
    private void createCondition() {
        this.condition = new BooleanCondition();
    }

    @Test
    @DisplayName("test automationId")
    public void testSetAutomationId() {
        condition.setAutomationId(32l);
        assertEquals(32l, condition.getAutomationId());
    }

    @Test
    @DisplayName("test getKind")
    public void testGetKind() {
        assertEquals("booleanCondition", condition.getKind());
    }

    @Test
    @DisplayName("test setId")
    public void testSetId() {
        condition.setId(11);
        assertEquals(11, condition.getId());
    }
}
