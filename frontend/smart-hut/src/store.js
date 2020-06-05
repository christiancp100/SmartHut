import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import update from 'immutability-helper';
import reduxWebSocket, { connect } from '@giantmachines/redux-websocket';
import { socketURL } from './endpoint';

function reducer(previousState, action) {
  let newState; let
change;

  const createOrUpdateRoom = (room) => {
    if (!newState.rooms[room.id]) {
      newState = update(newState, {
        rooms: { [room.id]: { $set: { ...room, devices: new Set() } } },
      });
    } else {
      newState = update(newState, {
        rooms: {
          [room.id]: {
            name: { $set: room.name },
            image: { $set: room.image },
            icon: { $set: room.icon },
          },
        },
      });
    }

    if (newState.pendingJoins.rooms[room.id]) {
      newState = update(newState, {
        pendingJoins: { rooms: { $unset: [room.id] } },
        rooms: {
          [room.id]: {
            devices: {
              $add: [...newState.pendingJoins.rooms[room.id]],
            },
          },
        },
      });
    }
  };

  const createOrUpdateScene = (scene) => {
    if (!newState.scenes[scene.id]) {
      newState = update(newState, {
        scenes: { [scene.id]: { $set: { ...scene, sceneStates: new Set() } } },
      });
    } else {
      newState = update(newState, {
        scenes: {
          [scene.id]: {
            name: { $set: scene.name },
            icon: { $set: scene.icon },
            guestAccessEnabled: { $set: scene.guestAccessEnabled },
          },
        },
      });
    }

    if (newState.pendingJoins.scenes[scene.id]) {
      newState = update(newState, {
        pendingJoins: { scenes: { $unset: [scene.id] } },
        scenes: {
          [scene.id]: {
            sceneStates: {
              $add: [...newState.pendingJoins.scenes[scene.id]],
            },
          },
        },
      });
    }
  };

  const updateDeviceProps = (device) => {
    // In some updates the information regarding a device is incomplete
    // due to a fault in the type system and JPA repository management
    // in the backend. Therefore to solve this avoid to delete existing
    // attributes of this device in the previous state, but just update
    // the new ones.
    change.devices[device.id] = {};
    for (const key in device) {
      change.devices[device.id][key] = { $set: device[key] };
    }
  };

  const updateSceneStateProps = (state) => {
    change.sceneStates[state.id] = {};
    for (const key in state) {
      change.sceneStates[state.id][key] = { $set: state[key] };
    }
  };

  switch (action.type) {
    case 'LOGIN_UPDATE':
      newState = update(previousState, { login: { $set: action.login } });
      break;
    case 'USER_INFO_UPDATE':
      newState = update(previousState, {
        userInfo: { $set: action.userInfo },
      });
      break;
    case 'ROOMS_UPDATE':
      newState = previousState;
      for (const room of action.rooms) {
        createOrUpdateRoom(room);
      }
      break;
    case 'HOST_ROOMS_UPDATE':
      change = {
        hostRooms: {
          [action.hostId]: { $set: {} },
        },
      };
      const rooms = change.hostRooms[action.hostId].$set;

      for (const room of action.rooms) {
        rooms[room.id] = room;
      }

      newState = update(previousState, change);
      break;
    case 'SCENES_UPDATE':
      newState = previousState;
      for (const scene of action.scenes) {
        createOrUpdateScene(scene);
      }
      break;
    case 'HOST_SCENES_UPDATE':
      change = {
        hostScenes: {
          [action.hostId]: { $set: action.scenes }, // stored as array
        },
      };

      newState = update(previousState, change);
      break;
    case 'STATES_UPDATE':
      // console.log(action.sceneStates);
      change = null;

      // if scene is given, delete all sceneStates in that scene
      // and remove any join between that scene and deleted
      // sceneStates
      change = {
        scenes: {
          [action.sceneId]: { sceneStates: { $set: new Set() } },
        },
        sceneStates: { $unset: [] },
      };

      const scene = previousState.scenes[action.sceneId];
      for (const stateId of scene.sceneStates) {
        change.sceneStates.$unset.push(stateId);
      }

      newState = update(previousState, change);

      change = {
        sceneStates: {},
        scenes: {},
        pendingJoins: { scenes: {} },
      };

      for (const sceneState of action.sceneStates) {
        if (!newState.sceneStates[sceneState.id]) {
          change.sceneStates[sceneState.id] = {
            $set: sceneState,
          };
        } else {
          updateSceneStateProps(sceneState);
        }

        if (sceneState.sceneId in newState.scenes) {
          change.scenes[sceneState.sceneId] = change.scenes[sceneState.sceneId] || {};
          change.scenes[sceneState.sceneId].sceneStates = change.scenes[sceneState.sceneId].sceneStates || {};
          const { sceneStates } = change.scenes[sceneState.sceneId];
          sceneStates.$add = sceneStates.$add || [];
          sceneStates.$add.push(sceneState.id);
        } else {
          // room does not exist yet, so add to the list of pending
          // joins

          if (!change.pendingJoins.scenes[sceneState.sceneId]) {
            change.pendingJoins.scenes[sceneState.sceneId] = {
              $set: new Set([sceneState.id]),
            };
          } else {
            change.pendingJoins.scenes[sceneState.sceneId].$set.add(
              sceneState.id,
            );
          }
        }
      }

      newState = update(newState, change);

      break;
    case 'DEVICES_UPDATE':
      change = null;

      // if room is given, delete all devices in that room
      // and remove any join between that room and deleted
      // devices
      if (action.roomId) {
        change = {
          rooms: {
            [action.roomId]: { devices: { $set: new Set() } },
          },
          devices: { $unset: [] },
        };

        const room = newState.rooms[action.roomId];
        for (const deviceId of room.devices) {
          change.devices.$unset.push(deviceId);
        }
      } else if (action.partial) {
        // if the update is partial and caused by an operation on an input
        // device (like /switch/operate), iteratively remove deleted
        // devices and their join with their corresponding room.
        change = {
          devices: { $unset: [] },
          rooms: {},
        };

        for (const device of action.devices) {
          if (!previousState.devices[device.id]) continue;
          change.devices.$unset.push(device.id);
          const { roomId } = previousState.devices[device.id];

          if (roomId in previousState.rooms) {
            change.rooms[roomId] = change.rooms[roomId] || {
              devices: { $remove: [] },
            };
            change.rooms[roomId].devices.$remove.push(device.id);
          }
        }
      } else {
        // otherwise, just delete all devices and all joins
        // between rooms and devices
        change = {
          devices: { $set: {} },
          rooms: {},
        };

        for (const room of Object.values(previousState.rooms)) {
          if (change.rooms[room.id]) {
            change.rooms[room.id].devices = { $set: new Set() };
          }
        }
      }

      newState = update(previousState, change);

      change = {
        devices: {},
        rooms: {},
        pendingJoins: { rooms: {} },
      };
      for (const device of action.devices) {
        if (!newState.devices[device.id]) {
          change.devices[device.id] = { $set: device };
        } else {
          updateDeviceProps(device);
        }

        if (device.roomId in newState.rooms) {
          change.rooms[device.roomId] = change.rooms[device.roomId] || {};
          change.rooms[device.roomId].devices = change.rooms[device.roomId].devices || {};
          const { devices } = change.rooms[device.roomId];
          devices.$add = devices.$add || [];
          devices.$add.push(device.id);
        } else {
          // room does not exist yet, so add to the list of pending
          // joins

          if (!change.pendingJoins.rooms[device.roomId]) {
            change.pendingJoins.rooms[device.roomId] = {
              $set: new Set([device.id]),
            };
          } else {
            change.pendingJoins.rooms[device.roomId].$set.add(device.id);
          }
        }
      }

      newState = update(newState, change);
      break;
    case 'HOST_DEVICES_UPDATE':
      newState = action.partial
        ? previousState
        : update(previousState, {
            hostDevices: { [action.hostId]: { $set: {} } },
          });
      newState.hostDevices[action.hostId] = newState.hostDevices[action.hostId] || {};
      change = {
        hostDevices: {
          [action.hostId]: {},
        },
      };
      const deviceMap = change.hostDevices[action.hostId];

      for (const device of action.devices) {
        deviceMap[device.id] = { $set: device };
      }

      newState = update(newState, change);
      break;
    case 'AUTOMATION_UPDATE':
      const automations = {};
      for (const automation of action.automations) {
        automations[automation.id] = automation;
      }

      change = {
        automations: { $set: automations },
      };
      newState = update(previousState, change);
      break;
    case 'ROOM_SAVE':
      newState = previousState;
      createOrUpdateRoom(action.room);
      break;
    case 'SCENE_SAVE':
      newState = previousState;
      createOrUpdateScene(action.scene);
      break;
    case 'DEVICE_SAVE':
      change = {
        devices: { [action.device.id]: { $set: action.device } },
      };

      if (previousState.rooms[action.device.roomId]) {
        change.rooms = {
          [action.device.roomId]: {
            devices: {
              $add: [action.device.id],
            },
          },
        };
      } else {
        change.pendingJoins = {
          rooms: {
            [action.device.roomId]: {
              $add: [action.device.id],
            },
          },
        };
      }
      newState = update(previousState, change);
      break;
    case 'HOST_DEVICE_SAVE':
      change = {
        hostDevices: {
          [action.hostId]: {
            [action.device.id]: {
              $set: action.device,
            },
          },
        },
      };
      newState = update(previousState, change);
      break;
    case 'HOST_DEVICES_DELETE':
      change = {
        hostDevices: {
          [action.hostId]: {
            $unset: [action.deviceIds],
          },
        },
      };
      newState = update(previousState, change);
      break;

    case 'AUTOMATION_SAVE':
      change = {
        automations: {
          [action.automation.id]: { $set: action.automation },
        },
      };

      newState = update(previousState, change);

      break;

    case 'STATE_SAVE':
      change = {
        sceneStates: {
          [action.sceneState.id]: { $set: action.sceneState },
        },
      };

      if (previousState.scenes[action.sceneState.sceneId]) {
        change.scenes = {
          [action.sceneState.sceneId]: {
            sceneStates: {
              $add: [action.sceneState.id],
            },
          },
        };
      } else {
        change.pendingJoins = {
          scenes: {
            [action.sceneState.sceneId]: {
              $add: [action.sceneState.id],
            },
          },
        };
      }
      newState = update(previousState, change);
      break;
    case 'ROOM_DELETE':
      if (!(action.roomId in previousState.rooms)) {
        console.warn(`Room to delete ${action.roomId} does not exist`);
        break;
      }

      // This update does not ensure the consistent update of switchId/dimmerId properties
      // on output devices connected to an input device in this room. Please manually request
      // all devices again if consistent update is desired
      change = { devices: { $unset: [] } };

      for (const id of previousState.rooms[action.roomId].devices) {
        change.devices.$unset.push(id);
      }

      change.rooms = { $unset: [action.roomId] };

      if (previousState.active.activeRoom === action.roomId) {
        change.active = { activeRoom: { $set: -1 } };
      }

      newState = update(previousState, change);
      break;

    case 'AUTOMATION_DELETE':
      change = {
        automations: { $unset: [action.id] },
      };

      newState = update(previousState, change);
      break;
    case 'SCENE_DELETE':
      if (!(action.sceneId in previousState.scenes)) {
        console.warn(`Scene to delete ${action.sceneId} does not exist`);
        break;
      }

      // This update does not ensure the consistent update of switchId/dimmerId properties
      // on output devices connected to an input device in this room. Please manually request
      // all devices again if consistent update is desired
      change = { sceneStates: { $unset: [] } };

      for (const id of previousState.scenes[action.sceneId].sceneStates) {
        change.sceneStates.$unset.push(id);
      }

      change.scenes = { $unset: [action.sceneId] };

      if (previousState.active.activeScene === action.sceneId) {
        change.active = { activeScene: { $set: -1 } };
      }

      newState = update(previousState, change);
      break;
    case 'STATE_DELETE':
      if (!(action.stateId in previousState.sceneStates)) {
        console.warn(`State to delete ${action.stateId} does not exist`);
        break;
      }

      change = {
        sceneStates: { $unset: [action.stateId] },
      };

      if (
        previousState.scenes[previousState.sceneStates[action.stateId].sceneId]
      ) {
        change.scenes = {
          [previousState.sceneStates[action.stateId].sceneId]: {
            sceneStates: { $remove: [action.stateId] },
          },
        };
      }

      newState = update(previousState, change);
      break;

    case 'DEVICE_DELETE':
      if (!(action.deviceId in previousState.devices)) {
        console.warn(`Device to delete ${action.deviceId} does not exist`);
        break;
      }

      change = {
        devices: { $unset: [action.deviceId] },
      };

      if (previousState.rooms[previousState.devices[action.deviceId].roomId]) {
        change.rooms = {
          [previousState.devices[action.deviceId].roomId]: {
            devices: { $remove: [action.deviceId] },
          },
        };
      }

      newState = update(previousState, change);
      break;
    case 'LOGOUT':
      newState = update(initState, {});
      break;
    case 'SET_ACTIVE':
      newState = update(previousState, {
        active: {
          [action.key]: {
            $set: action.value,
          },
        },
      });
      break;
    case 'REDUX_WEBSOCKET::MESSAGE':
      const allDevices = JSON.parse(action.payload.message);
      const devices = allDevices.filter(
        (d) => (d.fromHostId === null || d.fromHostId === undefined) && !d.deleted,
      );
      const hostDevicesMapByHostId = allDevices
        .filter((d) => d.fromHostId)
        .reduce((a, e) => {
          const hostId = e.fromHostId;
          // delete e.fromHostId;
          a[hostId] = a[hostId] || { updated: [], deletedIds: [] };
          if (e.deleted) {
            a[hostId].deletedIds.push(e.id);
          } else {
            a[hostId].updated.push(e);
          }
          return a;
        }, {});
      newState = reducer(previousState, {
        type: 'DEVICES_UPDATE',
        partial: true,
        devices,
      });
      for (const hostId in hostDevicesMapByHostId) {
        if (hostDevicesMapByHostId[hostId].updated.length > 0) {
 newState = reducer(newState, {
            type: 'HOST_DEVICES_UPDATE',
            devices: hostDevicesMapByHostId[hostId].updated,
            partial: true,
            hostId,
          });
}
        if (hostDevicesMapByHostId[hostId].deletedIds.length > 0) {
          newState = reducer(newState, {
            type: 'HOST_DEVICES_DELETE',
            deviceIds: hostDevicesMapByHostId[hostId].deletedIds,
            partial: true,
            hostId,
          });
        }
      }
      break;
    case 'HG_UPDATE':
      newState = update(previousState, {
        [action.key]: { $set: action.value },
      });
      break;
    default:
      console.warn(`Action type ${action.type} unknown`, action);
      return previousState;
  }
  return newState;
}

