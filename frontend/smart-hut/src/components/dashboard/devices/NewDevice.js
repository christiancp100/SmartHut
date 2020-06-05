import React, { Component } from 'react';
import styled from 'styled-components';
import {
  Button,
  Dropdown,
  Form,
  Icon,
  Image,
  Input,
  Modal,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import { RemoteService } from '../../../remote';

const StyledDiv = styled.div`
  background-color: #505bda;
  padding: 3rem;
  width: 10rem;
  height: 10rem;
  border-radius: 100%;
  border: none;
  position: relative;
  box-shadow: 3px 2px 10px 5px #ccc;
  transition: all 0.3s ease-out;
  :hover {
    background-color: #4345d9;
  }
  :active {
    transform: translate(0.3px, 0.8px);
    box-shadow: 0.5px 0.5px 7px 3.5px #ccc;
  }
`;

class NewDevice extends Component {
  constructor(props) {
    super(props);
    this.state = {
      step: 1,
      openModal: false,
      motion: false,
      deviceName: '',
    };
    this.baseState = this.state;
    this.createDevice = this.createDevice.bind(this);
  }

  handleOpen = () => {
    this.setState({ openModal: true });
  };

  handleClose = () => {
    this.setState({ openModal: false });
  };

  resetState = () => {
    this.setState(this.baseState);
    this.handleClose();
  };

  nextStep = () => {
    this.setState((prevState) => ({ step: prevState.step + 1 }));
  };

  previousStep = () => {
    this.setState((prevState) => ({ step: prevState.step - 1 }));
  };

  setTypeOfDevice = (e, d) => {
    if (d.value === 'dimmableLight') {
      this.setState({ typeOfDevice: d.value, intensity: 0 });
    } else {
      this.setState({ typeOfDevice: d.value });
    }
  };

  setDeviceName = (e, d) => {
    this.setState({ deviceName: d.value });
  };

  setTypeOfSensor = (e, d) => {
    console.log(d.value);
    if (d.value === 'motionSensor') {
      this.setState({ typeOfSensor: d.value, motion: true });
    } else {
      this.setState({ typeOfSensor: d.value });
    }
  };

  setLightsDimmerSwitch = (e, d) => {
    this.setState({ lightsAttached: d.value });
  };

  async createDevice() {
    // Connect to the backend and create device here.
    const data = {
      id: null,
      roomId: this.props.activeRoom,
      name: this.state.deviceName,
      kind: this.state.motion ? 'motionSensor' : this.state.typeOfDevice,
    };
    let outputs = null;

    const defaultNames = {
      regularLight: 'New regular light',
      dimmableLight: 'New intensity light',
      smartPlug: 'New smart Plug',
      sensor: 'New sensor',
      switch: 'New switch',
      buttonDimmer: 'New button dimmer',
      knobDimmer: 'New knob dimmer',
      securityCamera: 'New security camera',
      thermostat: 'New thermostat',
      curtains: 'New curtains',
    };

    if (this.state.deviceName === '') {
      data.name = defaultNames[this.state.typeOfDevice];
    }
    console.log('-------------------------');
    console.log(this.state.typeOfDevice);

    switch (this.state.typeOfDevice) {
      // trying to make securityCamera work
      // case "securityCamera":
      // data.path="/security_camera_videos/security_camera_1.mp4";
      // data.on=false;
      // break;
      // trying to make thermostat work
      case 'thermostat':
        data.targetTemperature = 0;
        data.measuredTemperature = 0;
        break;
      case 'dimmableLight':
        data.intensity = 0;
        break;
      case 'sensor':
        if (!this.state.motion) {
          data.sensor = this.state.typeOfSensor;
          data.value = 0;
        }
        break;
      case 'switch':
      case 'buttonDimmer':
      case 'knobDimmer':
        outputs = this.state.lightsAttached;
        if (
          this.state.lightsAttached === undefined
          || this.state.lightsAttached.length === 0
        ) {
          alert(
            'No lights attached to this switch! Please, add a light a first.',
          );
          return;
        }
        break;
      default:
        break;
    }

    try {
      const newDevice = await this.props.saveDevice(data);
      if (outputs) {
        await this.props.connectOutputs(newDevice, outputs);
      }
      this.resetState();
    } catch (e) {
      console.error('device creation error: ', e);
    }
  }

  render() {
    const deviceOptions = [
      // stuff
      {
        key: 'thermostat',
        text: 'Thermostat',
        value: 'thermostat',
        image: { avatar: true, src: '/img/thermostat-icon.png' },
      },
      {
        key: 'curtains',
        text: 'Curtain',
        value: 'curtains',
        image: { avatar: true, src: '/img/curtains-icon.png' },
      },
      // stuff ends
      {
        key: 'light',
        text: 'Normal Light',
        value: 'regularLight',
        image: { avatar: true, src: '/img/lightOn.svg' },
      },
      {
        key: 'intensity-light',
        text: 'Intensity Light',
        value: 'dimmableLight',
        image: { avatar: true, src: '/img/intensity-light.svg' },
      },
      {
        key: 'smart-plug',
        text: 'Smart Plug',
        value: 'smartPlug',
        image: { avatar: true, src: '/img/smart-plug.svg' },
      },
      {
        key: 'sensor',
        text: 'Sensor',
        value: 'sensor',
        image: { avatar: true, src: '/img/sensorOn.svg' },
      },
      {
        key: 'switch',
        text: 'Switch',
        value: 'switch',
        image: { avatar: true, src: '/img/switchOn.svg' },
      },
      {
        key: 'knobDimmer',
        text: 'Knob Dimmer',
        value: 'knobDimmer',
        image: { avatar: true, src: '/img/knob.svg' },
      },
      {
        key: 'buttonDimmer',
        text: 'Button Dimmer',
        value: 'buttonDimmer',
        image: { avatar: true, src: '/img/plusMinus.svg' },
      },
      {
        key: 'securityCamera',
        text: 'Security Camera',
        value: 'securityCamera',
        image: { avatar: true, src: '/img/security-icon.png' },
      },
    ];
    const sensorOptions = [
      {
        key: 'temperature',
        text: 'Temperature Sensor',
        value: 'TEMPERATURE',
        image: { avatar: true, src: '/img/temperature-sensor.svg' },
      },
      {
        key: 'humidity',
        text: 'Humidity Sensor',
        value: 'HUMIDITY',
        image: { avatar: true, src: '/img/humidity-sensor.svg' },
      },
      {
        key: 'light',
        text: 'Light Sensor',
        value: 'LIGHT',
        image: { avatar: true, src: '/img/light-sensor.svg' },
      },
      {
        key: 'motion',
        text: 'Motion Sensor',
        value: 'motionSensor',
        image: { avatar: true, src: '/img/sensorOn.svg' },
      },
    ];
    const availableSwitchDevices = [];
    const availableDimmerDevices = [];
    this.props.devices.forEach((d) => {
      if (
        d.kind === 'regularLight'
        || d.kind === 'dimmableLight'
        || d.kind === 'smartPlug'
      ) {
        availableSwitchDevices.push({
          key: d.id,
          text: d.name,
          value: d.id,
        });
      }
      if (d.kind === 'dimmableLight') {
        availableDimmerDevices.push({
          key: d.id,
          text: d.name,
          value: d.id,
        });
      }
    });
    const step1 = (
      <Dropdown
        name="typeOfDevice"
        placeholder="Select a Type of Device"
        fluid
        selection
        onChange={this.setTypeOfDevice}
        options={deviceOptions}
      />
    );
    const step2 = (typeOfDevice) => {
      const deviceName = (
        <div>
          <Form.Field>
            <label>Device Name: </label>
            <Input
              fluid
              size="large"
              onChange={this.setDeviceName}
              focus
              placeholder="Device Name"
            />
          </Form.Field>
        </div>
      );
      const sensorForm = (
        <Form.Field style={{ marginTop: '1rem' }}>
          <label>Type of Sensor: </label>
          <Dropdown
            name="typeOfDevice"
            placeholder="Select a Type of Sensor"
            fluid
            selection
            onChange={this.setTypeOfSensor}
            options={sensorOptions}
          />
        </Form.Field>
      );
      const switchOptions = (
        <Form.Field style={{ marginTop: '1rem' }}>
          <label>Select the lights or smart plugs You Want to Attach: </label>
          <Dropdown
            name="typeOfDevice"
            placeholder="Select Lights"
            fluid
            multiple
            onChange={this.setLightsDimmerSwitch}
            options={availableSwitchDevices}
          />
        </Form.Field>
      );
      const dimmerOptions = (
        <Form.Field style={{ marginTop: '1rem' }}>
          <label>Select the dimmable lights You Want to Attach: </label>
          <Dropdown
            name="typeOfDevice"
            placeholder="Select Lights"
            fluid
            multiple
            onChange={this.setLightsDimmerSwitch}
            options={availableDimmerDevices}
          />
        </Form.Field>
      );
      return (
        <Form>
          {deviceName}
          {this.state.typeOfDevice === 'sensor' ? sensorForm : ''}
          {this.state.typeOfDevice === 'switch' ? switchOptions : ''}
          {this.state.typeOfDevice === 'buttonDimmer'
          || this.state.typeOfDevice === 'knobDimmer'
            ? dimmerOptions
            : ''}
        </Form>
      );
    };
    const steps = [step1, step2()];
    return (
      <Modal
        closeIcon
        open={this.state.openModal}
        onClose={this.resetState}
        trigger={(
          <StyledDiv
            onClick={this.handleOpen}
            style={{
              position: 'relative',
              top: 'calc(50% - 5rem)',
              left: 'calc(50% - 5rem)',
            }}
          >
            <Image src="/img/add.svg" style={{ filter: 'invert()' }} />
          </StyledDiv>
        )}
        centered
      >
        <Modal.Header>Add a New Device</Modal.Header>
        <Modal.Content>{steps[this.state.step - 1]}</Modal.Content>
        <Modal.Actions>
          {this.state.step > 1 ? (
            <Button
              onClick={this.previousStep}
              color="blue"
              icon
              labelPosition="left"
            >
              <Icon name="left arrow" />
              Back
            </Button>
          ) : (
            ''
          )}
          {this.state.step < steps.length ? (
            <Button
              color="blue"
              onClick={this.nextStep}
              icon
              labelPosition="right"
            >
              Next
              <Icon name="right arrow" />
            </Button>
          ) : (
            ''
          )}
          {this.state.step === steps.length ? (
            <Button
              onClick={this.createDevice}
              color="blue"
              icon
              labelPosition="right"
            >
              <Icon name="up arrow" />
              Finish
            </Button>
          ) : (
            ''
          )}
        </Modal.Actions>
      </Modal>
    );
  }
}

const mapStateToProps = (state, _) => ({
  devices: Object.values(state.devices),
  activeRoom: state.active.activeRoom,
});
const NewDeviceContainer = connect(mapStateToProps, RemoteService)(NewDevice);
export default NewDeviceContainer;
