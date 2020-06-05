const actions = {
  loginSuccess: (token) => ({
    type: 'LOGIN_UPDATE',
    login: {
      loggedIn: true,
      token,
    },
  }),
  logout: () => ({
    type: 'LOGOUT',
  }),
  userInfoUpdate: (userInfo) => ({
    type: 'USER_INFO_UPDATE',
    userInfo,
  }),
  roomSave: (room) => ({
    type: 'ROOM_SAVE',
    room,
  }),
  sceneSave: (scene) => ({
    type: 'SCENE_SAVE',
    scene,
  }),
  deviceSave: (device) => ({
    type: 'DEVICE_SAVE',
    device,
  }),
  hostDeviceSave: (hostId, device) => ({
    type: 'HOST_DEVICE_SAVE',
    hostId,
    device,
  }),
  triggerSave: (automation) => ({
    type: 'TRIGGER_SAVE',
    automation,
  }),

  scenePrioritySave: (automation) => ({
    type: 'SCENE_PRIORITY_SAVE',
    automation,
  }),

  automationSave: (automation) => ({
    type: 'AUTOMATION_SAVE',
    automation,
  }),
  automationsUpdate: (automations) => ({
    type: 'AUTOMATION_UPDATE',
    automations,
  }),
  stateSave: (sceneState) => ({
    type: 'STATE_SAVE',
    sceneState,
  }),
  statesUpdate: (sceneId, sceneStates) => ({
    type: 'STATES_UPDATE',
    sceneId,
    sceneStates,
  }),
  devicesUpdate: (roomId, devices, partial = false) => ({
    type: 'DEVICES_UPDATE',
    roomId,
    devices,
    partial,
  }),
  hostDevicesUpdate: (hostId, devices, partial = false) => ({
    type: 'HOST_DEVICES_UPDATE',
    hostId,
    partial,
    devices,
  }),
  stateDelete: (stateId) => ({
    type: 'STATE_DELETE',
    stateId,
  }),
  deviceOperationUpdate: (devices) => ({
    type: 'DEVICES_UPDATE',
    devices,
    partial: true,
  }),
  roomsUpdate: (rooms) => ({
    type: 'ROOMS_UPDATE',
    rooms,
  }),
  hostRoomsUpdate: (hostId, rooms) => ({
    type: 'HOST_ROOMS_UPDATE',
    hostId,
    rooms,
  }),
  roomDelete: (roomId) => ({
    type: 'ROOM_DELETE',
    roomId,
  }),
  automationDelete: (id) => ({
    type: 'AUTOMATION_DELETE',
    id,
  }),
  sceneDelete: (sceneId) => ({
    type: 'SCENE_DELETE',
    sceneId,
  }),
  scenesUpdate: (scenes) => ({
    type: 'SCENES_UPDATE',
    scenes,
  }),
  hostScenesUpdate: (hostId, scenes) => ({
    type: 'HOST_SCENES_UPDATE',
    hostId,
    scenes,
  }),
  deviceDelete: (deviceId) => ({
    type: 'DEVICE_DELETE',
    deviceId,
  }),
  hostsUpdate: (hosts) => ({
    type: 'HG_UPDATE',
    key: 'hosts',
    value: hosts,
  }),
  guestsUpdate: (hosts) => ({
    type: 'HG_UPDATE',
    key: 'guests',
    value: hosts,
  }),
  getHostDevices: (host) => ({
    type: 'GET_HOST_DEVICES',
    host,
  }),
  guestUpdate: (guests) => ({
    type: 'HG_UPDATE',
    key: 'guests',
    value: guests,
  }),
};

export const appActions = {
  // -1 for home view
  setActiveRoom: (activeRoom = -1) => ({
    type: 'SET_ACTIVE',
    key: 'activeRoom',
    value: activeRoom,
  }),
  setActiveTab: (activeTab) => ({
    type: 'SET_ACTIVE',
    key: 'activeTab',
    value: activeTab,
  }),
  setActiveScene: (activeScene = -1) => ({
    type: 'SET_ACTIVE',
    key: 'activeScene',
    value: activeScene,
  }),
  setActiveHost: (activeHost = -1) => ({
    type: 'SET_ACTIVE',
    key: 'activeHost',
    value: activeHost,
  }),
};

export default actions;
