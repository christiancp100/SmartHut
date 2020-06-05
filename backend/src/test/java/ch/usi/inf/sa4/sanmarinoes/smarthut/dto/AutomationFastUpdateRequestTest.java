package ch.usi.inf.sa4.sanmarinoes.smarthut.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.usi.inf.sa4.sanmarinoes.smarthut.dto.automation.*;
import ch.usi.inf.sa4.sanmarinoes.smarthut.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Automation Update DTO")
public class AutomationFastUpdateRequestTest {

    @Test
    @DisplayName(" checking boolean trigger ")
    public void booleanTriggerDTOTest() {
        BooleanTriggerDTO booleanTriggerDTO = new BooleanTriggerDTO();
        booleanTriggerDTO.setOn(true);
        booleanTriggerDTO.setDeviceId(42);
        BooleanTrigger booleanTrigger = (BooleanTrigger) booleanTriggerDTO.toModel();
        assertEquals(booleanTrigger.isOn(), booleanTriggerDTO.isOn());
        assertEquals(booleanTrigger.getDeviceId(), booleanTriggerDTO.getDeviceId());
    }

    @Test
    @DisplayName(" checking range trigger ")
    public void rangeTriggerDTOTest() {
        RangeTriggerDTO rangeTriggerDTO = new RangeTriggerDTO();
        rangeTriggerDTO.setOperator(Operator.EQUAL);
        rangeTriggerDTO.setDeviceId(420);
        rangeTriggerDTO.setRange(12);

        RangeTrigger rangeTrigger = (RangeTrigger) rangeTriggerDTO.toModel();
        assertEquals(rangeTrigger.getOperator(), rangeTriggerDTO.getOperator());
        assertEquals(rangeTrigger.getRange(), rangeTriggerDTO.getRange());
        assertEquals(rangeTrigger.getDeviceId(), rangeTriggerDTO.getDeviceId());
    }

    @Test
    @DisplayName(" checking scene priority ")
    public void scenePriorityDTOTest() {
        ScenePriorityDTO scenePriorityDTO = new ScenePriorityDTO();
        scenePriorityDTO.setPriority(67);
        scenePriorityDTO.setSceneId(21);

        ScenePriority scenePriority = scenePriorityDTO.toModel();
        assertEquals(scenePriority.getPriority(), scenePriorityDTO.getPriority());
        assertEquals(scenePriority.getSceneId(), scenePriorityDTO.getSceneId());
    }

    @Test
    @DisplayName(" checking boolean condition ")
    public void booleanConditionDTOTest() {
        BooleanConditionDTO booleanConditionDTO = new BooleanConditionDTO();
        booleanConditionDTO.setOn(true);
        booleanConditionDTO.setDeviceId(17);

        BooleanCondition booleanCondition = (BooleanCondition) booleanConditionDTO.toModel();
        assertEquals(booleanCondition.isOn(), booleanConditionDTO.isOn());
        assertEquals(booleanCondition.getDeviceId(), booleanConditionDTO.getDeviceId());
    }

    @Test
    @DisplayName(" checking range condition ")
    public void rangeConditionDTOTest() {
        RangeConditionDTO rangeConditionDTO = new RangeConditionDTO();
        rangeConditionDTO.setOperator(Operator.LESS);
        rangeConditionDTO.setRange(82.01);
        rangeConditionDTO.setDeviceId(13);

        RangeCondition rangeCondition = (RangeCondition) rangeConditionDTO.toModel();
        assertEquals(rangeCondition.getOperator(), rangeConditionDTO.getOperator());
        assertEquals(rangeCondition.getRange(), rangeConditionDTO.getRange());
        assertEquals(rangeCondition.getDeviceId(), rangeConditionDTO.getDeviceId());
    }

    @Test
    @DisplayName(" checking thermostat condition ")
    public void thermostatConditionDTOTest() {
        ThermostatConditionDTO thermostatConditionDTO = new ThermostatConditionDTO();
        thermostatConditionDTO.setDeviceId(25);
        thermostatConditionDTO.setOperator(ThermostatCondition.Operator.EQUAL);
        thermostatConditionDTO.setMode(Thermostat.Mode.HEATING);

        ThermostatCondition thermostatCondition =
                (ThermostatCondition) thermostatConditionDTO.toModel();
        assertEquals(thermostatCondition.getMode(), thermostatConditionDTO.getMode());
        assertEquals(thermostatCondition.getOperator(), thermostatConditionDTO.getOperator());
        assertEquals(thermostatCondition.getDeviceId(), thermostatConditionDTO.getDeviceId());
    }
}
