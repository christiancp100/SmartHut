import React, { Component } from 'react';
import {
 Grid, Button, Segment, Responsive, Image,
} from 'semantic-ui-react';
import { Link } from 'react-router-dom';
import MyHeader from '../components/HeaderController';

export default class FourOhFour extends Component {
  constructor(props) {
    super(props);
    const meme = [
      '1.jpeg',
      '2.jpeg',
      '3.png',
      '4.jpeg',
      '5.jpeg',
      '6.jpg',
      '7.jpg',
      '8.jpg',
      '9.jpeg',
      '10.jpg',
      '11.jpeg',
      '12.gif',
      '13.gif',
      '14.gif',
    ];
    const arrayNum = Math.floor(Math.random() * 13) + 1;
    const path = `img/room_404_meme/${meme[arrayNum]}`;
    this.state = { meme: path };
  }

  render() {
    return (
      <div style={{ height: '110vh', background: '#1b1c1d' }}>
        <Responsive minWidth={768}>
          <Grid>
            <Grid.Row color="black">
              <Grid.Column>
                <MyHeader logout={this.props.logout} />
              </Grid.Column>
            </Grid.Row>
            <Grid.Row color="black">
              <Grid.Column width={16}>
                <Segment inverted color="red">
                  <Grid>
                    <Grid.Row>
                      <Grid.Column textAlign="center">
                        <h1>404 Page Not Found</h1>
                      </Grid.Column>
                    </Grid.Row>
                    <Grid.Row>
                      <Grid.Column width={8}>
                        <Image centered src={this.state.meme} size="medium" />
                      </Grid.Column>
                      <Grid.Column width={8}>
                        <p>
                          Hey what are you doing here? Looks like you are lost,
                          this room does not exist. Maybe you were looking for
                          the kitchen, or the garage, or the bedroom, or your
                          love room... so don't wait here and let's go back to
                          our main room! ...or refresh this page some times...
                        </p>
                        <Button fluid inverted color="white">
                          <Link style={{ color: 'black' }} to="/">
                            Let's go back to our main room!
                          </Link>
                        </Button>
                      </Grid.Column>
                    </Grid.Row>
                  </Grid>
                </Segment>
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </Responsive>

        <Responsive maxWidth={768}>
          <Grid>
            <Grid.Row color="black">
              <Grid.Column>
                <MyHeader logout={this.props.logout} />
              </Grid.Column>
            </Grid.Row>
            <Grid.Row color="black">
              <Grid.Column width={16}>
                <Segment inverted color="red">
                  <Grid>
                    <Grid.Row>
                      <Grid.Column textAlign="center">
                        <h1>404 Page Not Found</h1>
                      </Grid.Column>
                    </Grid.Row>
                    <Grid.Row>
                      <Grid.Column width={16}>
                        <Image centered src={this.state.meme} size="medium" />
                      </Grid.Column>
                    </Grid.Row>
                    <Grid.Row>
                      <Grid.Column width={16}>
                        <p>
                          Hey what are you doing here? Looks like you are lost,
                          this room does not exist. Maybe you were looking for
                          the kitchen, or the garage, or the bedroom, or your
                          love room... so don't wait here and let's go back to
                          our main room! ...or refresh this page some times...
                        </p>
                        <Button fluid inverted color="white">
                          <Link style={{ color: 'black' }} to="/">
                            Let's go back to our main room!
                          </Link>
                        </Button>
                      </Grid.Column>
                    </Grid.Row>
                  </Grid>
                </Segment>
              </Grid.Column>
            </Grid.Row>
          </Grid>
        </Responsive>
      </div>
    );
  }
}
