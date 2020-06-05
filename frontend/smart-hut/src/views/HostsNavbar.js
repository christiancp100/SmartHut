import React, { Component } from 'react';
import {
 Menu, Grid, Responsive, Dropdown,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import HostModal from '../components/HostModal';
import { RemoteService } from '../remote';
import { appActions } from '../storeActions';

class HostsNavbar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      editMode: false,
    };

    this.getHosts();
    this.selectHosts = this.selectHosts.bind(this);
  }

  getHosts() {
    this.props.fetchHosts().catch(console.error);
  }

  selectHosts(_, { id }) {
    this.props.setActiveHost(id || -1);
  }

  get activeItem() {
    return this.props.activeHost;
  }

  get activeItemHostsName() {
    if (this.props.activeItem === -1) return 'Home';
    return this.props.hosts[this.props.activeHost].name;
  }

  render() {
    return (
      <div>
        <Responsive minWidth={768}>
          <Grid style={{ margin: '1em -1em 0 1em' }}>
            <Grid.Row>
              <Menu inverted fluid vertical>
                <Menu.Item
                  key={-1}
                  id={null}
                  name="hosts"
                  active={this.activeItem === -1}
                  onClick={this.selectHosts}
                >
                  <strong>Hosts</strong>
                </Menu.Item>
                {Object.values(this.props.hosts).map((e, i) => (
                  <Menu.Item
                    id={e.id}
                    key={i}
                    name={e.name}
                    active={this.activeItem === e.id}
                    onClick={this.selectHosts}
                  >
                    {e.name}
                  </Menu.Item>
                  ))}
                <Menu.Item name="newM">
                  <HostModal />
                </Menu.Item>
              </Menu>
            </Grid.Row>
          </Grid>
        </Responsive>

        <Responsive maxWidth={768}>
          <Menu>
            <Dropdown item fluid text={this.activeItemHostName}>
              <Dropdown.Menu>
                <Dropdown.Item
                  key={-1}
                  id={null}
                  name="scene"
                  active={this.activeItem === -1}
                  onClick={this.selectHosts}
                >
                  <strong>Hosts</strong>
                </Dropdown.Item>

                {Object.values(this.props.hosts).map((e, i) => (
                  <Menu.Item
                    id={e.id}
                    key={i}
                    name={e.name}
                    active={this.activeItem === e.id}
                    onClick={this.selectHosts}
                  >
                    {e.name}
                  </Menu.Item>
                  ))}
              </Dropdown.Menu>
            </Dropdown>
          </Menu>
        </Responsive>
      </div>
    );
  }
}

const setActiveHost = (activeHost) => (dispatch) => dispatch(appActions.setActiveHost(activeHost));

const mapStateToProps = (state, _) => ({
  hosts: state.hosts,
  activeHost: state.active.activeHost,
});
const HostsNavbarContainer = connect(mapStateToProps, {
  ...RemoteService,
  setActiveHost,
})(HostsNavbar);
export default HostsNavbarContainer;
