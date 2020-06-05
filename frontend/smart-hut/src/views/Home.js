import PropTypes from 'prop-types';
import React, { Component } from 'react';
import {
  Button,
  Container,
  Grid,
  Header,
  Icon,
  Image,
  List,
  Menu,
  Responsive,
  Segment,
  Sidebar,
  Visibility,
} from 'semantic-ui-react';
import HomeNavbar from '../components/HomeNavbar';

// Heads up!
// We using React Static to prerender our docs with server side rendering, this is a quite simple solution.
// For more advanced usage please check Responsive docs under the "Usage" section.
const getWidth = () => {
  const isSSR = typeof window === 'undefined';
  return isSSR ? Responsive.onlyTablet.minWidth : window.innerWidth;
};

/* eslint-disable react/no-multi-comp */
/* Heads up! HomepageHeading uses inline styling, however it's not the best practice. Use CSS or styled components for
 * such things.
 */
const HomepageHeading = ({ mobile }) => (
  <Container text>
    <Header
      as="h1"
      content="SmartHut"
      inverted
      style={{
        fontSize: mobile ? '2em' : '4em',
        fontWeight: 'normal',
        marginBottom: 0,
        marginTop: mobile ? '1.5em' : '3em',
      }}
    />
    <Header
      as="h2"
      content="Keep your Home fully Connected"
      inverted
      style={{
        fontSize: mobile ? '1.5em' : '1.7em',
        fontWeight: 'normal',
        marginTop: mobile ? '0.5em' : '1.5em',
      }}
    />
    <Button size="huge" color="orange" href="/signup">
      Get Started
      <Icon name="right arrow" />
    </Button>
  </Container>
);

HomepageHeading.propTypes = {
  mobile: PropTypes.bool,
};

class DesktopContainer extends Component {
  state = {};

  hideFixedMenu = () => this.setState({ fixed: false });

  showFixedMenu = () => this.setState({ fixed: true });

  render() {
    const { children } = this.props;

    return (
      <Responsive getWidth={getWidth} minWidth={Responsive.onlyTablet.minWidth}>
        <Visibility
          once={false}
          onBottomPassed={this.showFixedMenu}
          onBottomPassedReverse={this.hideFixedMenu}
        >
          <Segment
            basic
            textAlign="center"
            style={{
              minHeight: 700,
              padding: '1em 0em',
              background: ' linear-gradient(to bottom, rgba(0, 46, 200, 0.51), rgba(0, 0, 0, 0.51)), url("img/header.jpg")',
              backgroundSize: 'cover',
            }}
            vertical
          >
            <HomeNavbar />
            <HomepageHeading />
          </Segment>
        </Visibility>

        {children}
      </Responsive>
    );
  }
}

DesktopContainer.propTypes = {
  children: PropTypes.node,
};

class MobileContainer extends Component {
  state = {};

  handleSidebarHide = () => this.setState({ sidebarOpened: false });

  handleToggle = () => this.setState({ sidebarOpened: true });

  render() {
    const { children } = this.props;
    const { sidebarOpened } = this.state;

    return (
      <Responsive
        as={Sidebar.Pushable}
        getWidth={getWidth}
        maxWidth={Responsive.onlyMobile.maxWidth}
      >
        <Sidebar
          as={Menu}
          animation="push"
          inverted
          onHide={this.handleSidebarHide}
          vertical
          visible={sidebarOpened}
        >
          <Menu.Item as="a" active>
            Home
          </Menu.Item>
          <Menu.Item as="a">Log in</Menu.Item>
          <Menu.Item as="a">Sign Up</Menu.Item>
        </Sidebar>

        <Sidebar.Pusher dimmed={sidebarOpened}>
          <Segment
            inverted
            textAlign="center"
            style={{ minHeight: 350, padding: '1em 0em' }}
            vertical
          >
            <Container>
              <Menu inverted pointing secondary size="large">
                <Menu.Item onClick={this.handleToggle}>
                  <Icon name="sidebar" />
                </Menu.Item>
                <Menu.Item position="right">
                  <Button as="a" inverted>
                    Log in
                  </Button>
                  <Button as="a" inverted style={{ marginLeft: '0.5em' }}>
                    Sign Up
                  </Button>
                </Menu.Item>
              </Menu>
            </Container>
            <HomepageHeading mobile />
          </Segment>

          {children}
        </Sidebar.Pusher>
      </Responsive>
    );
  }
}

