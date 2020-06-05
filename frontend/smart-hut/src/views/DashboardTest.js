import React, { Component } from 'react';
import { Button } from 'semantic-ui-react';

export default class Dashboard extends Component {
  handleLogOut = (e) => {
    console.log(this.props);
    this.props.logout();
  };

  render() {
    return (
      <Button circular style={{ margin: '2em' }} onClick={this.handleLogOut}>
        Go Home
        {' '}
      </Button>
    );
  }
}
