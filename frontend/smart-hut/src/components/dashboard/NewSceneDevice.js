import React, { Component } from 'react';
import {
 Button, Modal, Icon, Image, Form, Dropdown,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import styled from 'styled-components';
import { RemoteService } from '../../remote';
// import { appActions } from "../../storeActions";

const StyledDiv = styled.div`
  background-color: #505bda;
  padding: 3rem;
  width: 10rem;
  height: 10rem;
  border-radius: 100%;
  border: none;
  position: relative;
  box-shadow: 3px 2px 10px 5px #ccc;
  transition: all 0.3s ease-out;
  :hover {
    background-color: #4345d9;
  }
  :active {
    transform: translate(0.3px, 0.8px);
    box-shadow: 0.5px 0.5px 7px 3.5px #ccc;
  }
`;

class NewSceneDevice extends Component {
  constructor(props) {
    super(props);

    this.state = {
      openModal: false,
      sceneDevices: this.props.scene ? this.props.scene.sceneStates : {},
      deviceName: '',
      availableDevices: [],
    };
    this.getDevices();
    // this.getSceneStates();
    this.availableDevices();
    // console.log(this.state);

    this.setSceneState = this.setSceneState.bind(this);
    this.createState = this.createState.bind(this);
    this.availableDevices = this.availableDevices.bind(this);
  }

  getDevices() {
    this.props
      .fetchDevices()
      .catch((err) => console.error('error fetching devices:', err));
  }

  // getSceneStates() {
  //   this.props
  //     .fetchStates(this.props.activeScene)
  //     .catch((err) => console.error(`error fetching states`, err));
  // }

  handleOpen = () => {
    this.setState({ openModal: true });
  };

  handleClose = () => {
    this.setState({ openModal: false });
  };

  availableDevices() {
    const availableDevices = [];
    this.props.devices.forEach((e) => {
      if (
        Object.values(this.props.sceneStates).filter((d) => e.id === d.deviceId)
          .length < 1
      ) {
        if (e.flowType === 'OUTPUT') {
          availableDevices.push({
            key: e.id,
            text: e.name,
            value: e.id,
          });
        }
      } else {
        // console.log("NOT FOUND", e);
      }
    });
    this.setState({ availableDevices });
    // return availableDevices;
  }

  resetState = () => {
    this.setState(this.baseState);
    this.handleClose();
  };

  setSceneState(e, d) {
    this.setState({ devicesAttached: d.value });
  }

  createState() {
    for (let i = 0; i < this.state.devicesAttached.length; i++) {
      const device = this.props.devices.filter(
        (e) => this.state.devicesAttached[i] === e.id,
      );
      const data = {
        sceneId: this.props.activeScene,
        id: device[0].id,
        kind: device[0].kind,
      };
      this.props
        .saveState(data)
        .catch((err) => console.error('error in creating state', err));
    }
    this.resetState();
  }

  render() {
    return (
      <Modal
        closeIcon
        open={this.state.openModal}
        onClose={this.resetState}
        trigger={(
          <StyledDiv
            onClick={this.handleOpen}
            style={{
              position: 'relative',
              top: 'calc(50% - 5rem)',
              left: 'calc(50% - 5rem)',
            }}
          >
            <Image src="/img/add.svg" style={{ filter: 'invert()' }} />
          </StyledDiv>
        )}
        centered
      >
        <Modal.Header>Add a New Scene State</Modal.Header>
        <Modal.Content>
          <Form>
            <Form.Field style={{ marginTop: '1rem' }}>
              <label>Select devices you want to attach: </label>
              <Dropdown
                name="scene devices"
                placeholder="Select Devices"
                fluid
                multiple
                onChange={this.setSceneState}
                onClick={() => this.availableDevices()}
                options={this.state.availableDevices}
              />
            </Form.Field>
          </Form>
        </Modal.Content>
        <Modal.Actions>
          <Button
            onClick={this.createState}
            color="blue"
            icon
            labelPosition="right"
          >
            <Icon name="up arrow" />
            Finish
          </Button>
        </Modal.Actions>
      </Modal>
    );
  }
}

const mapStateToProps = (state, _) => ({
  devices: Object.values(state.devices),
  get sceneStates() {
    if (state.active.activeScene !== -1) {
      const stateArray = [
        ...state.scenes[state.active.activeScene].sceneStates,
      ].sort();
      console.log(state.scenes[state.active.activeScene]);
      return stateArray.map((id) => state.sceneStates[id]);
    }
      return [];
  },
  activeScene: state.active.activeScene,
});
const NewSceneDeviceContainer = connect(
  mapStateToProps,
  RemoteService,
)(NewSceneDevice);
export default NewSceneDeviceContainer;
