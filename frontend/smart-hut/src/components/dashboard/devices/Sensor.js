/**
 * Users can add sensors in their rooms.
 * Sensors typically measure physical quantities in a room.
 * You must support temperature sensors, humidity sensors, light sensors (which measure luminosity1).
 * Sensors have an internal state that cannot be changed by the user.
 * For this story, make the sensors return a constant value with some small random error.
 */

/*

  OPTIONAL STATE
  error: 2.4

  <text style={errorStyle} x={100} y={100} textAnchor="middle" dy="0.6em" fontWeight="bold">
    &#177;{this.state.error}
  </text>


  errorStyle,
 */

import React, { Component } from 'react';
import { CircularInput, CircularProgress } from 'react-circular-input';
import { Image } from 'semantic-ui-react';
import { connect } from 'react-redux';
import {
  container,
  sensorText,
  style,
  valueStyle,
  motionSensorInnerCircle,
  motionSensorOuterCircle,
  nameMotionStyle,
  motionSensorIcon,
  temperatureSensorColors,
  lightSensorColors,
  humiditySensorColors,
  iconSensorStyle,
} from './SensorStyle';
import { RemoteService } from '../../../remote';
import mapStateToProps from '../../../deviceProps';

class Sensor extends Component {
  constructor(props) {
    super(props);
    this.state = {
      value: 0,
      motion: false,
    };
    this.units = '';
    this.stateCallback = (e) => {
      this.setState(Object.assign(this.state, e));
    };

    this.colors = temperatureSensorColors;
    this.icon = 'temperatureIcon.svg';
    this.name = 'Sensor';
  }

  componentDidUpdate(prevProps) {
    if (
      this.props.stateOrDevice.kind === 'sensor'
      && this.props.stateOrDevice.value !== prevProps.stateOrDevice.value
    ) {
      this.setState({ value: this.props.stateOrDevice.value });
    } else if (
      this.props.stateOrDevice.kind === 'motionSensor'
      && this.props.stateOrDevice.detected !== prevProps.stateOrDevice.detected
    ) {
      this.setState({
        motion: true,
        detected: this.props.stateOrDevice.detected,
      });
    }
  }

  componentDidMount() {
    if (this.props.stateOrDevice.kind === 'sensor') {
      switch (this.props.stateOrDevice.sensor) {
        case 'TEMPERATURE':
          this.units = 'ºC';
          this.colors = temperatureSensorColors;
          this.icon = 'temperatureIcon.svg';
          this.name = 'Temperature Sensor';
          break;
        case 'HUMIDITY':
          this.units = '%';
          this.colors = humiditySensorColors;
          this.icon = 'humidityIcon.svg';
          this.name = 'Humidity Sensor';
          break;
        case 'LIGHT':
          this.units = 'lm';
          this.colors = lightSensorColors;
          this.icon = 'lightSensorIcon.svg';
          this.name = 'Light Sensor';
          break;
        default:
          this.units = '';
      }
      this.setState({
        value: this.props.stateOrDevice.value,
      });
    } else {
      this.setState({
        detected: this.props.stateOrDevice.detected,
        motion: true,
      });
    }
  }

  getIcon = () => {
    if (this.state.detected) {
      return this.iconOn;
    }
    return this.iconOff;
  };

  temperatureColor = (value) => {
    let hue = 100;
    const min = 16;
    const max = 20;
    if (value >= min && value < max) {
      hue = 100 - ((value - min) * 100) / (max - min);
    } else if (value >= max) {
      hue = 0;
    }
    return `hsl(${hue}, 100%, 50%)`;
  };

  render() {
    const MotionSensor = (props) => (
      <div
        style={{
            ...motionSensorOuterCircle,
            backgroundColor: this.state.detected ? '#505bda' : '#00bdaa',
          }}
      >
        <div
          style={{
              ...motionSensorInnerCircle,
              backgroundColor: this.state.detected ? '#fe346e' : '#00bdaa',
            }}
        >
          <Image style={motionSensorIcon} src="/img/motionSensorIcon.svg" />
          <span style={nameMotionStyle}>Motion Sensor</span>
        </div>
      </div>
      );

    return (
      <div style={container}>
        {this.state.motion ? (
          <MotionSensor />
        ) : (
          <>
            <CircularInput
              value={
                this.props.stateOrDevice.sensor === 'LIGHT'
                  ? this.state.value / 2000
                  : this.state.value / 100
              }
              style={style}
            >
              <CircularProgress
                strokeWidth="2rem"
                stroke={
                  this.props.stateOrDevice.sensor === 'TEMPERATURE'
                    ? this.temperatureColor(this.state.value)
                    : this.colors.progress
                }
                fill={this.colors.circle}
              />
              <text
                style={{
                  ...valueStyle,
                  fill: this.colors.text,
                }}
                x={100}
                y={110}
                textAnchor="middle"
                dy="0.3em"
                fontWeight="bold"
                fill={this.colors.text}
              >
                {+(`${Math.round(`${this.state.value}e+2`)}e-2`)}
                {this.units}
              </text>
              <text
                style={{
                  ...sensorText,
                  fill: this.colors.text,
                }}
                x={100}
                y={150}
                textAnchor="middle"
                dy="0.4em"
                fontWeight="bold"
              >
                {this.name}
              </text>
            </CircularInput>
            <Image style={iconSensorStyle} src={`/img/${this.icon}`} />
          </>
        )}
      </div>
    );
  }
}

const SensorContainer = connect(mapStateToProps, RemoteService)(Sensor);
export default SensorContainer;
