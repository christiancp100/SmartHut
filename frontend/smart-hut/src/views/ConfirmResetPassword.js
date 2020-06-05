import React, { Component } from 'react';
import Confirm from './Confirm';

const msg = 'Your password has been successfully reset.';

export default class ConfirmResetPassword extends Component {
  render() {
    return <Confirm msg={msg} />;
  }
}
