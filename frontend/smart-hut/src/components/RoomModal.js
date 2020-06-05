import React, { Component } from 'react';
import {
  Button,
  Header,
  Modal,
  Form,
  Input,
  Icon,
  Responsive,
  Image,
  Confirm,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import SelectIcons from './SelectIcons';
import { RemoteService } from '../remote';
import { appActions } from '../storeActions';

const NO_IMAGE = 'https://react.semantic-ui.com/images/wireframe/image.png';

class RoomModal extends Component {
  constructor(props) {
    super(props);
    this.state = this.initialState;

    this.fileInputRef = React.createRef();

    this.addRoomModal = this.addRoomModal.bind(this);
    this.updateIcon = this.updateIcon.bind(this);
    this.unsetImage = this.unsetImage.bind(this);
  }

  get initialState() {
    return {
      selectedIcon: this.type === 'new' ? 'home' : this.props.room.icon,
      name: this.type === 'new' ? 'New Room' : this.props.room.name,
      img: this.type === 'new' ? null : this.props.room.image,
      openModal: false,
      sure: false,
    };
  }

  unsetImage = (e) => {
    e.preventDefault();
    this.setState({ ...this.state, img: '' });
  };

  setInitialState() {
    this.setState(this.initialState);
  }

  get type() {
    return !this.props.id ? 'new' : 'modify';
  }

  addRoomModal = (e) => {
    const data = {
      icon: this.state.selectedIcon,
      name: this.state.name,
      image: this.state.img,
    };

    this.props
      .saveRoom(data, null)
      .then(() => {
        this.setInitialState();
        this.closeModal();
      })
      .catch((err) => console.error('error in creating room', err));
  };

  modifyRoomModal = (e) => {
    const data = {
      icon: this.state.selectedIcon,
      name: this.state.name,
      image: this.state.img,
    };

    console.log('data', data);

    this.props
      .saveRoom(data, this.props.id)
      .then(() => {
        this.setInitialState();
        this.closeModal();
      })
      .catch((err) => console.error('error in updating room', err));
  };

  deleteRoom = (e) => {
    this.props
      .deleteRoom(this.props.id)
      .then(() => this.closeModal())
      .catch((err) => console.error('error in deleting room', err));
  };

  setSureTrue = () => {
    this.setState({ sure: true });
  };

  setSureFalse = () => {
    this.setState({ sure: false });
  };

  changeSomething = (event) => {
    const nam = event.target.name;
    const val = event.target.value;
    this.setState({ [nam]: val });
  };

  closeModal = (e) => {
    this.setState({ openModal: false });
  };

  openModal = (e) => {
    this.setState({ openModal: true });
  };

  updateIcon(e) {
    this.setState({ selectedIcon: e });
  }

  getBase64(file, callback) {
    const reader = new FileReader();
    reader.readAsDataURL(file.target.files[0]);
    reader.onload = () => {
      this.setState(Object.assign(this.state, { img: reader.result }));
    };
    reader.onerror = console.error;
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
                  ADD ROOM
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
                  ADD ROOM
                </Button>
              ) : (
                <Button
                  icon
                  fluid
                  labelPosition="left"
                  onClick={this.openModal}
                >
                  <Icon name="pencil" size="small" />
                  EDIT ROOM
                </Button>
              )}
            </Responsive>
          </div>
        ) : null}

        <Modal closeIcon onClose={this.closeModal} open={this.state.openModal}>
          <Header>
            {this.type === 'new' ? 'Add new room' : 'Modify room'}
          </Header>
          <Modal.Content>
            <Form>
              <p>Insert the name of the room:</p>
              <Form.Field>
                <Input
                  label="Room name"
                  placeholder="Room Name"
                  name="name"
                  type="text"
                  onChange={this.changeSomething}
                  value={this.state.name}
                />
              </Form.Field>
              <p>Insert an image of the room:</p>
              <Form.Field>
                <Image
                  src={!this.state.img ? NO_IMAGE : this.state.img}
                  size="small"
                  onClick={() => this.fileInputRef.current.click()}
                />

                <input
                  ref={this.fileInputRef}
                  hidden
                  label="Room image"
                  type="file"
                  name="img"
                  accept="image/png, image/jpeg"
                  onChange={this.getBase64.bind(this)}
                />
              </Form.Field>
              {this.state.img ? (
                <Button onClick={this.unsetImage}>Remove image</Button>
              ) : null}
            </Form>

            <div style={spaceDiv}>
              <p>Select an icon:</p>
              <SelectIcons
                updateIcon={this.updateIcon}
                currentIcon={
                  this.type === 'new' ? 'home' : this.props.room.icon
                }
              />
            </div>

            {this.type === 'modify' ? (
              <div>
                <Button
                  icon
                  labelPosition="left"
                  inverted
                  color="red"
                  onClick={this.setSureTrue}
                >
                  <Icon name="trash alternate" />
                  Delete Room
                  {' '}
                </Button>
                <Confirm
                  open={this.state.sure}
                  onCancel={this.setSureFalse}
                  onConfirm={this.deleteRoom}
                />
              </div>
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
                this.type === 'new' ? this.addRoomModal : this.modifyRoomModal
              }
            >
              <Icon name="checkmark" />
              {' '}
              {this.type === 'new' ? 'Add room' : 'Save changes'}
            </Button>
          </Modal.Actions>
        </Modal>
      </div>
    );
  }
}

const setActiveRoom = (activeRoom) => (dispatch) => dispatch(appActions.setActiveRoom(activeRoom));

const mapStateToProps = (state, ownProps) => ({
  room: ownProps.id ? state.rooms[ownProps.id] : null,
});
const RoomModalContainer = connect(
  mapStateToProps,
  { ...RemoteService, setActiveRoom },
  null,
  { forwardRef: true },
)(RoomModal);
export default RoomModalContainer;
