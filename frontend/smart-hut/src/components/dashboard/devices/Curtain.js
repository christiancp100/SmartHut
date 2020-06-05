import React, { Component } from 'react';
import './Curtains.css';
import { connect } from 'react-redux';
import { RemoteService } from '../../../remote';
import mapStateToProps from '../../../deviceProps';

class Curtain extends Component {
  constructor(props) {
    super(props);
    this.state = {
      intensity: this.props.stateOrDevice.intensity,
      timeout: null,
    };

    this.setIntensity = this.setIntensity.bind(this);
  }

  // getters
  get turnedOn() {
    return this.props.stateOrDevice.on;
  }

  get intensity() {
    return this.props.stateOrDevice.intensity || 0;
  }

  onClickDevice = () => {
    const on = !this.turnedOn;
    if (this.props.tab === 'Devices') {
      this.props
        .saveDevice({ ...this.props.stateOrDevice, on })
        .catch((err) => console.error('curtains update error', err));
    } else {
      this.props.updateState(
        { id: this.props.stateOrDevice.id, on },
        this.props.stateOrDevice.kind,
      );
    }
  };

  setIntensity(intensity) {
    intensity *= 100;

    if (this.state.timeout) {
      clearTimeout(this.state.timeout);
    }

    this.setState({
      intensity,
      timeout: setTimeout(() => {
        this.saveIntensity();
        this.setState({
          intensity: this.state.intensity,
          timeout: null,
        });
      }, 100),
    });
  }

  saveIntensity = () => {
    const intensity = Math.round(this.state.intensity);
    if (this.props.tab === 'Devices') {
      this.props
        .saveDevice({ ...this.props.stateOrDevice, intensity })
        .catch((err) => console.error('curtain update error', err));
    } else {
      this.props.updateState(
        { id: this.props.stateOrDevice.id, intensity },
        this.props.stateOrDevice.kind,
      );
    }
  };

  helper = () => {
    if (this.props.device.intensity >= 90) {
      this.setIntensity(1);
      this.saveIntensity();
    } else {
      this.setIntensity(this.props.stateOrDevice.intensity / 100 + 0.1);
      this.saveIntensity();
    }
  };

  // /*this took me way too much more time than it should have*/

  handleChange = (a) => {
    this.setIntensity(a.target.value / 100);
    this.saveIntensity();
  };

  render() {
    return (
      <div className="container curtain-container">
        <div
          className="open-container"
          style={{
            height: `${(9 * this.props.stateOrDevice.intensity) / 100}rem`,
          }}
        />
        {' '}
        <span className="span-open">
          {Math.round(this.props.stateOrDevice.intensity)}
%
        </span>
        <input
          disabled={this.props.disabled}
          onChange={this.handleChange}
          value={this.props.stateOrDevice.intensity}
          className="slider"
          type="range"
          min="0"
          max="100"
        />
      </div>
    );
  }
}

const CurtainContainer = connect(mapStateToProps, RemoteService)(Curtain);
export default CurtainContainer;
