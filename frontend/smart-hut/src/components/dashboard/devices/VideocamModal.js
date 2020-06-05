import React from 'react';
import Modal from 'react-modal';
import { Button } from 'semantic-ui-react';

const modal = {
  opacity: 0,
  alignItems: 'center',
  display: 'flex',
  justifyContent: 'center',
  transition: 'opacity 200ms ease-in-out',
  background: 'grey',
  color: 'white',
  maxWidth: '2rem',
  outline: 'none',
  padding: '2rem',
  textAlign: 'center',
  maxHeight: '50vh',
};

const VideocamModal = (props) => (
  <Modal
    isOpen={!!props.selectedVideo}
    contentLabel="Live Cam"
    onRequestClose={props.closeModal}
    style={modal}
  >
    {props.selectedVideo && (
      <video autoPlay loop muted width="100%" height="90%">
        <source src={props.url} type="video/mp4" />
      </video>
    )}
    <Button fluid primary onClick={props.closeModal}>
      Close
    </Button>
  </Modal>
);

export default VideocamModal;
