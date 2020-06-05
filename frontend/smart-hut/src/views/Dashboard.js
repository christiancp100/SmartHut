import React, { Component } from 'react';
import {
 Grid, Responsive, Button, Menu,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import DevicePanel from '../components/dashboard/DevicePanel';
import ScenesPanel from '../components/dashboard/ScenesPanel';
import AutomationsPanel from '../components/dashboard/AutomationsPanel';
import HostsPanel from '../components/dashboard/HostsPanel';
import Navbar from './Navbar';
import ScenesNavbar from './ScenesNavbar';
import HostsNavbar from './HostsNavbar';
import MyHeader from '../components/HeaderController';
import { mobilePanelStyle } from '../components/dashboard/devices/styleComponents';

import { RemoteService } from '../remote';
import { appActions } from '../storeActions';

class Dashboard extends Component {
  constructor(props) {
    super(props);
    this.state = this.initialState;
    this.activeTab = 'Devices';
    this.selectTab = this.selectTab.bind(this);
  }

  get initialState() {
    return {
      activeTab: this.activeTab,
    };
  }

  setInitialState() {
    this.setState(this.initialState);
  }

  get activeTab() {
    return this.props.activeTab;
  }

  set activeTab(tab) {
    this.props.setActiveTab(tab);
  }

  selectTab(e, { name }) {
    this.setState({ activeTab: name });
    this.activeTab = name;
  }

  renderTab(tab) {
    switch (tab) {
      case 'Devices':
        return <DevicePanel tab={this.state.activeTab} />;
      case 'Scenes':
        return <ScenesPanel tab={this.state.activeTab} />;
      case 'Automations':
        return <AutomationsPanel />;
      case 'Hosts':
        return <HostsPanel />;
      default:
        return <h1>ERROR</h1>;
    }
  }

  renderNavbar(tab) {
    switch (tab) {
      case 'Devices':
        return <Navbar />;
      case 'Scenes':
        return <ScenesNavbar />;
      case 'Hosts':
        return <HostsNavbar />;
      default:
        return <h1>ERROR</h1>;
    }
  }

  get hasNavbar() {
    return this.state.activeTab !== 'Automations';
  }

  render() {
    // needed to correctly assign the background image
    // in case a room has one.
    let backgroundImageHelper;
    if (this.activeTab === 'Devices') {
      backgroundImageHelper = this.props.backgroundImage;
    } else {
      backgroundImageHelper = null;
    }
    // console.log("helper is",helper)
    return (
      <div style={{ background: '#1b1c1d', height: "102vh", overflowY: "unset"}}>
        <Responsive minWidth={768}>
          <Grid>
            <Grid.Row color="black">
              <Grid.Column>
                <MyHeader />
              </Grid.Column>
            </Grid.Row>
            <Grid.Row color="black" style={{ paddingTop: 0, height: "100%" }}>
              <Grid.Column textAlign="center" width={16}>
                <Menu fluid widths={4} inverted style={{backgroundColor: "rgb(80, 91, 218)"}}>
                  <Menu.Item
                    name="Devices"
                    content="Devices"
                    active={this.activeTab === 'Devices'}
                    onClick={this.selectTab}
                  />
                  <Menu.Item
                    name="Scenes"
                    content="Scenes"
                    active={this.activeTab === 'Scenes'}
                    onClick={this.selectTab}
                  />
                  <Menu.Item
                    name="Automations"
                    content="Automations"
                    active={this.activeTab === 'Automations'}
                    onClick={this.selectTab}
                  />
                  <Menu.Item
                    name="Hosts"
                    content="Hosts and Guests"
                    active={this.activeTab === 'Hosts'}
                    onClick={this.selectTab}
                  />
                </Menu>
              </Grid.Column>

              {this.hasNavbar && (
                <Grid.Column width={3}>
                  {this.renderNavbar(this.activeTab)}
                </Grid.Column>
              )}
              <Grid.Column width={this.hasNavbar ? 13 : 16}>
                <div
                  style={{
                    backgroundImage: `url(${backgroundImageHelper})`,
                    backgroundColor: '#fafafa',
                    height: '80vh',
                    padding: '0 3rem',
                    color: '#000000',
                    overflow: 'auto',
                    maxHeight: '80vh',
                  }}
                >
                  {this.renderTab(this.activeTab)}
                </div>
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </Responsive>
        <Responsive maxWidth={768}>
          <Grid inverted>
            <Grid.Row color="black">
              <Grid.Column>
                <MyHeader />
              </Grid.Column>
            </Grid.Row>
            <Grid.Row color="black">
              <Grid.Column textAlign="center">
                <Button
                  basic
                  name="Devices"
                  content="Devices"
                  active={this.activeTab === 'Devices'}
                  color={this.activeTab === 'Devices' ? 'yellow' : 'grey'}
                  onClick={this.selectTab}
                />
                <Button
                  basic
                  name="Scenes"
                  content="Scenes"
                  active={this.activeTab === 'Scenes'}
                  color={this.activeTab === 'Scenes' ? 'yellow' : 'grey'}
                  onClick={this.selectTab}
                />
                <Button
                  basic
                  name="Automations"
                  content="Automations"
                  active={this.activeTab === 'Automations'}
                  color={this.activeTab === 'Automations' ? 'yellow' : 'grey'}
                  onClick={this.selectTab}
                />
                <Button
                  basic
                  name="Hosts"
                  content="Hosts"
                  active={this.activeTab === 'Hosts'}
                  color={
                    this.activeTab === 'Hosts and Guests' ? 'yellow' : 'grey'
                  }
                  onClick={this.selectTab}
                />
              </Grid.Column>
            </Grid.Row>
            {this.hasNavbar && (
              <Grid.Row color="black">
                <Grid.Column color="black">
                  {this.renderNavbar(this.activeTab)}
                </Grid.Column>
              </Grid.Row>
            )}
            <Grid.Row>
              <Grid.Column>
                <div style={mobilePanelStyle}>
                  {this.renderTab(this.activeTab)}
                </div>
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </Responsive>
      </div>
    );
  }
}

const mapStateToProps = (state, _) => ({
  activeTab: state.active.activeTab,
  get currentRoom() {
    return state.active.activeRoom;
  },
  get backgroundImage() {
    if (state.active.activeRoom === -1) {
      return null;
    }
      return state.rooms[state.active.activeRoom].image;
  },
});

const setActiveTab = (activeTab) => (dispatch) => dispatch(appActions.setActiveTab(activeTab));

const DashboardContainer = connect(mapStateToProps, {
  ...RemoteService,
  setActiveTab,
})(Dashboard);
export default DashboardContainer;
