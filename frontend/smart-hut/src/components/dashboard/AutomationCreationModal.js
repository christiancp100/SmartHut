import React, { Component, useState, useRef } from 'react';
import { connect } from 'react-redux';
import { RemoteService } from '../../remote';
import update from 'immutability-helper';
import './Automations.css';

import {
    Segment,
    Grid,
    Icon,
    Header,
    Input,
    Button,
    Modal,
    List,
    Divider,
    Menu,
    Form,
    Dropdown,
    Checkbox,
} from 'semantic-ui-react';

export const operands = [
    { key: 'EQUAL', text: '=', value: 'EQUAL' },
    {
        key: 'GREATER_EQUAL',
        text: '\u2265',
        value: 'GREATER_EQUAL',
    },
    {
        key: 'GREATER',
        text: '>',
        value: 'GREATER',
    },
    {
        key: 'LESS_EQUAL',
        text: '\u2264',
        value: 'LESS_EQUAL',
    },
    {
        key: 'LESS',
        text: '<',
        value: 'LESS',
    },
];

const deviceStateOptions = [
    { key: 'off', text: 'off', value: false },
    { key: 'on', text: 'on', value: true },
];

const thermostatOptions = [
    { key: 'HEATING', text: 'HEATING', value: 'HEATING' },
    { key: 'COOLING', text: 'COOLING', value: 'COOLING' },
    { key: 'IDLE', text: 'IDLE', value: 'IDLE' },
    { key: 'OFF', text: 'OFF', value: 'OFF' },
];

const thermostatOperands = [
    { key: 'EQUAL', text: '=', value: 'EQUAL' },
    { key: 'NOTEQUAL', text: '\u2260', value: 'NOTEQUAL' },
];

const CreateTrigger = (props) => {
    const [activeOperand, setActiveOperand] = useState(true);
    const [activeThermostat, setActiveThermostat] = useState(false);
    const operandsRef = useRef(null);
    const valuesRef = useRef(null);
    const notAdmitedDevices = ['buttonDimmer'];
    const hasOperand = new Set([
        'knobDimmer',
        'dimmableLight',
        'curtains',
        'sensor',
    ]);
    const deviceList = Object.values(props.devices)
        .map((device) => ({
            key: device.id,
            text: device.name,
            value: device.id,
            kind: device.kind,
        }))
        .filter((e) => !notAdmitedDevices.includes(e.kind));

    const onChange = (e, val) => {
        props.inputChange(val);
        setActiveOperand(hasOperand.has(props.devices[val.value].kind));
        setActiveThermostat(props.devices[val.value].kind === 'thermostat');
        if (operandsRef.current) operandsRef.current.setValue('');
        if (valuesRef.current) valuesRef.current.inputRef.current.valueAsNumber = undefined;
    };

    return (
      <List.Item>
        <List.Content>
          <Form>
            <Form.Group>
              <Form.Field inline width={7}>
                <Dropdown
                  onChange={onChange}
                  name="device"
                  search
                  selection
                  options={deviceList}
                  placeholder="Device"
                />
              </Form.Field>
              {
                  activeThermostat ? (
                    <>
                      <Form.Field inline width={2}>
                        <Dropdown
                          onChange={(e, val) => props.inputChange(val)}
                          ref={operandsRef}
                          name="operand"
                          compact
                          selection
                          options={thermostatOperands}
                        />
                      </Form.Field>
                      <Form.Field inline width={7}>
                        <Dropdown
                          onChange={(e, val) => props.inputChange(val)}
                          placeholder="State"
                          name="mode"
                          compact
                          selection
                          options={thermostatOptions}
                        />
                      </Form.Field>
                    </>
)
                : activeOperand ? (
                  <>
                    <Form.Field inline width={2}>
                      <Dropdown
                        onChange={(e, val) => props.inputChange(val)}
                        ref={operandsRef}
                        name="operand"
                        compact
                        selection
                        options={operands}
                      />
                    </Form.Field>
                    <Form.Field inline width={7}>
                      <Input
                        onChange={(e, val) => {
                                            props.inputChange(val);
                                        }}
                        ref={valuesRef}
                        name="value"
                        type="number"
                        placeholder="Value"
                      />
                    </Form.Field>
                  </>
                        ) : (
                          <Form.Field inline width={7}>
                            <Dropdown
                              onChange={(e, val) => props.inputChange(val)}
                              placeholder="State"
                              name="on"
                              compact
                              selection
                              options={deviceStateOptions}
                            />
                          </Form.Field>
                        )
}
            </Form.Group>
          </Form>
        </List.Content>
      </List.Item>
    );
};

