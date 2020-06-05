function getStateOrDevice(state, ownProps) {
  switch (state.active.activeTab) {
    case 'Devices':
      return state.devices[ownProps.id];
    case 'Scenes':
      return state.sceneStates[ownProps.id];
    case 'Hosts':
      return state.hostDevices[ownProps.hostId][ownProps.id];
    default:
      throw new Error(
        `stateOrDevice has no value in tab "${state.active.activeTab}"`,
      );
  }
}

function getDevice(state, ownProps) {
  switch (state.active.activeTab) {
    case 'Scenes':
      return state.devices[getStateOrDevice(state, ownProps).deviceId];
    case 'Devices':
    case 'Hosts':
      return getStateOrDevice(state, ownProps);
    default:
      throw new Error(`device has no value in tab "${state.active.activeTab}"`);
  }
}

function getRoomName(state, ownProps) {
  switch (state.active.activeTab) {
    case 'Scenes':
    case 'Devices':
      return (state.rooms[getDevice(state, ownProps).roomId] || {}).name;
    case 'Hosts':
      const hostRooms = state.hostRooms[ownProps.hostId];
      if (!hostRooms) return '';
      const room = hostRooms[getDevice(state, ownProps).roomId];
      if (!room) return '';
      return room.name;
    default:
      throw new Error(
        `room name has no value in tab "${state.active.activeTab}"`,
      );
  }
}

export default function mapStateToProps(state, ownProps) {
  return {
    activeHost: state.active.activeHost,
    get stateOrDevice() {
      return getStateOrDevice(state, ownProps);
    },
    get device() {
      return getDevice(state, ownProps);
    },
    get roomName() {
      return getRoomName(state, ownProps);
    },
    get type() {
      return getDevice(state, ownProps).kind;
    },
    get disabled() {
      return (
        ownProps.tab === 'Hosts'
        && ['dimmableLight', 'light'].indexOf(getDevice(state, ownProps).kind)
          === -1
      );
    },
  };
}
