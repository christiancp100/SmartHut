import React, { Component } from 'react';
import {
  Button,
  Header,
  Modal,
  Icon,
  Responsive,
  Form,
  Input,
  Dropdown,
  Checkbox,
  Segment,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import SelectIcons from './SelectIcons';
import { RemoteService } from '../remote';
import { appActions } from '../storeActions';
// import { update } from "immutability-helper";

class SceneModal extends Component {
  constructor(props) {
    super(props);
    this.state = this.initialState;
    this.addSceneModal = this.addSceneModal.bind(this);
    this.modifySceneModal = this.modifySceneModal.bind(this);
    this.deleteScene = this.deleteScene.bind(this);
    this.updateIcon = this.updateIcon.bind(this);
    this.setGuestAccessEnabled = this.setGuestAccessEnabled.bind(this);
    this.setCopyFrom = this.setCopyFrom.bind(this);
  }

  componentDidUpdate(oldProps) {
    // this might bug out since we are just checking the length
    // to see if the elements inside this.props.scenes are changing
    if (this.props.scenes.length !== oldProps.scenes.length) {
      this.setState({ ...this.state, scenes: this.scenes });
    }
  }

  get initialState() {
    return {
      name: this.type === 'new' ? 'New Scene' : this.props.scene.name,
      openModal: false,
      selectedIcon: 'home',
      scenes: this.scenes,
      copyFrom: null,
      guestAccessEnabled:
        this.type === 'new' ? null : this.props.scene.guestAccessEnabled,
    };
  }

  get scenes() {
    return this.props.scenes.map((s) => ({
      key: s.id,
      text: s.name,
      value: s.id,
    }));
  }

  setInitialState() {
    this.setState(this.initialState);
  }

  get type() {
    return !this.props.id ? 'new' : 'modify';
  }

  addSceneModal = (e) => {
    const data = {
      name: this.state.name,
      icon: this.state.selectedIcon,
    };

    this.props
      .saveScene(data, null, this.state.copyFrom)
      .then(() => {
        this.setInitialState();
        this.closeModal();
      })
      .catch((err) => console.error('error in creating room', err));
  };

  modifySceneModal = (e) => {
    const data = {
      name: this.state.name,
      icon: this.state.selectedIcon,
      guestAccessEnabled: this.state.guestAccessEnabled,
    };
    console.log(data);

    this.props
      .saveScene(data, this.props.id)
      .then(() => {
        this.setInitialState();
        this.closeModal();
      })
      .catch((err) => console.error('error in updating room', err));
  };

  deleteScene = (e) => {
    this.props
      .deleteScene(this.props.id)
      .then(() => this.closeModal())
      .catch((err) => console.error('error in deleting room', err));
  };

  changeSomething = (event) => {
    const nam = event.target.name;
    const val = event.target.value;
    this.setState({ [nam]: val });
  };

  closeModal = (e) => {
    this.setState({ ...this.state, openModal: false });
  };

  openModal = (e) => {
    this.setState({ ...this.state, openModal: true });
  };

  updateIcon(e) {
    this.setState({ ...this.state, selectedIcon: e });
  }

  setCopyFrom(_, copyFrom) {
    this.setState({ ...this.state, copyFrom: copyFrom.value });
  }

  setGuestAccessEnabled(val) {
    console.log(this.state, val);
    this.setState({ ...this.state, guestAccessEnabled: val });
  }

  render() {
    const spaceDiv = {
      background: '#f4f4f4',
      padding: '10px 10px',
      margin: '10px 0px',
    };
    return (
      <div>
        {!this.props.nicolaStop ? (
          <div>
            <Responsive minWidth={768}>
              {this.type === 'new' ? (
                <Button
                  icon
                  labelPosition="left"
                  inverted
                  onClick={this.openModal}
                >
                  <Icon name="plus" size="small" />
                  ADD SCENE
                </Button>
              ) : (
                <Icon name="pencil" size="small" onClick={this.openModal} />
              )}
            </Responsive>
            <Responsive maxWidth={768}>
              {this.type === 'new' ? (
                <Button
                  icon
                  fluid
                  labelPosition="left"
                  onClick={this.openModal}
                >
                  <Icon name="plus" size="small" />
                  ADD SCENE
                </Button>
              ) : (
                <Button
                  icon
                  fluid
                  labelPosition="left"
                  onClick={this.openModal}
                >
                  <Icon name="pencil" size="small" />
                  EDIT SCENE
                </Button>
              )}
            </Responsive>
          </div>
        ) : null}

        <Modal closeIcon onClose={this.closeModal} open={this.state.openModal}>
          <Header>
            {this.type === 'new' ? 'Add new scene' : 'Modify scene'}
          </Header>
          <Modal.Content>
            <Form>
              <p>Insert the name of the scene:</p>
              <Form.Field>
                <Input
                  required
                  label="Scene name"
                  placeholder="Scene Name"
                  name="name"
                  type="text"
                  onChange={this.changeSomething}
                  value={this.state.name}
                />
              </Form.Field>
              <div style={spaceDiv}>
                <label>Icon:</label>
                <SelectIcons
                  updateIcon={this.updateIcon}
                  currentIcon={
                    this.type === 'new' ? 'home' : this.props.scene.icon
                  }
                />
              </div>
              {this.type === 'new' && (
                <Form.Field>
                  <label>Copy configuration from:</label>
                  <Dropdown
                    name="guests"
                    placeholder="Select scene to copy configuration form"
                    fluid
                    onChange={this.setCopyFrom}
                    options={this.state.scenes}
                    value={this.state.copyFrom}
                  />
                </Form.Field>
              )}
              {this.type === 'modify' ? (
                <Form.Field>
                  <Segment compact style={{ marginBottom: '1rem' }}>
                    <Checkbox
                      label="Enable guest access"
                      checked={this.state.guestAccessEnabled}
                      toggle
                      onChange={(e, val) => this.setGuestAccessEnabled(val.checked)}
                    />
                  </Segment>
                </Form.Field>
              ) : null}
            </Form>

            {this.type === 'modify' ? (
              <Button
                icon
                labelPosition="left"
                inverted
                color="red"
                onClick={this.deleteScene}
              >
                <Icon name="trash alternate" />
                Delete Scene
              </Button>
            ) : null}
          </Modal.Content>
          <Modal.Actions>
            <Button color="red" onClick={this.closeModal}>
              <Icon name="remove" />
              {' '}
              {this.type === 'new' ? 'Cancel' : 'Discard changes'}
            </Button>

            <Button
              color="green"
              onClick={
                this.type === 'new' ? this.addSceneModal : this.modifySceneModal
              }
            >
              <Icon name="checkmark" />
              {' '}
              {this.type === 'new' ? 'Add scene' : 'Save changes'}
            </Button>
          </Modal.Actions>
        </Modal>
      </div>
    );
  }
}

const setActiveScene = (activeScene) => (dispatch) => dispatch(appActions.setActiveScene(activeScene));

const mapStateToProps = (state, ownProps) => ({
  scene: ownProps.id ? state.scenes[ownProps.id] : null,
  scenes: Object.values(state.scenes),
});
const SceneModalContainer = connect(
  mapStateToProps,
  { ...RemoteService, setActiveScene },
  null,
  { forwardRef: true },
)(SceneModal);
export default SceneModalContainer;
