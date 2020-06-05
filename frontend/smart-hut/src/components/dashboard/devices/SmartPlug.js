/**
 A smart plug is a plug that has a boolean internal state, i.e., that can be turned on or off, either with the
 SmartHut interface or by a switch.
 The smart plug also stores the total energy consumed while the plug is active, in terms of kilowatt-hours 2(kWh) .
 The user can reset this value.
 * */
import React, { Component } from 'react';
import { Image } from 'semantic-ui-react';
import { connect } from 'react-redux';
import { BottomPanel, StyledDiv } from './styleComponents';
import {
  energyConsumedStyle,
  imageStyle,
  kwhStyle,
  nameStyle,
} from './SmartPlugStyle';
import { RemoteService } from '../../../remote';
import mapStateToProps from '../../../deviceProps';

class SmartPlug extends Component {
  constructor(props) {
    super(props);
    this.iconOn = '/img/smart-plug.svg';
    this.iconOff = '/img/smart-plug-off.svg';
  }

  get turnedOn() {
    return this.props.stateOrDevice.on;
  }

  get energyConsumed() {
    return (this.props.device.totalConsumption / 1000).toFixed(3);
  }

  onClickDevice = () => {
    const on = !this.turnedOn;
    if (this.props.tab === 'Devices') {
      this.props
        .saveDevice({ ...this.props.stateOrDevice, on })
        .catch((err) => console.error('smart plug update error', err));
    } else {
      this.props.updateState(
        { id: this.props.stateOrDevice.id, on },
        this.props.stateOrDevice.kind,
      );
    }
  };

  getIcon = () => (this.turnedOn ? this.iconOn : this.iconOff);

  render() {
    return (
      <StyledDiv onClick={this.props.disabled ? () => {} : this.onClickDevice}>
        <Image src={this.getIcon()} style={imageStyle} />
        <span style={nameStyle}>Smart Plug</span>

        <BottomPanel
          style={
            this.turnedOn
              ? { backgroundColor: '#505bda' }
              : { backgroundColor: '#1a2849' }
          }
        >
          <span style={energyConsumedStyle}>{this.energyConsumed}</span>
          <span style={kwhStyle}>KWh</span>
        </BottomPanel>
      </StyledDiv>
    );
  }
}

const SmartPlugContainer = connect(mapStateToProps, RemoteService)(SmartPlug);
export default SmartPlugContainer;