const SceneItem = (props) => {
    const position = props.order.indexOf(props.scene.id);
    return (
      <List.Item>
        <List.Header>
          <Grid textAlign="center">
            <Grid.Row>
              <Grid.Column width={4}>
                <Checkbox
                  toggle
                  onChange={(e, val) => props.orderScenes(props.scene.id, val.checked)}
                  checked={position + 1 > 0}
                />
              </Grid.Column>
              <Grid.Column width={8}>
                <h3>{props.scene.name}</h3>
              </Grid.Column>
              <Grid.Column width={4}>
                <h3>{position !== -1 ? `# ${position + 1}` : ''}</h3>
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </List.Header>
      </List.Item>
    );
};

const Trigger = ({
                     deviceName, trigger, onRemove, index,
                 }) => {
    const {
 operand, value, on, mode,
} = trigger;
    let symbol;
    if (operand) {
        symbol = operands.filter((opt) => opt.key === operand)[0].text;
    }
    return (
      <List.Item className="trigger-item">
        <Menu compact>
          <Menu.Item as="span">{deviceName}</Menu.Item>
          {operand ? <Menu.Item as="span">{symbol}</Menu.Item> : ''}
          <Menu.Item as="span">{mode || (operand ? value : on ? 'on' : 'off')}</Menu.Item>
        </Menu>
        <Icon
          as="i"
          onClick={() => onRemove(index)}
          className="remove-icon"
          name="remove"
        />
      </List.Item>
    );
};

class AutomationSaveModal extends Component {
    constructor(props) {
        super(props);
        this.state = {
            triggerList: [],
            conditionsList: [],
            order: [],
            automationName: 'New Automation',
            editName: false,
            newTrigger: {},
            newCondition: {},
            scenesFilter: null,
            openModal: false,
        };

        if (this.props.automation) {
            this.state.automationName = this.props.automation.name;
            for (const scenePriority of this.props.automation.scenes) {
                this.state.order[scenePriority.priority] = scenePriority.sceneId;
            }
            for (const trigger of this.props.automation.triggers) {
                this.state.triggerList.push(
                    {
                        device: trigger.deviceId,
                        kind: trigger.kind,
                        ...(trigger.kind === 'booleanTrigger'
                            ? { on: trigger.on }
                            : {
                                operand: trigger.operator,
                                value: trigger.value,
                            }),
                    },
                );
            }
        }

        this.setTrigger = this._setter('triggerList');
        this.setOrder = this._setter('order');
        this.setautomationName = this._setter('automationName');
        this.setEditName = this._setter('editName');
        this.setNewTrigger = this._setter('newTrigger');
        this.addTrigger = this.addTrigger.bind(this);
        this.removeTrigger = this.removeTrigger.bind(this);
        this.onInputChange = this.onInputChange.bind(this);
        this.searchScenes = this.searchScenes.bind(this);
        this.orderScenes = this.orderScenes.bind(this);
        this.onChangeName = this.onChangeName.bind(this);

        // Conditions
        this.setNewCondition = this._setter('newCondition');
        this.addCondition = this.addCondition.bind(this);
        this.removeCondition = this.removeCondition.bind(this);
    }

    openModal = (e) => {
        this.setState({ openModal: true });
    };

    closeModal = (e) => {
        this.setState({ openModal: false });
    };

    get deviceList() {
        return Object.values(this.props.devices);
    }

    _setter(property) {
        return (value) => this.setState(update(this.state, { [property]: { $set: value } }));
    }

    triggerKind(trigger) {
        if ('operand' in trigger && 'value' in trigger) {
            return 'rangeTrigger';
        }
        if ('on' in trigger) {
            return 'booleanTrigger';
        }
        return false;
        // throw new Error("Trigger kind not handled");
    }

    conditionKind(condition) {
        if ('operand' in condition && 'value' in condition) {
            return 'rangeTrigger';
        }
        if ('on' in condition) {
            return 'booleanTrigger';
        }

        if ('operand' in condition && 'mode' in condition) {
            return 'thermostatCondition';
        }
        return false;
    }

