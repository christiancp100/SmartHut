import React, { Component } from 'react';
import {
  Image,
  Grid,
  Button,
  Icon,
  Header,
  Container,
} from 'semantic-ui-react';

export default class Confirm extends Component {
  render() {
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
Congratulation!
            </Header>
            <Container textAlign="center">
              <p>{this.props.msg}</p>
            </Container>
          </Grid.Column>
        </Grid>
      </>
    );
  }
}