const initState = {
  pendingJoins: {
    rooms: {},
    scenes: {},
    automations: {},
  },
  active: {
    activeRoom: -1,
    activeTab: 'Devices',
    activeScene: -1,
    activeAutomation: -1,
    activeHost: -1,
  },
  login: {
    loggedIn: false,
    token: null,
  },
  userInfo: null,
  /** @type {[integer]Room} */
  rooms: {},
  /** @type {[integer]Scene} */
  scenes: {},
  hostScenes: {},
  /** @type {[integer]Automation} */
  automations: {},
  /** @type {[integer]Device} */
  devices: {},
  /** @type {[integer]SceneState} */
  sceneStates: {},
  /** @type {User[]} */
  guests: [],
  /** @type {User[]} */
  hosts: [],
  /** @type {[integer]Device} */
  hostDevices: {},
  /** @type {[integer]Eoom} */
  hostRooms: {},
};

function createSmartHutStore() {
  const token = localStorage.getItem('token');
  const exp = localStorage.getItem('exp');

  const initialState = update(initState, {
    login: {
      token: { $set: token },
      loggedIn: { $set: !!(token && exp > new Date().getTime()) },
    },
  });

  if (!initialState.login.loggedIn) {
    localStorage.removeItem('token');
    localStorage.removeItem('exp');
    initialState.login.token = null;
  }

  const store = createStore(
    reducer,
    initialState,
    compose(applyMiddleware(thunk), applyMiddleware(reduxWebSocket())),
  );
  if (initialState.login.loggedIn) {
    store.dispatch(connect(socketURL(token)));
  }
  return store;
}

const smartHutStore = createSmartHutStore();
export default smartHutStore;
