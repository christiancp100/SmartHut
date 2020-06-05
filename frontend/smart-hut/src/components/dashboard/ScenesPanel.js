import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
 Button, Card, Segment, Header, Icon,
} from 'semantic-ui-react';
import { RemoteService } from '../../remote';
import Device from './devices/Device';
import NewSceneDevice from './NewSceneDevice';

class ScenesPanel extends Component {
  constructor(props) {
    super(props);
    this.applyScene = this.applyScene.bind(this);
  }

  applyScene() {
    this.props
      .sceneApply(this.props.activeScene)
      .then(() => {
        alert('Scene applied.');
      })
      .catch(console.error);
  }

  render() {
    return (
      <Card.Group centered style={{ paddingTop: '3rem' }}>
        {!this.props.isActiveDefaultScene ? (
          <Card style={{ height: '27em' }}>
            <Card.Content>
              <Card.Header textAlign="center">
                <Header as="h3">Add devices - Apply Scene</Header>
              </Card.Header>
              <Segment basic style={{ width: '100%', height: '100%' }}>
                <NewSceneDevice states={this.props.sceneStates} />
              </Segment>
            </Card.Content>
            <Card.Content extra>
              <div className="ui two buttons">
                <Button color="blue" onClick={this.applyScene}>
                  Apply Scene
                </Button>
              </div>
            </Card.Content>
          </Card>
        ) : (
          <Segment placeholder>
            <Header icon>
              <Icon
                name="exclamation triangle"
                style={{ paddingBottom: '1rem' }}
              />
              Please select a scene on the left or add a new one.
            </Header>
          </Segment>
        )}
        {!this.props.isActiveDefaultScene
          ? this.props.sceneStates.map((e, i) => <Device key={i} tab={this.props.tab} id={e.id} />)
          : null}
      </Card.Group>
    );
  }
}

const mapStateToProps = (state, _) => ({
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
  get isActiveDefaultScene() {
    return state.active.activeScene === -1;
  },
  activeScene: state.active.activeScene,
});
const ScenesPanelContainer = connect(
  mapStateToProps,
  RemoteService,
)(ScenesPanel);
export default ScenesPanelContainer;
