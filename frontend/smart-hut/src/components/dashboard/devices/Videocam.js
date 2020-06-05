// vim: set ts=2 sw=2 et tw=80:

import React, { Component } from 'react';
import { Grid, Checkbox } from 'semantic-ui-react';
import { connect } from 'react-redux';
import { StyledDivCamera } from './styleComponents';
import { RemoteService } from '../../../remote';
import { endpointURL } from '../../../endpoint';
import VideocamModal from './VideocamModal';
import mapStateToProps from '../../../deviceProps';

class Videocam extends Component {
  constructor(props) {
    super(props);
    this.state = { selectedVideo: undefined };

    this.setOnOff = this.setOnOff.bind(this);
  }

  openModal = () => {
    this.setState((state) => ({ selectedVideo: true }));
  };

  closeModal = () => {
    this.setState((state) => ({ selectedVideo: undefined }));
  };

  setOnOff(onOff) {
    const turn = onOff;
    if (this.props.tab === 'Devices' || this.props.tab === 'Hosts') {
      this.props
        .saveDevice({ ...this.props.device, on: turn })
        .then((res) => (turn ? this.refs.vidRef.play() : this.refs.vidRef.pause()))
        .catch((err) => console.error('videocamera update error', err));
    } else {
      this.props.updateState(
        { id: this.props.stateOrDevice.id, on: turn },
        this.props.stateOrDevice.kind,
      );
    }
  }

  get url() {
    return endpointURL() + this.props.device.path;
  }

  render() {
    const VideocamView = (
      <div>
        <StyledDivCamera>
          <div onClick={this.openModal}>
            <video ref="vidRef" autoPlay loop muted width="100%" height="auto">
              <source src={this.url} type="video/mp4" />
            </video>
          </div>
        </StyledDivCamera>
        <Grid columns="equal" padded>
          <Grid.Row textAlign="center">
            <Grid.Column>
              <VideocamModal
                selectedVideo={this.state.selectedVideo}
                closeModal={this.closeModal}
                url={this.url}
              />
            </Grid.Column>
          </Grid.Row>
          <Grid.Row>
            <Grid.Column>
              <Checkbox
                checked={this.props.stateOrDevice.on}
                toggle
                label="Turn on/off"
                onChange={(e, val) => this.setOnOff(val.checked)}
              />
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </div>
    );

    return <div>{VideocamView}</div>;
  }
}

const VideocamContainer = connect(mapStateToProps, RemoteService)(Videocam);
export default VideocamContainer;
