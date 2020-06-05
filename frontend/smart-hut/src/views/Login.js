import React, { Component } from 'react';
import {
  Button,
  Form,
  Grid,
  Header,
  Image,
  Message,
  Icon,
  Input,
} from 'semantic-ui-react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import { RemoteService } from '../remote';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      user: '',
      password: '',
      fireRedirect: false,
      error: { state: false, message: '' },
    };
  }

  handleLogin = (e) => {
    e.preventDefault();

    this.props
      .login(this.state.user, this.state.password)
      .then(() => this.props.history.push('/dashboard'))
      .catch((err) => {
        this.setState({
          error: { state: true, message: err.messages.join(' - ') },
        });
      });
  };

  onChangeHandler = (event) => {
    const nam = event.target.name;
    const val = event.target.value;
    this.setState({ [nam]: val });
  };

  toggle = () => this.setState((prevState) => ({ rememberme: !prevState.rememberme }));

  render() {
    return (
      <>
        <Button circular style={{ margin: '2em' }} href="/">
          <Icon name="arrow alternate circle left" />
          Go Home
        </Button>
        <Grid
          textAlign="center"
          style={{ height: '70vh' }}
          verticalAlign="middle"
        >
          <Grid.Column style={{ maxWidth: 450 }}>
            <Header as="h2" color="blue" textAlign="center">
              <Image src="img/logo.png" />
              {' '}
Log-in to SmartHut
            </Header>
            <Form
              size="large"
              style={{ marginTop: '2em' }}
              error={this.state.error.state}
            >
              <Message
                error
                header="Login Error"
                content={this.state.error.message}
              />
              <Form.Input
                control={Input}
                type="text"
                icon="user"
                name="user"
                iconPosition="left"
                placeholder="Username or E-mail"
                onChange={this.onChangeHandler}
              />
              <Form.Input
                icon="lock"
                iconPosition="left"
                placeholder="Password"
                name="password"
                type="password"
                onChange={this.onChangeHandler}
              />

              <Button
                color="blue"
                fluid
                size="large"
                onClick={this.handleLogin}
              >
                Login
              </Button>
            </Form>
            <Message>
              <p>
                <a href="/forgot-password">Forgot Password?</a>
              </p>
              <p>
                New to us?
                {' '}
                <a href="/signup"> Sign Up</a>
              </p>
            </Message>
          </Grid.Column>
        </Grid>
      </>
    );
  }
}

const mapStateToProps = (state, _) => ({ loggedIn: state.login.loggedIn });
const LoginContainer = connect(
  mapStateToProps,
  RemoteService,
)(withRouter(Login));
export default LoginContainer;
