import React, { Component } from 'react';
import {
  Menu,
  Button,
  Grid,
  Icon,
  Responsive,
  Dropdown,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import { editButtonStyle } from '../components/dashboard/devices/styleComponents';
import RoomModal from '../components/RoomModal';
import { RemoteService } from '../remote';
import { appActions } from '../storeActions';

class Navbar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      editMode: false,
    };

    this.toggleEditMode = this.toggleEditMode.bind(this);
    this.selectRoom = this.selectRoom.bind(this);
    this.openCurrentModalMobile = this.openCurrentModalMobile.bind(this);

    this.getRooms();
  }

  getRooms() {
    this.props.fetchAllRooms().catch(console.error);
  }

  get activeItem() {
    return this.props.activeRoom;
  }

  set activeItem(item) {
    this.props.setActiveRoom(item);
  }

  get activeItemName() {
    if (this.props.activeRoom === -1) return 'Home';
    return this.props.rooms[this.props.activeRoom].name;
  }

  openCurrentModalMobile() {
    console.log(this.activeItem, this.props.roomModalRefs);
    const currentModal = this.props.roomModalRefs[this.activeItem].current;
    currentModal.openModal();
  }

  toggleEditMode(e) {
    this.setState((prevState) => ({ editMode: !prevState.editMode }));
  }

  selectRoom(e, { id }) {
    this.activeItem = id || -1;
  }

  render() {
    return (
      <div>
        <Responsive minWidth={768}>
          <Grid style={{ margin: '1em -1em 0 1em' }}>
            <Grid.Row color="black">
              <button style={editButtonStyle} onClick={this.toggleEditMode}>
                Edit
              </button>
            </Grid.Row>
            <Grid.Row>
              <Menu inverted fluid vertical>
                <Menu.Item
                  key={-1}
                  id={null}
                  name="Home"
                  active={this.activeItem === -1}
                  onClick={this.selectRoom}
                >
                  <Grid>
                    <Grid.Row>
                      <Grid.Column>
                        <Icon name="home" size="small" />
                      </Grid.Column>
                      <Grid.Column width={8}>
                        <strong>Home view</strong>
                      </Grid.Column>
                    </Grid.Row>
                  </Grid>
                </Menu.Item>

                {Object.values(this.props.rooms).map((e, i) => (
                  <Menu.Item
                    id={e.id}
                    key={i}
                    name={e.name}
                    active={this.activeItem === e.id}
                    onClick={this.selectRoom}
                  >
                    <Grid>
                      <Grid.Row>
                        <Grid.Column>
                          <Icon name={e.icon} size="small" />
                        </Grid.Column>
                        <Grid.Column width={11}>{e.name}</Grid.Column>
                        <Grid.Column floated="right">
                          {this.state.editMode ? (
                            <RoomModal id={e.id} />
                            ) : null}
                        </Grid.Column>
                      </Grid.Row>
                    </Grid>
                  </Menu.Item>
                  ))}

                <Menu.Item name="newM">
                  <Grid>
                    <Grid.Row centered name="new">
                      <RoomModal id={null} />
                    </Grid.Row>
                  </Grid>
                </Menu.Item>
              </Menu>
            </Grid.Row>
          </Grid>
        </Responsive>

        <Responsive maxWidth={768}>
          <Menu>
            <Dropdown item fluid text={this.activeItemName}>
              <Dropdown.Menu>
                <Dropdown.Item
                  key={-1}
                  id={null}
                  name="Home"
                  active={this.activeItem === -1}
                  onClick={this.selectRoom}
                >
                  <Grid>
                    <Grid.Row>
                      <Grid.Column>
                        <Icon name="home" size="small" />
                      </Grid.Column>
                      <Grid.Column>Home</Grid.Column>
                    </Grid.Row>
                  </Grid>
                </Dropdown.Item>

                {Object.values(this.props.rooms).map((e, i) => (
                  <Dropdown.Item
                    id={e.id}
                    key={i}
                    name={e.name}
                    active={this.activeItem === e.id}
                    onClick={this.selectRoom}
                  >
                    <Grid>
                      <Grid.Row>
                        <Grid.Column width={1}>
                          <Icon name={e.icon} size="small" />
                        </Grid.Column>
                        <Grid.Column>{e.name}</Grid.Column>
                      </Grid.Row>
                    </Grid>
                    <RoomModal
                      ref={this.props.roomModalRefs[e.id]}
                      nicolaStop
                      id={e.id}
                    />
                  </Dropdown.Item>
                  ))}
              </Dropdown.Menu>
            </Dropdown>
          </Menu>
          <Grid inverted>
            <Grid.Row>
              <Grid.Column width={8}>
                <RoomModal id={null} />
              </Grid.Column>
              {this.activeItem !== -1 ? (
                <Grid.Column width={8}>
                  <Button
                    icon
                    fluid
                    labelPosition="left"
                    onClick={this.openCurrentModalMobile}
                  >
                    <Icon name="pencil" size="small" />
                    EDIT ROOM
                  </Button>
                </Grid.Column>
              ) : null}
            </Grid.Row>
          </Grid>
        </Responsive>
      </div>
    );
  }
}

const setActiveRoom = (activeRoom) => (dispatch) => dispatch(appActions.setActiveRoom(activeRoom));

const mapStateToProps = (state, _) => ({
  rooms: state.rooms,
  activeRoom: state.active.activeRoom,
  roomModalRefs: Object.keys(state.rooms).reduce(
    (acc, key) => ({ ...acc, [key]: React.createRef() }),
    {},
  ),
});
const NavbarContainer = connect(mapStateToProps, {
  ...RemoteService,
  setActiveRoom,
})(Navbar);
export default NavbarContainer;
