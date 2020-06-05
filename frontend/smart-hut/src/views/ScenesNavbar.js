import React, { Component } from 'react';
import {
  Menu,
  Button,
  Icon,
  Grid,
  Responsive,
  Dropdown,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import { editButtonStyle } from '../components/dashboard/devices/styleComponents';
import SceneModal from '../components/SceneModal';
import { RemoteService } from '../remote';
import { appActions } from '../storeActions';

class ScenesNavbar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      editMode: false,
    };
    this.toggleEditMode = this.toggleEditMode.bind(this);
    this.openCurrentModalMobile = this.openCurrentModalMobile.bind(this);
    this.selectScene = this.selectScene.bind(this);

    this.getScenes();
  }

  get activeItemScene() {
    return this.props.activeScene;
  }

  set activeItemScene(item) {
    this.props.setActiveScene(item);
  }

  get activeItemSceneName() {
    if (this.props.activeScene === -1) return 'Scene';
    return this.props.scenes[this.props.activeScene].name;
  }

  getScenes() {
    this.props.fetchAllScenes().catch(console.error);
  }

  openCurrentModalMobile() {
    // console.log(this.activeItemScene, this.props.sceneModalRefs);
    const currentModal = this.props.sceneModalRefs[this.activeItemScene]
      .current;
    currentModal.openModal();
  }

  toggleEditMode(e) {
    this.setState((prevState) => ({ editMode: !prevState.editMode }));
  }

  selectScene(e, { id }) {
    this.activeItemScene = id || -1;
    this.getStates(id);
  }

  getStates(sceneId) {
    if (sceneId) {
      this.props
        .fetchStates(sceneId)
        .catch((err) => console.error('error fetching states:', err));
    }
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
                  name="scene"
                  active={this.activeItemScene === -1}
                  onClick={this.selectScene}
                >
                  <strong>Scenes</strong>
                </Menu.Item>

                {Object.values(this.props.scenes).map((e, i) => (
                  <Menu.Item
                    id={e.id}
                    key={i}
                    name={e.name}
                    active={this.activeItemScene === e.id}
                    onClick={this.selectScene}
                  >
                    <Grid>
                      <Grid.Row>
                        <Grid.Column>
                          <Icon name={e.icon} size="small" />
                        </Grid.Column>
                        <Grid.Column width={11}>{e.name}</Grid.Column>
                        <Grid.Column floated="right">
                          {this.state.editMode ? (
                            <SceneModal id={e.id} />
                            ) : null}
                        </Grid.Column>
                      </Grid.Row>
                    </Grid>
                  </Menu.Item>
                  ))}

                <Menu.Item name="newM">
                  <Grid>
                    <Grid.Row centered name="new">
                      <SceneModal id={null} />
                    </Grid.Row>
                  </Grid>
                </Menu.Item>
              </Menu>
            </Grid.Row>
          </Grid>
        </Responsive>

        <Responsive maxWidth={768}>
          <Menu>
            <Dropdown item fluid text={this.activeItemSceneName}>
              <Dropdown.Menu>
                <Dropdown.Item
                  key={-1}
                  id={null}
                  name="scene"
                  active={this.activeItemScene === -1}
                  onClick={this.selectScene}
                >
                  <Grid>
                    <Grid.Row>
                      <Grid.Column>Scenes</Grid.Column>
                    </Grid.Row>
                  </Grid>
                </Dropdown.Item>

                {Object.values(this.props.scenes).map((e, i) => (
                  <Dropdown.Item
                    id={e.id}
                    key={i}
                    name={e.name}
                    active={this.activeItemScene === e.id}
                    onClick={this.selectScene}
                  >
                    <Grid>
                      <Grid.Row>
                        <Grid.Column>{e.name}</Grid.Column>
                      </Grid.Row>
                    </Grid>
                    <SceneModal
                      ref={this.props.sceneModalRefs[e.id]}
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
                <SceneModal id={null} />
              </Grid.Column>
              {this.activeItemScene !== -1 ? (
                <Grid.Column width={8}>
                  <Button
                    icon
                    fluid
                    labelPosition="left"
                    onClick={this.openCurrentModalMobile}
                  >
                    <Icon name="pencil" size="small" />
                    EDIT SCENE
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

const setActiveScene = (activeScene) => (dispatch) => dispatch(appActions.setActiveScene(activeScene));

const mapStateToProps = (state, _) => ({
  scenes: state.scenes,
  sceneModalRefs: Object.keys(state.scenes).reduce(
    (acc, key) => ({ ...acc, [key]: React.createRef() }),
    {},
  ),
  get isActiveDefaultScene() {
    return state.active.activeScene === -1;
  },
  activeScene: state.active.activeScene,
});
const ScenesNavbarContainer = connect(mapStateToProps, {
  ...RemoteService,
  setActiveScene,
})(ScenesNavbar);
export default ScenesNavbarContainer;