    checkRange(deviceKind, devicesWithPercentage, trigger, error, device) {
        if (!trigger.device || !trigger.operand || !trigger.value) {
            return error;
        }
        if (trigger.value < 0) {
            error.message = 'Values cannot be negative';
            return error;
        }
        // If the device's range is a percentage, values cannot exceed 100
        if (
            devicesWithPercentage.includes(deviceKind)
            && trigger.value > 100
        ) {
            error.message = "The value can't exceed 100, as it's a percentage";
            return error;
        }
        if (
            deviceKind === 'sensor'
            && device.sensor === 'HUMIDITY'
            && trigger.value > 100
        ) {
            error.message = "The value can't exceed 100, as it's a percentage";
            return error;
        }
        return false;
    }

    checkBool(trigger, error) {
        if (!trigger.device || trigger.on === null || trigger.on === undefined) return error;
        return false;
    }

    _checkNewTrigger(trigger, isCondition = false) {
        const error = {
            result: false,
            message: 'There are missing fields!',
        };
        const device = Object.values(this.props.devices).filter(
            (d) => d.id === trigger.device,
        )[0];

        const triggerKind = this.triggerKind(trigger);
        const conditionKind = this.conditionKind(trigger);
        if (!isCondition && (!device || !triggerKind)) {
            error.message = 'There are missing fields';
            return error;
        }

        if (isCondition && !conditionKind) {
            error.message = 'There are missing fields';
            return error;
        }
        const deviceKind = device.kind;
        const devicesWithPercentage = ['dimmableLight', 'curtains', 'knobDimmer'];

        switch (isCondition ? conditionKind : triggerKind) {
            case 'booleanTrigger':
                const checkBoolTrigger = this.checkBool(trigger, error);
                if (checkBoolTrigger) {
                    return checkBoolTrigger;
                }
                break;
            case 'booleanCondition':
                const checkBoolCond = this.checkBool(trigger, error);
                if (checkBoolCond) {
                    return checkBoolCond;
                }
                break;
            case 'rangeTrigger':
                const checkRangeTrigger = this.checkRange(deviceKind, devicesWithPercentage, trigger, error, device);
                if (checkRangeTrigger) {
                    return checkRangeTrigger;
                }
                break;
            case 'rangeCondition':
                const checkRangeCond = this.checkRange(deviceKind, devicesWithPercentage, trigger, error, device);
                if (checkRangeCond) {
                    return checkRangeCond;
                }
                break;
            case 'thermostatCondition':
                if (!trigger.device || trigger.mode === null || trigger.mode === undefined || !trigger.operand) return error;
                break;
            default:
                throw new Error('theoretically unreachable statement');
        }

        let isNotDuplicate = null;

        if (isCondition === true) {
            isNotDuplicate = !this.state.conditionsList.some(
                (t) => t.device === trigger.device && t.operand === trigger.operand,
            );
        } else {
            isNotDuplicate = !this.state.triggerList.some(
                (t) => t.device === trigger.device && t.operand === trigger.operand,
            );
        }
        const type = isCondition ? 'condition' : 'trigger';
        const duplicationMessage = `You have already created a ${type} for this device with the same conditions`;
        return {
            result: isNotDuplicate,
            message: isNotDuplicate
                ? null
                : duplicationMessage,
        };
    }

    addTrigger() {
        const { result, message } = this._checkNewTrigger(this.state.newTrigger);
        if (result) {
            this.setState(
                update(this.state, {
                    triggerList: { $push: [this.state.newTrigger] },
                }),
            );
        } else {
            alert(message);
        }
    }

    removeTrigger(index) {
        this.setState(
            update(this.state, { triggerList: { $splice: [[index, 1]] } }),
        );
    }

    // This gets triggered when the devices dropdown changes the value.
    onInputChange(val) {
        if (val.name === 'device') {
            this.setNewTrigger({ [val.name]: val.value });
        } else {
            this.setNewTrigger({
                ...this.state.newTrigger,
                [val.name]: val.value,
            });
        }
    }

    onChangeName(_, val) {
        this.setautomationName(val.value);
    }

    orderScenes = (id, checked) => {
        if (checked) {
            this.setState(update(this.state, { order: { $push: [id] } }));
        } else {
            this.setState(
                update(this.state, {
                    order: (prevList) => prevList.filter((e) => e !== id),
                }),
            );
        }
    };

