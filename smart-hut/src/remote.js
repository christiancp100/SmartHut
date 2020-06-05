import axios from 'axios';
import { connect, disconnect } from '@giantmachines/redux-websocket';
import smartHutStore from './store';
import actions from './storeActions';
import { endpointURL, socketURL } from './endpoint';

/**
 * An object returned by promise rejections in remoteservice
 * @typedef {Error} RemoteError
 * @property {String[]} messages a list of user-friendly error messages to show;
 */
class RemoteError extends Error {
  messages;

  constructor(messages) {
    super(
      messages && Array.isArray(messages)
        ? messages.join(' - ')
        : JSON.stringify(messages, null, 2),
    );
    this.messages = messages;
  }
}

const Endpoint = {
  axiosInstance: axios.create({
    baseURL: endpointURL(),
    validateStatus: (status) => status >= 200 && status < 300,
  }),

  /**
   * Returns token for current session, null if logged out
   * @returns {String|null} the token
   */
  get token() {
    return smartHutStore.getState().login.token;
  },

  /**
   * Performs an authenticated request
   * @param {get|post|put|delete} the desired method
   * @param {String} route the desired route (e.g. "/rooms/1/devices")
   * @param {[String]String} query query ('?') parameters (no params by default)
   * @param {any} body the JSON request body
   */
  send: (method, route, query = {}, body = null) => {
    if (!Endpoint.token) {
      throw new Error('No token while performing authenticated request');
    }

    return Endpoint.axiosInstance(route, {
      method,
      params: query,
      data: ['put', 'post'].indexOf(method) !== -1 ? body : null,
      headers: {
        Authorization: `Bearer ${Endpoint.token}`,
      },
    }).then((res) => {
      if (!res.data && method !== 'delete') {
        console.error('Response body is empty');
        return null;
      }
        return res;
    });
  },

  /**
   * Performs a non-authenticated post and put request for registration, reset-password
   * @param {post} the desired method
   * @param {String} route the desired route (e.g. "/rooms/1/devices")
   * @param {[String]String} query query ('?') parameters (no params by default)
   * @param {any} body the JSON request body
   */
  sendNA: (method, route, query = {}, body = null) => Endpoint.axiosInstance(route, {
      method,
      params: query,
      data: ['put', 'post'].indexOf(method) !== -1 ? body : null,
    }).then((res) => {
      if (!res.data) {
        console.error('Response body is empty');
        return null;
      }
        return res;
    }),

  /**
   * Performs login
   * @param {String} usernameOrEmail
   * @param {String} password
   * @returns {Promise<String, *>} promise that resolves to the token string
   *  and rejects to the axios error.
   */
  login: (usernameOrEmail, password) => Endpoint.axiosInstance
      .post('/auth/login', {
        usernameOrEmail,
        password,
      })
      .then((res) => {
        localStorage.setItem('token', res.data.jwttoken);
        localStorage.setItem('exp', new Date().getTime() + 5 * 60 * 60 * 1000);
        return res.data.jwttoken;
      }),

  /**
   * Returns an immediately resolved promise for the socket logouts
   * @return {Promise<Undefined, _>} An always-resolved promise
   */
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('exp');
    return Promise.resolve(void 0);
  },

  /**
   * Performs an authenticated GET request
   * @param {String} route the desired route (e.g. "/rooms/1/devices")
   * @param {[String]String} query query ('?') parameters (no params by default)
   * @returns {Promise<*, *>} The Axios-generated promise
   */
  get(route, query = {}) {
    return this.send('get', route, query);
  },

  /**
   * Performs an authenticated POST request
   * @param {String} route the desired route (e.g. "/rooms/1/devices")
   * @param {[String]String} query query ('?') parameters (no params by default)
   * @param {any} body the JSON request body
   * @returns {Promise<*, *>} The Axios-generated promise
   */
  post(route, query, body) {
    return this.send('post', route, query, body);
  },

  /**
   * Performs a non-authenticated POST request
   * @param {String} route the desired route (e.g. "/rooms/1/devices")
   * @param {[String]String} query query ('?') parameters (no params by default)
   * @param {any} body the JSON request body
   * @returns {Promise<*, *>} The Axios-generated promise
   */
  postNA(route, query, body) {
    return this.sendNA('post', route, query, body);
  },

  /**
   * Performs an authenticated PUT request
   * @param {String} route the desired route (e.g. "/rooms/1/devices")
   * @param {[String]String} query query ('?') parameters (no params by default)
   * @param {any} body the JSON request body
   * @returns {Promise<*, *>} The Axios-generated promise
   */
  put(route, query = {}, body = {}) {
    return this.send('put', route, query, body);
  },

  /**
   * Performs a non-authenticated PUT request
   * @param {String} route the desired route (e.g. "/rooms/1/devices")
   * @param {[String]String} query query ('?') parameters (no params by default)
   * @param {any} body the JSON request body
   * @returns {Promise<*, *>} The Axios-generated promise
   */
  putNA(route, query = {}, body = {}) {
    return this.sendNA('put', route, query, body);
  },

  /**
   * Performs an authenticated DELETE request
   * @param {get|post|put|delete} the desired method
   * @param {String} route the desired route (e.g. "/rooms/1/devices")
   * @param {[String]String} query query ('?') parameters (no params by default)
   * @returns {Promise<*, *>} The Axios-generated promise
   */
  delete(route, query = {}) {
    return this.send('delete', route, query);
  },
};

