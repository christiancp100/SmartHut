import React, { Component } from 'react';
import {
  Button,
  Form,
  Grid,
  Header,
  Image,
  Icon,
  Message,
} from 'semantic-ui-react';
import { Redirect } from 'react-router-dom';
import { Forms } from '../remote';

export default class ForgotPass extends Component {
  constructor(props) {
    super(props);
    this.state = {
      user: '',
      error: {
        state: false,
        message: [],
      },
      success: false,
    };

    this.handleChangePassword = this.handleChangePassword.bind(this);
  }

  onChangeHandler = (event) => {
    const nam = event.target.name;
    const val = event.target.value;
    this.setState({ [nam]: val });
  };

  handleSendEmail = (e) => {
    e.preventDefault();

    Forms.submitInitResetPassword(this.state.user)
      .then(() => this.setState({ success: true }))
      .catch((err) => this.setState({ error: { state: true, message: err.messages } }));
  };

  handleChangePassword = (e) => {
    if (this.state.confirmPassword !== this.state.password) {
      this.setState({
        error: {
          state: true,
          message: 'Passwords do not match.',
        },
      });
    }

    Forms.submitResetPassword(this.props.query.token, this.state.password)
      .then(() => this.setState({ success: true }))
      .catch((err) => this.setState({
          error: { state: true, message: err.messages.join(' - ') },
        }));
  };

  render() {
    console.log(this.props);
    if (this.state.success) {
      return <Redirect to="sent-email" />;
    }
    return (
      <>
        <Button circular style={{ margin: '2em' }} href="/">
          <Icon name="arrow alternate circle left" />
          Go Home
          {' '}
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
Reset Password
            </Header>
            <Form
              size="large"
              style={{ marginTop: '2em' }}
              error={this.state.error.state}
            >
              <Message error>
                <Message.Header>Reset Password Error</Message.Header>

                {this.state.error.message.map((e, i) => (
                  <span key={i}>
                    {e}
                    <br />
                  </span>
                ))}
              </Message>
              {this.props.type === 'FPassword1' ? (
                <>
                  <Form.Input
                    icon="address card outline"
                    iconPosition="left"
                    placeholder="Enter your E-mail"
                    name="user"
                    type="text"
                    onChange={this.onChangeHandler}
                    required
                  />
                  <Button
                    color="blue"
                    fluid
                    size="large"
                    onClick={this.handleSendEmail}
                  >
                    Send E-mail
                  </Button>
                </>
              ) : (
                <>
                  <Form.Input
                    icon="address card outline"
                    iconPosition="left"
                    placeholder="Reset your password"
                    name="password"
                    type="password"
                    onChange={this.onChangeHandler}
                    required
                  />
                  <Form.Input
                    icon="address card outline"
                    iconPosition="left"
                    placeholder="Confirm Password"
                    name="confirmPassword"
                    type="password"
                    onChange={this.onChangeHandler}
                    required
                  />
                  <Button
                    color="blue"
                    fluid
                    size="large"
                    onClick={this.handleChangePassword}
                  >
                    Confirm password
                  </Button>
                </>
              )}
            </Form>
          </Grid.Column>
        </Grid>
      </>
    );
  }
}
