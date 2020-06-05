import React, { Component } from 'react';
import {
 BrowserRouter, Switch, Route, Redirect,
} from 'react-router-dom';
import queryString from 'query-string';
import { connect } from 'react-redux';
import Home from './views/Home';
import Dashboard from './views/Dashboard';
import Signup from './views/Signup';
import Login from './views/Login';
import FourOhFour from './views/FourOhFour';
import ForgotPass from './views/Forgot-password';
import ConfirmForgotPasswrod from './views/ConfirmForgotPassword';
import ConfirmRegistration from './views/ConfirmRegistration';
import ConfirmResetPassword from './views/ConfirmResetPassword';
import Instruction from './views/Instruction';
import Videocam from './views/Videocam';
import { RemoteService } from './remote';

class App extends Component {
  constructor(props, context) {
    super(props, context);

    this.state = {
      query: '',
      info: '',
    };
  }

  componentDidMount() {
    if (window.location) {
      const values = queryString.parse(window.location.search);
      this.setState({
        query: values,
      });
    }
  }

  render() {
    console.log('rendering root', this.props.loggedIn, this.state.query);
    return (
      <BrowserRouter>
        <Switch>
          <Route path="/" exact component={Home} />
          <Route path="/login">
            {this.props.loggedIn ? <Redirect to="/dashboard" /> : <Login />}
          </Route>
          <Route path="/signup" exact component={Signup} />
          <Route path="/dashboard">
            {this.props.loggedIn ? <Dashboard /> : <Redirect to="/login" />}
          </Route>
          <Route path="/forgot-password">
            <ForgotPass type="FPassword1" />
          </Route>
          <Route path="/sent-email">
            <ConfirmForgotPasswrod />
          </Route>
          <Route path="/sent-email-reg">
            <ConfirmRegistration />
          </Route>
          <Route path="/instruction">
            <Instruction />
          </Route>
          <Route path="/forgot-pass-reset">
            <ForgotPass type="FPassword1" />
          </Route>
          <Route path="/password-reset">
            <ForgotPass type="FPassword2" query={this.state.query} />
          </Route>
          <Route path="/conf-reset-pass">
            <ConfirmResetPassword />
          </Route>
          <Route path="/videocam">
            <Videocam />
          </Route>
          <Route component={FourOhFour} />
        </Switch>
      </BrowserRouter>
    );
  }
}

const mapStateToProps = (state, _) => ({ loggedIn: state.login.loggedIn });
const AppContainer = connect(mapStateToProps, RemoteService)(App);
export default AppContainer;
