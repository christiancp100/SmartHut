import styled from 'styled-components';
import { useCircularInputContext } from 'react-circular-input';
import React from 'react';
import { ValueStyle } from './DimmerStyle';

export const editButtonStyle = {
  position: 'absolute',
  top: '0',
  right: '0',
  backgroundColor: '#505bda',
  borderRadius: '0 0 0 20px',
  border: 'none',
  padding: '.4rem 1.2rem',
  outline: 'none',
  color: 'white',
  fontFamily: 'Lato',
  textTransform: 'uppercase',
};

export const panelStyle = {
  backgroundColor: '#fafafa',
  height: '85vh',
  padding: '0rem 3rem',
  color: '#000000',
  overflow: 'auto',
  maxHeight: '75vh',
};

export const mobilePanelStyle = {
  backgroundColor: '#fafafa',
  minHeight: '100vh',
  padding: '0rem 3rem',
  color: '#000000',
};

export const editModeStyle = {
  position: 'absolute',
  top: '15%',
  right: '0',
  width: '1.5rem',
  height: '1.5rem',
  backgroundColor: 'black',
  borderRadius: '100%',
  zIndex: '1000',
  cursor: 'pointer',
};

export const editModeStyleLeft = {
  position: 'absolute',
  top: '15%',
  left: '0',
  width: '1.5rem',
  height: '1.5rem',
  backgroundColor: 'white',
  borderRadius: '100%',
  zIndex: '1000',
  cursor: 'pointer',
};

export const editModeIconStyle = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '0.75rem',
  height: '0.75rem',
  borderRadius: '20%',
  zIndex: '101',
};

export const iconStyle = {
  width: '3.5rem',
  height: 'auto',
  position: 'absolute',
  top: '10%',
  left: '50%',
  transform: 'translateX(-50%)',
  userSelect: 'none',
};
export const nameStyle = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translateX(-50%)',
};

export const formStyle = {
  position: 'absolute',
  zIndex: '1000',
  width: '80rem',
  height: '10rem',
  padding: '1rem',
  margin: '1rem',
  borderRadius: '10%',
  boxShadow: '1px 1px 5px 2px #5d5d5d',
  backgroundColor: '#3e99ff',
};

export const addDeviceFormStyle = {
  maxWidth: '400px',
  background: '#3e99ff',
  paddingRight: '5rem',
};

export const StyledDiv = styled.div`
  cursor: pointer;
  background-color: white;
  padding: 3rem;
  width: 10rem;
  height: 10rem;
  border-radius: 100%;
  border: none;
  position: relative;
  box-shadow: 3px 2px 10px 5px #ccc;
  transition: all 0.3s ease-out;
  text-align: center;
  :hover {
    background-color: #f2f2f2;
  }
  :active {
    transform: translate(0.3px, 0.8px);
    box-shadow: 0.5px 0.5px 7px 3.5px #ccc;
  }
`;

export const StyledDivCamera = styled.div`
  cursor: pointer;
  background-color: white;
  padding: 0.5rem;
  margin-bottom: 0.5rem;
  border: none;
  position: relative;
  box-shadow: 3px 2px 10px 5px #ccc;
  transition: all 0.3s ease-out;
  text-align: center;
  :hover {
    background-color: #f2f2f2;
  }
  :active {
    transform: translate(0.3px, 0.8px);
    box-shadow: 0.5px 0.5px 7px 3.5px #ccc;
  }
`;

export const ButtonDimmerContainer = styled.div`
  background-color: white;
  padding: 3rem;
  width: 10rem;
  height: 10rem;
  border-radius: 100%;
  border: none;
  position: relative;
  box-shadow: 3px 2px 10px 5px #ccc;
  transition: all 0.3s ease-out;
  text-align: center;
  display: inline-block;
  .knob {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    width: 10rem;
    color: #1a2849;
  }
  img {
    position: absolute;
    top: 10%;
    left: 50%;
    transform: translateX(-50%);
    width: 1.5rem;
    height: 1.5rem;
  }
`;

export const PlusPanel = styled.div`
  position: absolute;
  cursor: pointer;
  bottom: 0;
  left: 5rem;
  background-color: #1a2849;
  width: 5rem;
  height: 5rem;
  border-radius: 0 0 5rem 0;
  :hover {
    background-color: #505bda;
  }
  :active {
    transform: translate(0.3px, 0.8px);
  }
  span {
    color: white;
    position: absolute;
    top: 40%;
    left: 45%;
    font-size: 3rem;
    transform: translate(-50%, -50%);
  }
`;

export const MinusPanel = styled.div`
  cursor: pointer;
  position: absolute;
  bottom: 0;
  left: 0;
  background-color: #1a2849;
  width: 5rem;
  height: 5rem;
  border-radius: 0 0 0 5rem;
  :hover {
    background-color: #505bda;
  }
  :active {
    transform: translate(0.3px, 0.8px);
  }
  span {
    color: white;
    position: absolute;
    top: 40%;
    left: 45%;
    font-size: 3rem;
    transform: translate(-50%, -50%);
  }
`;

export const BottomPanel = styled.div`
  position: absolute;
  cursor: pointer;
  bottom: 0;
  left: 0rem;
  width: 10rem;
  height: 5rem;
  border-bottom-left-radius: 10rem;
  border-bottom-right-radius: 10rem;
  span {
    color: white;
    position: absolute;
    top: 40%;
    left: 45%;
    font-size: 3rem;
    transform: translate(-50%, -50%);
  }
`;

export const ThumbText = (props) => {
  const { getPointFromValue, value } = useCircularInputContext();
  const { x, y } = getPointFromValue();

  return (
    <text style={{ ...ValueStyle, fill: props.color }} x={x} y={y + 5}>
      {Math.round(value * 100)}
    </text>
  );
};
