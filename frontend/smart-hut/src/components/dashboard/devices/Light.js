// vim: set ts=2 sw=2 et tw=80:

/**
 * Users can add lights in their rooms.
 * Lights are devices like bulbs, LED strip lights, lamps.
 * Lights may support an intensity level (from 0% to 100%).
 * Lights have an internal state that can be changed and it must
 * be shown accordingly in the SmartHut views (house view and room views).
 */
import React, { Component } from 'react';
import { Image } from 'semantic-ui-react';
import {
  CircularInput,
  CircularProgress,
  CircularThumb,
} from 'react-circular-input';
import { connect } from 'react-redux';
import {
  iconStyle,
  StyledDiv,
  BottomPanel,
  ThumbText,
} from './styleComponents';
import {
  LightDimmerContainer,
  LightDimmerStyle,
  textStyle,
  nameStyle,
  KnobProgress,
  CircularThumbStyle,
  knobIcon,
} from './LightStyle';
import { RemoteService } from '../../../remote';
import mapStateToProps from '../../../deviceProps';

class Light extends Component {
  constructor(props) {
    super(props);
    this.state = {
      intensity: this.props.stateOrDevice.intensity,
      timeout: null,
    };

    this.iconOn = '/img/lightOn.svg';
    this.iconOff = '/img/lightOff.svg';

    this.setIntensity = this.setIntensity.bind(this);
  }

  componentDidUpdate(prevProps) {
    if (
      this.props.stateOrDevice.intensity !== prevProps.stateOrDevice.intensity
    ) {
      this.setState({
        intensity: this.props.stateOrDevice.intensity,
        timeout: null,
      });
    }
  }

  get turnedOn() {
    return this.props.stateOrDevice.on;
  }

  get intensity() {
    return this.state.intensity || 0;
  }

  onClickDevice = () => {
    const on = !this.turnedOn;
    if (this.props.tab !== 'Scenes') {
      this.props
        .saveDevice(
          { ...this.props.stateOrDevice, on },
          this.props.tab === 'Hosts' ? this.props.activeHost : null,
        )
        .catch((err) => console.error('regular light update error', err));
    } else if (this.props.device.kind === 'regularLight') {
        this.props
          .updateState(
            {
              id: this.props.stateOrDevice.id,
              on,
              sceneId: this.props.stateOrDevice.sceneId,
            },
            this.props.stateOrDevice.kind,
          )
          .then((res) => {
            console.log(res);
          });
      }
  };

  getIcon = () => (this.turnedOn ? this.iconOn : this.iconOff);

  setIntensity(intensity) {
    intensity *= 100;

    if (this.state.timeout) {
      clearTimeout(this.state.timeout);
    }

    this.setState({
      intensity,
      timeout: setTimeout(() => {
        this.saveIntensity();
        this.setState({
          intensity: this.state.intensity,
          timeout: null,
        });
      }, 100),
    });
  }

  saveIntensity = () => {
    const intensity = Math.round(this.state.intensity);
    if (this.props.tab !== 'Scenes') {
      this.props
        .saveDevice(
          { ...this.props.stateOrDevice, intensity },
          this.props.tab === 'Hosts' ? this.props.activeHost : null,
        )
        .catch((err) => console.error('dimmable light update error', err));
    } else {
      this.props
        .updateState(
          { id: this.props.stateOrDevice.id, intensity },
          this.props.stateOrDevice.kind,
        )
        .then((res) => {
          console.log(res, this.props.stateOrDevice.kind);
        });
    }
  };

  render() {
    const intensityLightView = (
      <div style={LightDimmerContainer}>
        <CircularInput
          style={LightDimmerStyle}
          value={+(`${Math.round(`${this.intensity / 100}e+2`)}e-2`)}
          onChange={this.props.disabled ? null : this.setIntensity}
          onMouseUp={this.props.disabled ? null : this.saveIntensity}
        >
          <text
            style={textStyle}
            x={100}
            y={120}
            textAnchor="middle"
            dy="0.3em"
            fontWeight="bold"
          >
            Intensity light
          </text>
          <CircularProgress
            style={{
              ...KnobProgress,
              opacity: this.intensity / 100 + 0.3,
            }}
          />
          <CircularThumb style={CircularThumbStyle} />
          <ThumbText color="#ffd31d" />
        </CircularInput>
        <Image style={knobIcon} src="/img/intensityLightIcon.svg" />
      </div>
    );

    const normalLightView = (
      <StyledDiv onClick={this.onClickDevice}>
        <div>
          <Image src={this.getIcon()} style={iconStyle} />
          <BottomPanel style={{ backgroundColor: '#ffa41b' }}>
            <h5 style={nameStyle}>Light</h5>
          </BottomPanel>
        </div>
      </StyledDiv>
    );

    return (
      <div>
        {this.props.device.kind === 'dimmableLight'
          ? intensityLightView
          : normalLightView}
      </div>
    );
  }
}

const LightContainer = connect(mapStateToProps, RemoteService)(Light);
export default LightContainer;