/**
 * Given an error response, returns an array of user
 * friendly messages to display to the user
 * @param {*} err the Axios error reponse object
 * @returns {RemoteError} user friendly error messages
 */
function parseValidationErrors(err) {
  if (
    err.response
    && err.response.status === 400
    && err.response.data
    && Array.isArray(err.response.data.errors)
  ) {
    throw new RemoteError([
      ...new Set(err.response.data.errors.map((e) => e.defaultMessage)),
    ]);
  } else {
    console.warn('Non validation error', err);
    throw new RemoteError(['Network error']);
  }
}

export const RemoteService = {
  /**
   * Performs login
   * @param {String} usernameOrEmail
   * @param {String} password
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  login: (usernameOrEmail, password) => (dispatch) => Endpoint.login(usernameOrEmail, password)
        .then((token) => {
          dispatch(actions.loginSuccess(token));
          dispatch(connect(socketURL(token)));
        })
        .catch((err) => {
          console.warn('login error', err);
          throw new RemoteError([
            err.response && err.response.status === 401
              ? 'Wrong credentials'
              : 'An error occurred while logging in',
          ]);
        }),

  /**
   * Performs logout
   */
  logout: () => (dispatch) => Endpoint.logout().then(() => {
        dispatch(disconnect());
        dispatch(actions.logout());
      }),

  /**
   * Fetches user information via REST calls, if it is logged in
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  userPermissions: (data) => (dispatch) => Endpoint.put('/user/permissions', {}, data).catch((err) => {
        console.warn('Fetch user info error', err);
        throw new RemoteError(['Network error']);
      }),

  /**
   * Fetches user information via REST calls, if it is logged in
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  fetchUserInfo: () => (dispatch) => Endpoint.get('/auth/profile')
        .then((res) => void dispatch(actions.userInfoUpdate(res.data)))
        .catch((err) => {
          console.warn('Fetch user info error', err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Fetches all rooms that belong to this user. This call does not
   * populate the devices attribute in rooms.
   * @param {Number|null} hostId the user id of the host we need to fetch the rooms from.
   *  Null if we need to fetch our own rooms.
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  fetchAllRooms: (hostId = null) => (dispatch) => Endpoint.get('/room', hostId ? { hostId } : null)
        .then(
          (res) => void dispatch(
              hostId
                ? actions.hostRoomsUpdate(hostId, res.data)
                : actions.roomsUpdate(res.data),
            ),
        )
        .catch((err) => {
          console.error('Fetch all rooms error', err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Fetches all scenes that belong to this user. This call does not
   * populate the devices attribute in scenes.
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  fetchAllScenes: (hostId = null) => (dispatch) => Endpoint.get('/scene', hostId ? { hostId } : {})
        .then(
          (res) => void dispatch(
              !hostId
                ? actions.scenesUpdate(res.data)
                : actions.hostScenesUpdate(hostId, res.data),
            ),
        )
        .catch((err) => {
          console.error('Fetch all scenes error', err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Fetches all devices in a particular room, or fetches all devices.
   * This also updates the devices attribute on values in the map rooms.
   * @param {Number|null} roomId the rsoom to which fetch devices
   *   from, null to fetch from all rooms
   * @param {Number|null} hostId the user id of the owner of the devices to get
   *   (can be used for host view)
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  fetchDevices: (roomId = null, hostId = null) => (dispatch) => Endpoint.get(
        roomId ? `/room/${roomId}/device` : '/device',
        hostId ? { hostId } : null,
      )
        .then(
          (res) => void dispatch(
              !hostId
                ? actions.devicesUpdate(roomId, res.data, hostId)
                : actions.hostDevicesUpdate(hostId, res.data),
            ),
        )
        .catch((err) => {
          console.error(`Fetch devices roomId=${roomId} error`, err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Fetches all the automations
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  fetchAutomations: () => (dispatch) => Endpoint.get('/automation/')
        .then((res) => void dispatch(actions.automationsUpdate(res.data)))
        .catch((err) => {
          console.error('Fetch automations error', err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Fetches all devices in a particular scene, or fetches all devices.
   * This also updates the devices attribute on values in the map scenes.
   * @param {Number} sceneId the scene to which fetch devices
   *   from, null to fetch from all scenes
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  fetchStates: (sceneId) => (dispatch) => Endpoint.get(`/scene/${sceneId}/states`)
        .then((res) => void dispatch(actions.statesUpdate(sceneId, res.data)))
        .catch((err) => {
          console.error(`Fetch devices sceneId=${sceneId} error`, err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Fetches all hosts of a particular user.
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  fetchHosts: () => (dispatch) => Endpoint.get('/user/hosts')
        .then((res) => void dispatch(actions.hostsUpdate(res.data)))
        .catch((err) => {
          console.error('Fetch hosts error', err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Fetches all guests of a particular user.
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  fetchGuests: () => (dispatch) => Endpoint.get('/user/guests')
        .then((res) => void dispatch(actions.guestsUpdate(res.data)))
        .catch((err) => {
          console.error('Fetch guests error', err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Adds the current user as a guest to another user
   * identified through a user id.
   * @param {Number[]} userId the users to add.
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  updateGuests: (userIds) => (dispatch) => Endpoint.put('/user/guests', {}, { ids: userIds })
        .then((res) => void dispatch(actions.guestsUpdate(res.data)))
        .catch((err) => {
          console.error('Guest save error', err);
          throw new RemoteError(['Network Error']);
        }),

  /**
   * Creates/Updates a room with the given data
   * @param {String} data.name the room's name,
   * @param {String} data.icon the room's icon name in SemanticUI icons
   * @param {String} data.image ths room's image, as base64
   * @param {Number|null} roomId the room's id if update, null for creation
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  saveRoom: (data, roomId = null) => (dispatch) => {
      data = {
        name: data.name,
        icon: data.icon,
        image: data.image,
      };

      return (roomId
        ? Endpoint.put(`/room/${roomId}`, {}, data)
        : Endpoint.post('/room', {}, data)
      )
        .then((res) => void dispatch(actions.roomSave(res.data)))
        .catch(parseValidationErrors);
    },

  /**
   * Creates/Updates a scene with the given data
   * @param {String} data.name the scene's name,
   * @param {Number|null} sceneId the scene's id if update, null for creation
   * @param {Number|null} copyFrom the id of the scene from which the states must be copied from.
   *  (ignored for updates)
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  saveScene: (data, sceneId = null, copyFrom = null) => (dispatch) => {
      copyFrom = sceneId === null ? copyFrom : null;
      data = {
        name: data.name,
        icon: data.icon,
        guestAccessEnabled: sceneId ? data.guestAccessEnabled : false,
      };

      return (sceneId
        ? Endpoint.put(`/scene/${sceneId}`, {}, data)
        : Endpoint.post('/scene', {}, data)
      )
        .then(async (res) => {
          let states = [];

          if (copyFrom) {
            const sceneId = res.data.id;
            try {
              const res = await Endpoint.post(
                `/scene/${sceneId}/copyFrom/${copyFrom}`,
              );
              states = res.data;
            } catch (e) {
              console.warn(`Error in state cloning from scene ${copyFrom}`, e);
              throw new RemoteError(['Network error']);
            }
          }

          dispatch(actions.sceneSave(res.data));
          if (states.length > 0) {
            dispatch(actions.statesUpdate(sceneId, states));
          }
        })
        .catch(parseValidationErrors);
    },

  updateState: (data, type) => (dispatch) => {
      let url;
      if (data.on !== undefined) {
        url = '/switchableState';
      } else {
        url = '/dimmableState';
      }

      return Endpoint.put(url, {}, data)
        .then((res) => {
          dispatch(actions.stateSave(res.data));
          return res.data;
        })
        .catch((err) => {
          console.warn('Update device: ', data, 'error: ', err);
          throw new RemoteError(['Network error']);
        });
    },

  deleteState: (id, type) => (dispatch) => {
      let url;
      if (type === 'dimmableState') {
        url = '/dimmableState';
      } else {
        url = '/switchableState';
      }
      return Endpoint.delete(`${url}/${id}`)
        .then((_) => dispatch(actions.stateDelete(id)))
        .catch((err) => {
          console.warn('state delete error', err);
          throw new RemoteError(['Network error']);
        });
    },

  sceneApply: (id, hostId = null) => (dispatch) => {
      let url = `/scene/${id}/apply`;
      if (hostId) {
        url = `${url}?hostId=${hostId}`;
      }

      return Endpoint.post(url)
        .then((res) => dispatch(
            hostId
              ? actions.hostDevicesUpdate(hostId, res.data, true)
              : actions.deviceOperationUpdate(res.data),
          ))
        .catch((err) => {
          console.warn('scene apply error', err);
          throw new RemoteError(['Network error']);
        });
    },

  /**
   * Creates/Updates a device with the given data. If
   * data.id is truthy, then a update call is performed,
   * otherwise a create call is performed. The update URL
   * is computed based on data.kind when data.flowType =
   * 'OUTPUT', otherwise the PUT "/device" endpoint
   * is used for updates and the POST "/<device.kind>"
   * endpoints are used for creation.
   * @param {Device} data the device to update.
   * @returns {Promise<Device, RemoteError>} promise that resolves to the saved device and rejects
   *  with user-fiendly errors as a RemoteError
   */
  saveDevice: (data, hostId = null) => (dispatch) => {
      let url = '/device';
      if ((data.id && data.flowType === 'OUTPUT') || !data.id) {
        url = `/${data.kind}`;
      }

      return Endpoint[data.id ? 'put' : 'post'](
        url,
        hostId ? { hostId } : {},
        data,
      )
        .then((res) => {
          dispatch(
            hostId
              ? actions.hostDeviceSave(hostId, res.data)
              : actions.deviceSave(res.data),
          );
          return res.data;
        })
        .catch((err) => {
          console.warn('Update device: ', data, 'error: ', err);
          throw new RemoteError(['Network error']);
        });
    },

  fastUpdateAutomation: (automation) => (dispatch) => Endpoint.put('/automation/fast', {}, automation)
        .then((res) => dispatch(actions.automationSave(res.data)))
        .catch((err) => {
          console.warn('Update automation: ', automation, 'error: ', err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Creates/Updates an automation with the given data. If
   * data.id is truthy, then a update call is performed,
   * otherwise a create call is performed.
   * @param {Automation} data the automation to update.
   * @returns {Promise<Device, RemoteError>} promise that resolves to the saved device and rejects
   *  with user-fiendly errors as a RemoteError
   */
  saveAutomation: (data) => {
    const {
 automation, triggerList, order, conditionList,
} = data;
    automation.triggers = [];
    automation.scenes = [];
    automation.condition = [];
    return (dispatch) => {
      const urlAutomation = '/automation';
      const urlBooleanTrigger = '/booleanTrigger';
      const urlRangeTrigger = '/rangeTrigger';
      const urlScenePriority = '/scenePriority';
      // conditions
      const urlRangeCondition = '/rangeCondition';
      const urlBooleanCondition = '/booleanCondition';
      const urlThermostatCondition = '/thermostatCondition';

      const rangeTriggerList = triggerList.filter((trigger) => 'operand' in trigger);
      const booleanTriggerList = triggerList.filter(
        (trigger) => !('operand' in trigger),
      );
      const rangeConditionList = conditionList.filter((condition) => 'operand' in condition && 'value' in condition);
      const booleanConditionList = conditionList.filter((condition) => 'on' in condition);
      const thermostatConditionList = conditionList.filter((condition) => 'operand' in condition && 'mode' in condition);


      return Endpoint.post(urlAutomation, {}, automation).then(
        async (automationRes) => {
          const { id } = automationRes.data;
          // Introduce the range triggers in the automation
          const resRangeTriggers = [];
          for (const t of rangeTriggerList) {
            const trigger = {
              automationId: id,
              deviceId: t.device,
              operator: t.operand,
              range: t.value,
            };
            resRangeTriggers.push(Endpoint.post(urlRangeTrigger, {}, trigger));
          }
          automation.triggers = (await Promise.all(resRangeTriggers)).map((v) => v.data);

          const resBoolTriggers = [];
          for (const t of booleanTriggerList) {
            const trigger = {
              automationId: id,
              deviceId: t.device,
              on: t.on,
            };
            resBoolTriggers.push(Endpoint.post(
              urlBooleanTrigger,
              {},
              trigger,
            ));
          }
          automation.triggers.push(...((await Promise.all(resBoolTriggers)).map((v) => v.data)));

          // Conditions
          const resRangeConditions = [];
          for (const t of rangeConditionList) {
            const condition = {
              automationId: id,
              deviceId: t.device,
              operator: t.operand,
              range: t.value,
            };
            resRangeConditions.push(Endpoint.post(urlRangeCondition, {}, condition));
          }
          automation.conditions = (await Promise.all(resRangeConditions)).map((v) => v.data);

          const resBoolConditions = [];
          for (const t of booleanConditionList) {
            const condition = {
              automationId: id,
              deviceId: t.device,
              on: t.on,
            };
            resBoolConditions.push(Endpoint.post(
              urlBooleanCondition,
              {},
              condition,
            ));
          }
          automation.conditions.push(...((await Promise.all(resBoolConditions)).map((v) => v.data)));

          const resThermoConditions = [];
          for (const t of thermostatConditionList) {
            const condition = {
              automationId: id,
              deviceId: t.device,
              mode: t.mode,
              operator: t.operand,
            };
            resThermoConditions.push(Endpoint.post(
              urlThermostatCondition,
              {},
              condition,
            ));
          }
          automation.conditions.push(...((await Promise.all(resThermoConditions)).map((v) => v.data)));

          const resScenePriorities = [];
          for (const [priority, sceneId] of order.entries()) {
            const scenePriority = {
              automationId: id,
              priority,
              sceneId,
            };
            resScenePriorities.push(Endpoint.post(
              urlScenePriority,
              {},
              scenePriority,
            ));
          }
          automation.scenes = (await Promise.all(resScenePriorities)).map((v) => v.data);
          automation.id = id;
          dispatch(actions.automationSave(automation));
        },
      );
    };
  },

  /**
   * Creates/Updates a state with the given data. If
   * data.id is truthy, then a update call is performed,
   * otherwise a create call is performed. The update URL
   * is computed based on data.kind when data.flowType =
   * 'OUTPUT', otherwise the PUT "/device" endpoint
   * is used for updates and the POST "/<device.kind>"
   * endpoints are used for creation.
   * @param {State} data the device to update.
   * @returns {Promise<Device, RemoteError>} promise that resolves to the saved device and rejects
   *  with user-fiendly errors as a RemoteError
   */
  saveState: (data) => (dispatch) => {
      const url = `/${data.kind}/${data.id}/state?sceneId=${data.sceneId}`;

      return Endpoint.post(url, {}, data)
        .then((res) => {
          dispatch(actions.stateSave(res.data));
          return res.data;
        })
        .catch((err) => {
          console.warn('Update device: ', data, 'error: ', err);
          throw new RemoteError(['Network error']);
        });
    },

  /**
   * Connetcs a series of output devices to an input device.
   * Output devices for Switch input can be: Normal Light, Dimmable Light, Smart Plug.
   * Output devices for Dimmers input can be: Dimmable Light.
   *
   * @typedef {"switch" | "buttonDimmer" | "knobDimmer"} ConnectableInput
   *
   * @param {ConnectableInput} newDevice.kind kind of the input device
   * @param {Integer} newDevice.id id of the input device
   * @param {Integer[]} outputs ids of the output device
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  connectOutputs: (newDevice, outputs) => (dispatch) => {
      const url = `/${newDevice.kind}/${newDevice.id}/lights`;

      return Endpoint.post(url, {}, outputs)
        .then((res) => {
          dispatch(actions.deviceOperationUpdate(res.data));
          return res.data;
        })
        .catch((err) => {
          console.warn(
            'ConnectOutputs of ',
            newDevice.id,
            ' with outputs: ',
            outputs,
            'error: ',
            err,
          );
          throw new RemoteError(['Network error']);
        });
    },

  _operateInput: (url, getUrl, action) => (dispatch) => Endpoint.put(url, {}, action)
        .then(async (res) => {
          const inputDevice = await Endpoint.get(getUrl);
          delete inputDevice.outputs;
          dispatch(
            actions.deviceOperationUpdate([...res.data, inputDevice.data]),
          );
        })
        .catch((err) => {
          console.warn(`${url} error`, err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Changes the state of a switch, by turning it on, off or toggling it.
   *
   * @typedef {"ON" | "OFF" | "TOGGLE"} SwitchOperation
   *
   * @param {Number} switchId the switch device id
   * @param {SwitchOperation} type the operation to perform on the switch
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  switchOperate: (switchId, type) => RemoteService._operateInput(
      '/switch/operate',
      `/switch/${switchId}`,
      {
        type: type.toUpperCase(),
        id: switchId,
      },
    ),

  /**
   * Turns a knob dimmer to a specific amount
   *
   * @param {Number} dimmerId the knob dimmer id
   * @param {number} intensity the absolute intensity to dim to. Must be >=0 and <= 100.
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  knobDimmerDimTo: (dimmerId, intensity) => RemoteService._operateInput(
      '/knobDimmer/dimTo',
      `/knobDimmer/${dimmerId}`,
      {
        intensity,
        id: dimmerId,
      },
    ),

  /**
   * Turns a button dimmer up or down
   *
   * @typedef {"UP" | "DOWN"} ButtonDimmerDimType
   *
   * @param {Number} dimmerId the button dimmer id
   * @param {ButtonDimmerDimType} dimType the type of dim to perform
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  buttonDimmerDim: (dimmerId, dimType) => RemoteService._operateInput(
      '/buttonDimmer/dim',
      `/buttonDimmer/${dimmerId}`,
      {
        dimType,
        id: dimmerId,
      },
    ),

  /**
   * Resets the meter on a smart plug
   *
   * @param {Number} smartPlugId the smart plug to reset
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  smartPlugReset(smartPlugId) {
    return (dispatch) => Endpoint.delete(`/smartPlug/${smartPlugId}/meter`)
        .then((res) => dispatch(actions.deviceOperationUpdate([res.data])))
        .catch((err) => {
          console.warn('Smartplug reset error', err);
          throw new RemoteError(['Network error']);
        });
  },
  /**
   * Deletes a room
   * @param {Number} roomId the id of the room to delete
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  deleteRoom: (roomId) => (dispatch) => Endpoint.delete(`/room/${roomId}`)
        .then((_) => dispatch(actions.roomDelete(roomId)))
        .catch((err) => {
          console.warn('Room deletion error', err);
          throw new RemoteError(['Network error']);
        }),

  deleteAutomation: (id) => {
    console.log('ID OF AUTO ', id);
    return (dispatch) => Endpoint.delete(`/automation/${id}`)
        .then((_) => dispatch(actions.automationDelete(id)))
        .catch((err) => {
          console.warn('Automation deletion error', err);
          throw new RemoteError(['Network error']);
        });
  },

  /**
   * Deletes a scene
   * @param {Number} sceneId the id of the scene to delete
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  deleteScene: (sceneId) => (dispatch) => Endpoint.delete(`/scene/${sceneId}`)
        .then((_) => dispatch(actions.sceneDelete(sceneId)))
        .catch((err) => {
          console.warn('Scene deletion error', err);
          throw new RemoteError(['Network error']);
        }),

  /**
   * Deletes a device
   * @param {Device} device the device to delete
   * @returns {Promise<Undefined, RemoteError>} promise that resolves to void and rejects
   *  with user-fiendly errors as a RemoteError
   */
  deleteDevice: (device) => (dispatch) => Endpoint.delete(`/${device.kind}/${device.id}`)
        .then((_) => dispatch(actions.deviceDelete(device.id)))
        .catch((err) => {
          console.warn('Device deletion error', err);
          throw new RemoteError(['Network error']);
        }),
};

for (const key in RemoteService) {
  RemoteService[key] = RemoteService[key].bind(RemoteService);
}

export class Forms {
  static fetchAllUsers() {
    return Endpoint.get('/user')
      .then((res) => res.data)
      .catch((err) => {
        console.error('Fetch users error', err);
        throw new RemoteError(['Network error']);
      });
  }

  /**
   * Attempts to create a new user from the given data.
   * This method does not update the global state,
   * please check its return value.
   * @param {String} data.username the chosen username
   * @param {String} data.password the chosen password
   * @param {String} data.email the chosen email
   * @param {String} data.name the chosen full name
   * @returns {Promise<Undefined, String[]>} promise that resolves to void and rejects
   *  with validation errors as a String array
   */
  static submitRegistration(data) {
    return Endpoint.postNA(
      '/register',
      {},
      {
        username: data.username,
        password: data.password,
        name: data.name,
        email: data.email,
      },
    )
      .then((_) => void 0)
      .catch(parseValidationErrors);
  }

  /**
   * Sends a request to perform a password reset.
   * This method does not update the global state,
   * please check its return value.
   * @param {String} email the email to which perform the reset
   * @returns {Promise<Undefined, String[]>} promise that resolves to void and rejects
   *  with validation errors as a String array
   */
  static submitInitResetPassword(email) {
    return Endpoint.postNA(
      '/register/init-reset-password',
      {},
      {
        email,
      },
    )
      .then((_) => void 0)
      .catch((err) => {
        console.warn('Init reset password failed', err);
        throw new RemoteError(['Network error']);
      });
  }

  /**
   * Sends the password for the actual password reset, haviug already
   * performed email verification
   * This method does not update the global state,
   * please check its return value.
   * @param {String} confirmationToken the confirmation token got from the email
   * @param {String} password the new password
   * @returns {Promise<Undefined, String[]>} promise that resolves to void and rejects
   *  with validation errors as a String array
   */
  static submitResetPassword(confirmationToken, password) {
    return Endpoint.putNA(
      '/register/reset-password',
      {},
      {
        confirmationToken,
        password,
      },
    )
      .then((_) => void 0)
      .catch(parseValidationErrors);
  }
}
