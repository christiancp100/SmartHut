import React from 'react';
import {
  Grid,
  Divider,
  Button,
  Label,
  Responsive,
  Checkbox,
  Segment, Image, Icon,
} from 'semantic-ui-react';

import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { RemoteService } from '../remote';

const IconHomeImage = () => (
  <Image
    src="smart-home.png"
    style={{ width: '50px', height: 'auto' }}
    centered
    as="a"
    href="/"
  />
);

const TitleImage = () => <Image src="sm_logo.png" size="medium"  />;

export class MyHeader extends React.Component {
  constructor(props) {
    super(props);

    this.getInfo();
    this.logout = this.logout.bind(this);
  }

  logout() {
    this.props.logout().then(() => this.props.history.push('/'));
  }

  getInfo() {
    this.props
      .fetchUserInfo()
      .catch((err) => console.error('MyHeader fetch user info error', err));
  }

  setCameraEnabled(val) {
    const enabled = {
      cameraEnabled: val,
    };
    this.props
      .userPermissions(enabled)
      .then(() => this.getInfo())
      .catch((err) => console.error('Camera enabled', err));
  }

  render() {
    return (
      <div>
        <Responsive minWidth={768}>
          <Grid columns="equal" inverted padded>
            <Grid.Row color="black" textAlign="center">
              <Grid.Column width={10} floated={"left"}>
                  <TitleImage />
              </Grid.Column>
              <Grid.Column width={2} style={{marginTop: "2rem"}}>
                <Label color="yellow">
                  <Checkbox
                    label={<label>Share cameras</label>}
                    checked={this.props.cameraEnabled}
                    toggle
                    onChange={(e, val) => this.setCameraEnabled(val.checked)}
                  />
                </Label>
              </Grid.Column>
              <Grid.Column width={2} style={{marginTop: "2rem"}}>
                <Label as="a" image color="black">
                  <Icon name="user" size={"big"}/>
                  {this.props.username}
                </Label>
              </Grid.Column>
              <Grid.Column width={2} style={{marginTop: "2rem"}}>
                <Button icon labelPosition="right" inverted onClick={this.logout}>
                  Logout
                  <Icon name="log out"/>
                </Button>
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </Responsive>
        <Responsive maxWidth={768}>
          <Grid columns="equal" inverted padded>
            <Grid.Row color="black" textAlign="center">
              <Segment color="black" inverted>
                <TitleImage />
              </Segment>
            </Grid.Row>
            <Grid.Row color="black" textAlign="center">
              <Grid.Column>
                <IconHomeImage />
              </Grid.Column>
              <Grid.Column>
                <Label as="a" image color="black">
                  <img alt="SmartHut logo" src="smart-home.png" />
                  {this.props.username}
                </Label>
                <Divider />
                <Button onClick={this.logout}>Logout</Button>
                <Segment
                  compact
                  style={{
                    margin: 'auto',
                    marginTop: '1em',
                    textAlign: 'center',
                  }}
                >
                  <Checkbox
                    label={<label>Share cameras</label>}
                    checked={this.props.cameraEnabled}
                    toggle
                    onChange={(e, val) => this.setCameraEnabled(val.checked)}
                  />
                </Segment>
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </Responsive>
      </div>
    );
  }
}

const mapStateToProps = (state, _) => ({
  username:
    state.userInfo && state.userInfo.username ? state.userInfo.username : '',
  cameraEnabled: state.userInfo ? state.userInfo.cameraEnabled : false,
});
const LoginContainer = connect(
  mapStateToProps,
  RemoteService,
)(withRouter(MyHeader));
export default LoginContainer;