    searchScenes(_, { value }) {
        this.setState(update(this.state, { scenesFilter: { $set: value } }));
        this.forceUpdate();
    }

    get sceneList() {
        if (!this.scenesFilter) {
            return this.props.scenes;
        }
        return this.props.scenes.filter((e) => e.name.includes(this.scenesFilter));
    }

    generateBoolKey(trigger) {
        return `${trigger.device}${trigger.on}`;
    }

    generateRangeKey(trigger) {
        return `${trigger.device}${trigger.operand}${trigger.value}`;
    }

    _generateKey = (trigger, isCondition = false) => {
        switch (isCondition ? this.conditionKind(trigger) : this.triggerKind(trigger)) {
            case 'booleanTrigger':
                return this.generateBoolKey(trigger);
            case 'booleanCondition':
                return this.generateBoolKey(trigger);
            case 'rangeTrigger':
                return this.generateRangeKey(trigger);
            case 'rangeCondition':
                return this.generateRangeKey(trigger);
            case 'thermostatCondition':
                return `${trigger.device}${trigger.operand}${trigger.mode}`;
            default:
                throw new Error('theoretically unreachable statement');
        }
    };

    checkBeforeSave = () => {
        if (!this.state.automationName) {
            alert('Give a name to the automation');
            return false;
        }
        if (this.state.triggerList.length <= 0) {
            alert('You have to create a trigger');
            return false;
        }
        if (this.state.order.length <= 0) {
            alert('You need at least one active scene');
            return false;
        }
        return true;
    };

    saveAutomation = () => {
        if (this.checkBeforeSave()) {
            const automation = {
                name: this.state.automationName,
            };

            if (this.props.id) {
                automation.id = this.props.id;
                automation.triggers = [];
                automation.scenes = [];
                automation.conditions = [];
                for (let i = 0; i < this.state.order.length; i++) {
                    automation.scenes.push({
                        priority: i,
                        sceneId: this.state.order[i],
                    });
                }

                for (const trigger of this.state.triggerList) {
                    const kind = trigger.kind || this.triggerKind(trigger);
                    automation.triggers.push(
                        {
                            deviceId: trigger.device,
                            kind,
                            ...(kind === 'booleanTrigger'
                                ? { on: trigger.on }
                                : {
                                    operator: trigger.operand,
                                    range: parseInt(trigger.value),
                                }),
                        },
                    );
                }

                for (const condition of this.state.conditionsList) {
                    const kind = condition.kind || this.conditionKind(condition);
                    const loSpagnolo = (kind === 'thermostatCondition' ? { operator: condition.operand, mode: condition.mode }
                        : {
                            operator: condition.operand,
                            range: parseInt(condition.value),
                        });
                    automation.conditions.push(
                        {
                            deviceId: condition.device,
                            kind,
                            ...(kind === 'booleanTrigger'
                                ? { on: condition.on }
                                : loSpagnolo
                            ),
                        },
                    );
                }

                this.props
                    .fastUpdateAutomation(automation)
                    .then(this.closeModal)
                    .catch(console.error);
            } else {
                this.props
                    .saveAutomation({
                        automation,
                        triggerList: this.state.triggerList,
                        order: this.state.order,
                        conditionList: this.state.conditionsList,
                    })
                    .then(this.closeModal)
                    .catch(console.error);
            }
        }
    };

    get trigger() {
        return this.props.id ? (
          <Button
            style={{ display: 'inline' }}
            circular
            size="small"
            icon="edit"
            onClick={this.openModal}
          />
        ) : (
          <Button icon labelPosition="left" color="green" onClick={this.openModal}>
            <Icon name="add" />
                Create new automation
          </Button>
        );
    }

    // CONDITIONS

    addCondition() {
        // Same method used to check triggers and conditions, not a mistake
        const { result, message } = this._checkNewTrigger(this.state.newCondition, true);
        if (result) {
            this.setState(
                update(this.state, {
                    conditionsList: { $push: [this.state.newCondition] },
                }),
            );
        } else {
            alert(message);
        }
    }

    removeCondition(index) {
        this.setState(
            update(this.state, { conditionsList: { $splice: [[index, 1]] } }),
        );
    }

    onInputChangeCondition = (val) => {
        if (val.name === 'device') {
            this.setNewCondition({ [val.name]: val.value });
        } else {
            this.setNewCondition({
                ...this.state.newCondition,
                [val.name]: val.value,
            });
        }
    }

