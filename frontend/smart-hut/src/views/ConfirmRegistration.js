import React, { Component } from 'react';
import Confirm from './Confirm';

const msg = "An E-mail has been sent to your address, confirm your registration by following the enclosed link. If you don't find the E-mail please check also the spam folder.";
export default class ConfirmRegistration extends Component {
  render() {
    return <Confirm msg={msg} />;
  }
}
