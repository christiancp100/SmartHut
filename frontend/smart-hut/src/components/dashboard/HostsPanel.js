import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
 Card, Segment, Header, Icon, Button,
} from 'semantic-ui-react';
import { RemoteService } from '../../remote';
import Device from './devices/Device';

class HostsPanel extends Component {
  componentDidUpdate(oldProps) {
    if (
      oldProps.activeHost !== this.props.activeHost
      && this.props.activeHost !== -1
    ) {
      this.props.fetchDevices(null, this.props.activeHost).catch(console.error);
      this.props.fetchAllRooms(this.props.activeHost).catch(console.error);
      this.props.fetchAllScenes(this.props.activeHost).catch(console.error);
    }
  }

  applyHostScene(id) {
    this.props
      .sceneApply(id, this.props.activeHost)
      .then(() => console.log('SCCUESS'))
      .catch((err) => console.error('sceneApply update error', err));
  }

  render() {
    if (this.props.isActiveDefaultHost) {
      return (
        <Card.Group centered style={{ paddingTop: '3rem' }}>
          <Segment placeholder>
            <Header icon>
              <Icon
                name="exclamation triangle"
                style={{ paddingBottom: '1rem' }}
              />
              Please select a host to visit on the left.
            </Header>
          </Segment>
        </Card.Group>
      );
    }

    return (
      <>
        <Header style={{ textAlign: 'center', marginTop: '3rem' }} as="h3">
          Scenes
        </Header>
        <Card.Group centered>
          {this.props.hostScenes.map((scene) => (
            <Card>
              <Card.Header textAlign="center">
                <Header style={{ margin: '1.5rem 0' }} as="h3">
                  {scene.name}
                  {' '}
                  <Icon name={scene.icon} />
                </Header>
              </Card.Header>
              <Card.Content extras>
                <div className="ui two buttons">
                  <Button onClick={() => this.applyHostScene(scene.id)}>
                    Apply
                  </Button>
                </div>
              </Card.Content>
            </Card>
          ))}
        </Card.Group>
        <Header style={{ textAlign: 'center' }} as="h3">
          Devices
        </Header>
        <Card.Group centered>
          {this.props.hostDeviceIds.map((id) => (
            <Device
              key={id}
              hostId={this.props.activeHost}
              tab="Hosts"
              id={id}
            />
            ))}
        </Card.Group>
      </>
    );
  }
}

const mapStateToProps = (state, _) => ({
  isActiveDefaultHost: state.active.activeHost === -1,
  activeHost: state.active.activeHost,
  hostScenes: state.hostScenes[state.active.activeHost] || [],
  hostDevices: state.hostDevices,
  hostDeviceIds: Object.keys(state.hostDevices[state.active.activeHost] || {}),
});
const HostsPanelContainer = connect(mapStateToProps, RemoteService)(HostsPanel);
export default HostsPanelContainer;
