/**
 Users can add dimmers, a particular kind of switch that can also modify the intensity level of a given light.
 There are two types of dimmers:
 • A dimmer with state stores a given intensity level and sets the light to that level. <-- StatefulDimmer
 • A dimmer without state can just increase or decrease the intensity of a light. <-- DefualtDimmer

 The user can change the state of a dimmer through an intuitive UI in SmartHut .
 * */

import React, { Component } from 'react';
import {
  CircularInput,
  CircularProgress,
  CircularThumb,
} from 'react-circular-input';
import { connect } from 'react-redux';
import {
  ButtonDimmerContainer,
  MinusPanel,
  PlusPanel,
  ThumbText,
} from './styleComponents';
import {
  CircularThumbStyle,
  KnobDimmerStyle,
  KnobProgress,
  textStyle,
  knobIcon,
  knobContainer,
} from './DimmerStyle';
import { RemoteService } from '../../../remote';
import mapStateToProps from '../../../deviceProps';

export class ButtonDimmerComponent extends Component {
  increaseIntensity = () => {
    this.props
      .buttonDimmerDim(this.props.id, 'UP')
      .catch((err) => console.error('button dimmer increase error', err));
  };

  decreaseIntensity = () => {
    this.props
      .buttonDimmerDim(this.props.id, 'DOWN')
      .catch((err) => console.error('button dimmer decrease error', err));
  };

  render() {
    return (
      <ButtonDimmerContainer>
        <img alt="icon" src="/img/buttonDimmer.svg" />
        <span className="knob">Button Dimmer</span>
        <PlusPanel name="UP" onClick={this.increaseIntensity}>
          <span>&#43;</span>
        </PlusPanel>
        <MinusPanel name="DOWN" onClick={this.decreaseIntensity}>
          <span>&minus;</span>
        </MinusPanel>
      </ButtonDimmerContainer>
    );
  }
}

export class KnobDimmerComponent extends Component {
  constructor(props) {
    super(props);

    this.state = {
      intensity: this.props.stateOrDevice.intensity || 0,
      timeout: null,
    };

    this.saveIntensity = this.saveIntensity.bind(this);
    this.setIntensity = this.setIntensity.bind(this);
  }

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

  saveIntensity() {
    const val = Math.round(this.state.intensity);
    this.props
      .knobDimmerDimTo(this.props.id, val)
      .catch((err) => console.error('knob dimmer set intensity error', err));
  }

  render() {
    return (
      <div style={knobContainer}>
        <CircularInput
          style={KnobDimmerStyle}
          value={+(`${Math.round(`${this.state.intensity / 100}e+2`)}e-2`)}
          onChange={this.props.disabled ? null : this.setIntensity}
        >
          <text
            style={textStyle}
            x={100}
            y={120}
            textAnchor="middle"
            dy="0.3em"
            fontWeight="bold"
          >
            Knob Dimmer
          </text>
          <CircularProgress
            style={{ ...KnobProgress, opacity: this.state.intensity + 0.1 }}
          />
          <CircularThumb style={CircularThumbStyle} />
          <ThumbText color="#1a2849" />
        </CircularInput>
        <img alt="Knob Icon" style={knobIcon} src="/img/knobDimmer.svg" />
      </div>
    );
  }
}

const conn = connect(mapStateToProps, RemoteService);

export const KnobDimmer = conn(KnobDimmerComponent);
export const ButtonDimmer = conn(ButtonDimmerComponent);
