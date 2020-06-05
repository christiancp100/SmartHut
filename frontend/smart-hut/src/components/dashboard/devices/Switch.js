/**
 * Users can add on-off switches. A on-off switch can turn on (or off) lights.
 * If a light has an intensity level, when it gets switched back on, it gets the last available
 * intensity level that was set by the user (or 100% if no such level exists).
 * The user can change the state of a switch through the SmartHut interface.
 */

import React, { Component } from 'react';
import { Image } from 'semantic-ui-react';
import { connect } from 'react-redux';
import { BottomPanel, StyledDiv } from './styleComponents';
import { imageStyle, nameStyle, turnedOnStyle } from './SwitchStyle';
import { RemoteService } from '../../../remote';
import mapStateToProps from '../../../deviceProps';

class Switch extends Component {
  constructor(props) {
    super(props);
    this.iconOn = '/img/switchOn.svg';
    this.iconOff = '/img/switchOff.svg';
  }

  get turnedOn() {
    return this.props.device.on;
  }

  getIcon = () => (this.turnedOn ? this.iconOn : this.iconOff);

  onClickDevice = () => {
    const newOn = !this.turnedOn;
    const type = newOn ? 'ON' : 'OFF';
    this.props
      .switchOperate(this.props.id, type)
      .catch((err) => console.error('switch operate failed', err));
  };

  render() {
    return (
      <StyledDiv onClick={this.props.disabled ? () => {} : this.onClickDevice}>
        <Image src={this.getIcon()} style={imageStyle} />
        <span style={nameStyle}>Switch</span>

        <BottomPanel
          style={
            this.turnedOn
              ? { backgroundColor: '#505bda' }
              : { backgroundColor: '#1a2849' }
          }
        >
          <span style={turnedOnStyle}>{this.turnedOn ? 'ON' : 'OFF'}</span>
        </BottomPanel>
      </StyledDiv>
    );
  }
}

const SwitchContainer = connect(mapStateToProps, RemoteService)(Switch);
export default SwitchContainer;
