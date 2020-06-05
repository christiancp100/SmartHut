import React, { Component } from 'react';
import { Checkbox, Icon } from 'semantic-ui-react';
import { RemoteService } from '../../../remote';
import { connect } from 'react-redux';
import './Thermostat.css';
import Slider from 'react-rangeslider';
import 'react-rangeslider/lib/index.css';
import mapStateToProps from '../../../deviceProps';

import {
  stateTag,
  container,
  deviceName,
  targetTemperature,
  toggle,
  stateTagContainer,
} from './ThermostatStyle';

class Thermostats extends Component {
  constructor(props) {
    super(props);
    this.state = {
      targetTemperature: this.props.device.targetTemperature,
      mode: this.props.device.mode,
      measuredTemperature: this.props.device.measuredTemperature,
    };
    this.setMode = this.setMode.bind(this);
    this.setTargetTemperature = this.setTargetTemperature.bind(this);
  }

  setMode(mode) {
    // i came to the conclusion that is not possible to set mode.
    // Good job Jacob (Claudio)
    // this.mode = "HEATING";
    const turnOn = mode;
    if (this.props.tab === 'Devices') {
      this.props
        .saveDevice({ ...this.props.stateOrDevice, turnOn })
        .catch((err) => console.error('thermostat update error', err));
    } else {
      this.props.updateState(
        { id: this.props.stateOrDevice.id, on: turnOn },
        this.props.stateOrDevice.kind,
      );
    }
  }

  onClickDevice = () => {
    const on = !this.turnedOn;
    if (this.props.tab === 'Devices') {
      this.props
        .saveDevice({ ...this.props.stateOrDevice, on })
        .catch((err) => console.error('thermostat update error', err));
    } else {
      this.props.updateState(
        { id: this.props.stateOrDevice.id, on },
        this.props.stateOrDevice.kind,
      );
    }
  };

  saveTargetTemperature(targetTemperature) {
    const turn = this.props.stateOrDevice.mode !== 'OFF';
    if (this.props.tab === 'Devices') {
      this.props
        .saveDevice({
          ...this.props.stateOrDevice,
          targetTemperature,
          turnOn: turn,
        })
        .catch((err) => console.error('thermostat update error', err));
    } else {
      this.props.updateState(
        {
          id: this.props.stateOrDevice.id,
          targetTemperature,
        },
        this.props.stateOrDevice.kind,
      );
    }
  }

  setTargetTemperature() {
    this.saveTargetTemperature(this.state.targetTemperature);
  }

  handleChange = (value) => {
    this.setState({ ...this.state, targetTemperature: value });
  };

  handleCheckbox = (val) => {
    const useExternalSensors = val;
    const turnOn = this.props.stateOrDevice.mode !== 'OFF';
    if (this.props.tab === 'Devices') {
      this.props
        .saveDevice({ ...this.props.stateOrDevice, useExternalSensors, turnOn })
        .catch((err) => console.error('thermostat update error', err));
    }
  };

  render() {
    return (
      <div style={container}>
        <h3 style={deviceName}>
          Thermostat
          <Checkbox
            disabled={this.props.disabled}
            checked={
              this.props.tab === 'Devices'
                ? this.props.device.mode !== 'OFF'
                : this.props.stateOrDevice.on
            }
            toggle
            style={toggle}
            onChange={(e, val) => this.setMode(val.checked)}
          />
        </h3>
        <hr />
        <div style={targetTemperature}>
          <Icon name="thermometer half" />
          {' '}
          {this.props.device.measuredTemperature}
          {' '}
ºC
          <br />
          <Icon name="target" />
          {' '}
          {this.props.device.targetTemperature.toFixed(1)}
          {' '}
ºC
        </div>
        {this.props.tab === 'Devices' ? (
          <>
            <Slider
              disabled={this.props.disabled}
              min={10}
              max={30}
              step={0.1}
              tooltip={false}
              className="slider-css"
              value={this.state.targetTemperature}
              onChange={(event) => this.handleChange(event)}
              onChangeComplete={() => this.setTargetTemperature()}
            />
            <Checkbox
              style={{ padding: '0 .7rem' }}
              label="Use external sensors"
              name="external"
              toggle
              checked={this.props.stateOrDevice.useExternalSensors}
              disabled={!this.props.tempSensorsInRoom}
              onChange={(e, val) => this.handleCheckbox(val.checked)}
            />
          </>
        ) : null}

        <div style={stateTagContainer}>
          <span style={stateTag}>
            {this.props.tab !== 'Scenes'
              ? this.props.device.mode
              : this.props.stateOrDevice.on
              ? 'WILL TURN ON'
              : 'WILL TURN OFF'}
          </span>
        </div>
      </div>
    );
  }
}

const mapStateToProps2 = (state, ownProps) => ({
  ...mapStateToProps(state, ownProps),
  get tempSensorsInRoom() {
    if (state.active.activeTab !== 'Devices') return false;
    const room = state.rooms[state.devices[ownProps.id].roomId];
    if (!room) return false;
    const deviceIds = room.devices;
    const devices = [...deviceIds].map((id) => state.devices[id]);
    const sensors = devices.filter(
      (d) => d.kind === 'sensor' && d.sensor === 'TEMPERATURE',
    );
    return sensors.length > 0;
  },
});

const ThermostatContainer = connect(
  mapStateToProps2,
  RemoteService,
)(Thermostats);
export default ThermostatContainer;