    render() {
        return (
          <Modal
            closeIcon
            trigger={this.trigger}
            open={this.state.openModal}
            onClose={this.closeModal}
          >
            <Segment basic>
              <Header style={{ display: 'inline', marginRight: '1rem' }}>
                {this.state.editName ? (
                  <Input
                    focus
                    transparent
                    placeholder="New automation name..."
                    onChange={this.onChangeName}
                  />
                        ) : (
                            this.state.automationName
                        )}
              </Header>
              <Button
                onClick={() => this.setEditName((prev) => !prev)}
                style={{ display: 'inline' }}
                circular
                size="small"
                icon={this.state.editName ? 'save' : 'edit'}
              />

              <Segment placeholder className="segment-automations">
                <Grid columns={2} stackable textAlign="center">
                  <Divider vertical />
                  <Grid.Row verticalAlign="middle">
                    <Grid.Column>
                      <Header>Create Triggers</Header>
                      <List divided relaxed>
                        {this.state.triggerList.length > 0
                                        && this.state.triggerList.map((trigger, i) => {
                                            const deviceName = this.deviceList.filter(
                                                (d) => d.id === trigger.device,
                                            )[0].name;
                                            const key = this._generateKey(trigger);
                                            return (
                                              <Trigger
                                                key={key}
                                                index={i}
                                                deviceName={deviceName}
                                                trigger={trigger}
                                                onRemove={this.removeTrigger}
                                              />
                                            );
                                        })}
                        <CreateTrigger
                          devices={this.props.devices}
                          inputChange={this.onInputChange}
                        />
                      </List>
                      <Button
                        onClick={this.addTrigger}
                        circular
                        icon="add"
                        color="blue"
                        size="huge"
                      />
                    </Grid.Column>
                    <Grid.Column>
                      {this.props.scenes.length > 0 ? (
                        <>
                          <Header>Activate Scenes</Header>
                          <Input
                            icon="search"
                            placeholder="Search..."
                            fluid
                            onChange={this.searchScenes}
                          />
                          <Divider horizontal />
                          <List divided relaxed>
                            {this.sceneList.map((scene) => (
                              <SceneItem
                                key={scene.id}
                                scene={scene}
                                order={this.state.order}
                                orderScenes={this.orderScenes}
                              />
                                                ))}
                          </List>
                        </>
                                    ) : (
                                      <>
                                        <Header icon>
                                          <Icon name="world" />
                                        </Header>
                                        <Button primary>Create Scene</Button>
                                      </>
                                    )}
                    </Grid.Column>
                  </Grid.Row>
                </Grid>
              </Segment>
              <Grid columns={1} stackable textAlign="center">
                <Grid.Row verticalAlign="middle">
                  <Grid.Column>
                    <Header>Add Conditions</Header>
                    <List divided relaxed>
                      {this.state.conditionsList.length > 0
                                    && this.state.conditionsList.map((condition, i) => {
                                        const deviceName = this.deviceList.filter(
                                            (d) => d.id === condition.device,
                                        )[0].name;
                                        const key = this._generateKey(condition, true);
                                        return (
                                          <Trigger
                                            key={key}
                                            index={i}
                                            deviceName={deviceName}
                                            trigger={condition}
                                            onRemove={this.removeCondition}
                                          />
                                        );
                                    })}
                      <CreateTrigger
                        devices={this.props.devices}
                        inputChange={this.onInputChangeCondition}
                      />
                    </List>
                    <Button
                      onClick={this.addCondition}
                      circular
                      icon="add"
                      color="blue"
                      size="huge"
                    />
                  </Grid.Column>
                </Grid.Row>
              </Grid>
              <Grid>
                <Grid.Row>
                  <Grid.Column style={{ marginRight: '1rem' }}>
                    <Button onClick={() => this.saveAutomation()} color="green">
                      {this.props.id ? 'Save' : 'Create'}
                    </Button>
                  </Grid.Column>
                </Grid.Row>
              </Grid>
            </Segment>
          </Modal>
        );
    }
}

const mapStateToProps = (state, ownProps) => ({
    scenes: Object.values(state.scenes),
    devices: state.devices,
    automation: ownProps.id ? state.automations[ownProps.id] : null,
});
const AutomationSaveModalContainer = connect(
    mapStateToProps,
    RemoteService,
)(AutomationSaveModal);
export default AutomationSaveModalContainer;
