// vim: set ts=2 sw=2 et tw=80:

import React, { Component } from 'react';
import {
 Segment, Card, Header, Icon,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import Device from './devices/Device';
import NewDevice from './devices/NewDevice';
import { RemoteService } from '../../remote';

class DevicePanel extends Component {
  constructor(props) {
    super(props);

    this.getDevices();
  }

  getDevices() {
    if (this.props.tab === 'Devices') {
      this.props
        .fetchDevices()
        .catch((err) => console.error('error fetching devices:', err));
    }
  }

  render() {
    return (
      <Card.Group style={{ paddingTop: '3rem' }}>
        {this.props.numbeOfRooms > 0 ? (
          <>
            {this.props.devices.map((e, i) => <Device key={i} tab={this.props.tab} id={e.id} />)}
            {!this.props.isActiveRoomHome ? (
              <Card style={{ height: '27em' }}>
                <Segment basic style={{ width: '100%', height: '100%' }}>
                  <NewDevice />
                </Segment>
              </Card>
            ) : null}
          </>
        ) : (
          <Segment placeholder>
            <Header icon>
              <Icon
                name="exclamation triangle"
                style={{ paddingBottom: '1rem' }}
              />
              Please create a room on the left, and then add devices to the
              same.
            </Header>
          </Segment>
        )}
      </Card.Group>
    );
  }
}

const mapStateToProps = (state, _) => ({
  get devices() {
    if (state.active.activeRoom === -1) {
      return Object.values(state.devices);
    }
      const deviceArray = [
        ...state.rooms[state.active.activeRoom].devices,
      ].sort();
      return deviceArray.map((id) => state.devices[id]);
  },
  get isActiveRoomHome() {
    return state.active.activeRoom === -1;
  },
  activeRoom: state.active.activeRoom,
  get numbeOfRooms() {
    return Object.keys(state.rooms).length;
  },
});
const DevicePanelContainer = connect(
  mapStateToProps,
  RemoteService,
)(DevicePanel);
export default DevicePanelContainer;
