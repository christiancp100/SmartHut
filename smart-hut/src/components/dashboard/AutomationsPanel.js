import React, { Component } from 'react';
import { connect } from 'react-redux';
import { RemoteService } from '../../remote';
import './Automations.css';

import {
  Segment,
  Grid,
  Header,
  Button,
  List,
  Divider,
  Menu,
} from 'semantic-ui-react';
import CreateAutomation, { operands } from './AutomationCreationModal';

const Automation = ({
 automation, devices, scenes, removeAutomation,
}) => {
  const { triggers, conditions } = automation;
  const scenePriorities = automation.scenes;
  const getOperator = (operand) => operands.filter((o) => o.key === operand)[0].text;

  return (
    <>
      <Header style={{ display: 'inline', marginRight: '1rem' }}>
        {automation.name}
      </Header>
      <CreateAutomation id={automation.id} />
      <Button
        style={{ display: 'inline' }}
        circular
        onClick={() => removeAutomation(automation.id)}
        color="red"
        size="small"
        icon="trash alternate outline"
      />
      <Segment placeholder>
        <Grid columns={2} stackable textAlign="center">
          <Divider vertical />
          <Grid.Row verticalAlign="middle">
            <Grid.Column>
              <Header>Triggers</Header>
              <List divided relaxed>
                {triggers !== undefined
                  && triggers.map((trigger) => {
                    const device = devices.filter(
                      (d) => d.id === trigger.deviceId,
                    )[0];
                    return (
                      <Menu key={trigger.id} compact>
                        <Menu.Item as="span">
                          {device.name}
                          {' '}
                          {trigger.operator
                            ? `${getOperator(trigger.operator)
                              } ${
                              trigger.range}`
                            : trigger.on
                            ? ' - on'
                            : ' - off'}
                        </Menu.Item>
                      </Menu>
                    );
                  })}
              </List>
            </Grid.Column>
            <Grid.Column>
              <Header>Scenes</Header>
              <List divided relaxed>
                {scenePriorities !== undefined
                  && scenePriorities.map((sp) => {
                    const sceneData = scenes.filter(
                      (s) => s.id === sp.sceneId,
                    )[0];
                    if (!sceneData) return '';
                    return (
                      <Menu key={sceneData.id} compact>
                        <Menu.Item as="span">{sceneData.name}</Menu.Item>
                      </Menu>
                    );
                  })}
              </List>
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </Segment>
      <Grid columns={1} stackable textAlign="center">
        <Grid.Row verticalAlign="middle">
          <Grid.Column>
            <Header>Conditions</Header>
            <List divided relaxed>
              {conditions !== undefined
                  && conditions.map((condition) => {
                    const device = devices.filter(
                      (d) => d.id === condition.deviceId,
                    )[0];
                    return (
                      <Menu key={condition.id} compact>
                        <Menu.Item as="span">
                          {device.name}
                          {' '}
                          {condition.operator
                            ? `${getOperator(condition.operator)
                              } ${
                              condition.mode ? condition.mode : condition.range}`
                            : condition.on
                            ? ' - on'
                            : ' - off'}
                        </Menu.Item>
                      </Menu>
                    );
                  })}
            </List>
          </Grid.Column>
        </Grid.Row>
      </Grid>
    </>
  );
};

class AutomationsPanel extends Component {
  constructor(props) {
    super(props);
    this.state = { openModal: false };
    this.getDevices();
    this.getScenes();
    this.getAutomations();
  }

  getScenes() {
    this.props.fetchAllScenes().catch(console.error);
  }

  getDevices() {
    this.props
      .fetchDevices()
      .catch((err) => console.error('error fetching devices:', err));
  }

  getAutomations() {
    this.props
      .fetchAutomations()
      .catch((err) => console.error('error fetching automations:', err));
  }

  removeAutomation = (id) => {
    this.props
      .deleteAutomation(id)
      .catch((err) => console.error(`error removing automation ${id}:`, err));
  };

  render() {
    return (
      <Grid style={{ marginTop: '3rem' }}>
        <Grid.Row>
          <Grid.Column textAlign="center" width={16}>
            {!this.state.openModal ? (
              <List>
                <CreateAutomation />
              </List>
            ) : (
              <Button color="green">CREATE AUTOMATION</Button>
            )}
          </Grid.Column>
        </Grid.Row>
        <Grid.Row>
          {this.props.automations.map((automation, i) => (
            <Grid.Column key={i} width={8} style={{ margin: '2rem 0' }}>
              <Automation
                removeAutomation={this.removeAutomation}
                scenes={this.props.scenes}
                devices={this.props.devices}
                automation={automation}
              />
            </Grid.Column>
            ))}
        </Grid.Row>
      </Grid>
    );
  }
}

const mapStateToProps = (state, _) => ({
  activeRoom: state.active.activeRoom,
  activeTab: state.active.activeTab,
  get scenes() {
    return Object.values(state.scenes);
  },
  get devices() {
    return Object.values(state.devices);
  },
  get automations() {
    return Object.values(state.automations);
  },
});
const AutomationsPanelContainer = connect(
  mapStateToProps,
  RemoteService,
)(AutomationsPanel);
export default AutomationsPanelContainer;
