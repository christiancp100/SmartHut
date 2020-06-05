import React from 'react';
import {
 Header, Button, Icon, Card,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import Light from './Light';
import SmartPlug from './SmartPlug';
import Sensor from './Sensor';
import { ButtonDimmer, KnobDimmer } from './Dimmer';
import Switcher from './Switch';
import Videocam from './Videocam';
import Curtains from './Curtain';
import Thermostat from './Thermostats';
import { RemoteService } from '../../../remote';
import DeviceSettingsModal from './DeviceSettingsModal';
import mapStateToProps from '../../../deviceProps';

const centerComponent = {
  marginLeft: '50%',
  transform: 'translateX(-50%)',
  marginTop: '10%',
  marginBottom: '10%',
};

class Device extends React.Component {
  constructor(props) {
    super(props);

    this.modalRef = React.createRef();
    this.edit = this.edit.bind(this);
    this.resetSmartPlug = this.resetSmartPlug.bind(this);
    this.deleteState = this.deleteState.bind(this);
  }

  edit() {
    console.log(`editing device with id=${this.props.id}`);
    this.modalRef.current.openModal();
  }

  resetSmartPlug() {
    this.props
      .smartPlugReset(this.props.id)
      .catch((err) => console.error('Smart plug reset error', err));
  }

  deleteState() {
    this.props.deleteState(this.props.id, this.props.stateOrDevice.kind);
  }

  renderDeviceComponent() {
    const mapKindToComponent = {
      curtains: Curtains,
      thermostat: Thermostat,
      regularLight: Light,
      sensor: Sensor,
      motionSensor: Sensor,
      buttonDimmer: ButtonDimmer,
      knobDimmer: KnobDimmer,
      smartPlug: SmartPlug,
      switch: Switcher,
      dimmableLight: Light,
      securityCamera: Videocam,
    };

    if (!(this.props.type in mapKindToComponent)) {
      throw new Error(`device kind ${this.props.type} not known`);
    }

    return React.createElement(
      mapKindToComponent[this.props.type],
      {
        tab: this.props.tab,
        id: this.props.id,
        hostId: this.props.hostId,
      },
      '',
    );
  }

  deviceDescription() {
    return (
      <div className="ui two buttons">
        <Button color="blue" icon onClick={this.edit} labelPosition="left">
          <Icon name="pencil" />
          Edit
        </Button>
        {this.props.stateOrDevice.kind === 'smartPlug' ? (
          <Button
            color="orange"
            icon
            onClick={this.resetSmartPlug}
            labelPosition="left"
          >
            <Icon name="undo" />
            Reset
          </Button>
        ) : null}
      </div>
    );
  }

  stateDescription() {
    return (
      <div className="ui two buttons">
        <Button
          color="red"
          icon
          onClick={this.deleteState}
          labelPosition="left"
        >
          <Icon name="undo" />
          Delete
        </Button>
      </div>
    );
  }

  get deviceName() {
    return this.props.device.name;
  }

  render() {
    return (
      <Card style={{ height: '27em' }}>
        <Card.Content>
          <Card.Header textAlign="center">
            <Header as="h3">{this.deviceName}</Header>
            <Header as="h4" style={{ marginTop: '.5rem' }}>
              {this.props.roomName}
            </Header>
          </Card.Header>

          <Card.Description
            style={
              this.props.device.kind !== 'curtains' ? centerComponent : null
            }
          >
            {this.renderDeviceComponent()}
          </Card.Description>
        </Card.Content>
        <Card.Content extra>
          {this.props.tab === 'Devices'
            ? this.deviceDescription()
            : this.props.tab === 'Scenes' && this.stateDescription()}
        </Card.Content>
        {this.props.tab === 'Devices' ? (
          <DeviceSettingsModal
            ref={this.modalRef}
            id={this.props.stateOrDevice.id}
          />
        ) : null}
      </Card>
    );
  }
}

const DeviceContainer = connect(mapStateToProps, RemoteService)(Device);
export default DeviceContainer;
