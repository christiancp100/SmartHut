import React, { Component } from 'react';
import { Button, Grid } from 'semantic-ui-react';

export default class SelectIcons extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentIcon: this.props.currentIcon,
    };
  }

  selectIcon = (e) => {
    let el = e.target.name;
    if (e.target.tagName === 'I') {
      el = e.target.parentNode.name;
    }
    this.props.updateIcon(el);
    this.setState({ currentIcon: el });
  };

  render() {
    const myicons = [
      ['home', 'coffee', 'beer', 'glass martini', 'film', 'video'],
      ['music', 'headphones', 'fax', 'phone', 'laptop', 'bath'],
      ['shower', 'bed', 'child', 'warehouse', 'car', 'bicycle'],
      ['motorcycle', 'archive', 'boxes', 'cubes', 'chess', 'gamepad'],
      ['futbol', 'table tennis', 'server', 'tv', 'heart', 'camera'],
      ['trophy', 'wrench', 'image', 'book', 'university', 'medkit'],
      ['paw', 'tree', 'utensils', 'male', 'female', 'life ring outline'],
    ];

    return (
      <Grid centered relaxed>
        {myicons.map((e, i) => (
          <Grid.Row key={i}>
            {e.map((e, i) => (
              <Grid.Column key={i}>
                <Button
                  name={e}
                  color={e === this.state.currentIcon ? 'black' : null}
                  icon={e}
                  size="small"
                  onClick={this.selectIcon}
                />
              </Grid.Column>
                ))}
          </Grid.Row>
          ))}
      </Grid>
    );
  }
}