MobileContainer.propTypes = {
  children: PropTypes.node,
};

const ResponsiveContainer = ({ children }) => (
  <div>
    <DesktopContainer>{children}</DesktopContainer>
    <MobileContainer>{children}</MobileContainer>
  </div>
);

ResponsiveContainer.propTypes = {
  children: PropTypes.node,
};

const Home = () => (
  <ResponsiveContainer>
    <Segment style={{ padding: '8em 0em' }} vertical>
      <Grid container stackable verticalAlign="middle">
        <Grid.Row>
          <Grid.Column width={8}>
            <Header as="h3" style={{ fontSize: '2em' }}>
              We help you keep your home connected
            </Header>
            <p style={{ fontSize: '1.33em' }}>
              In a few steps your home will be fully connected with SmartHut.
            </p>
            <Header as="h3" style={{ fontSize: '2em' }}>
              Choose between a wide range of devices
            </Header>
            <p style={{ fontSize: '1.33em' }}>
              SmartHut is a leading worldwide company in technology innovation.
              Explore our website to find the best devices for each room of your
              home!
            </p>
          </Grid.Column>
          <Grid.Column floated="right" width={6}>
            <Image bordered rounded size="large" src="/img/banner.jpg" />
          </Grid.Column>
        </Grid.Row>
      </Grid>
    </Segment>

    <Segment style={{ padding: '8em 0em' }} vertical>
      <Container text>
        <Header as="h3" style={{ fontSize: '2em' }}>
          Have you ever dreamt about a smart home?
        </Header>
        <p style={{ fontSize: '1.33em' }}>
          Let us carrying you into the future. With SmartHut, being at home will
          be a refreshing experience. With just a few clicks, you will be able
          the set the illumination of your entire place. Follow the intelligent
          light, follow SmartHut.
        </p>
      </Container>
    </Segment>

    <Segment inverted vertical style={{ padding: '5em 0em' }}>
      <Container>
        <Grid divided inverted stackable>
          <Grid.Row>
            <Grid.Column width={3}>
              <Header inverted as="h4" content="About" />
              <List link inverted>
                <List.Item>
                  <a href="https://usi.ch" target="blank">
                    Usi
                  </a>
                </List.Item>
                <List.Item>
                  <a href="https://vimtok.com" target="blank">
                    VimTok
                  </a>
                </List.Item>
                <List.Item>
                  <a href="https://legendofajax.fun" target="blank">
                    Legend of Ajax
                  </a>
                </List.Item>
                <List.Item>
                  <a href="https://www.theshell.ch" target="blank">
                    The Shell
                  </a>
                </List.Item>
              </List>
            </Grid.Column>
            <Grid.Column width={3}>
              <Header inverted as="h4" content="Services" />
              <List link inverted>
                <List.Item>
                  <a href="https://semantic-ui.com/" target="blank">
                    Semantic UI
                  </a>
                </List.Item>
                <List.Item>
                  <a
                    href="https://worldmeters.info/coronavirus/"
                    target="blank"
                  >
                    Stay Safe
                  </a>
                </List.Item>
                <List.Item>
                  <a
                    href="http://hasthelargehadroncolliderdestroyedtheworldyet.com/"
                    target="blank"
                  >
                    Stay Home
                  </a>
                </List.Item>
                <List.Item>
                  <a href="http://Howmuchtoiletpaper.com/" target="blank">
                    Can you Survive?
                  </a>
                </List.Item>
              </List>
            </Grid.Column>
            <Grid.Column width={7}>
              <Header as="h4" inverted>
                SmartHut
              </Header>
              <p>Keep your Home connected.</p>
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </Container>
    </Segment>
  </ResponsiveContainer>
);

export default Home;
