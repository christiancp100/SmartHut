import React, { Component, useState } from 'react';
import {
 Button, Form, Icon, Header, Modal, Input,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import { RemoteService } from '../../../remote';

const DeleteModal = (props) => (
  <Modal
    trigger={(
      <Button icon labelPosition="left" inverted color="red">
        <Icon name="trash alternate" />
        Delete device
      </Button>
    )}
    closeIcon
  >
    <Header icon="archive" content="Are you sure ?" />
    <Modal.Actions>
      <Button color="red">
        <Icon name="remove" />
        {' '}
No
      </Button>
      <Button onClick={() => props.removeDevice()} color="green">
        <Icon name="checkmark" />
        {' '}
Yes
      </Button>
    </Modal.Actions>
  </Modal>
);

const SettingsForm = (props) => {
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setValues({ ...values, [name]: value });
  };

  const [values, setValues] = useState({ name: '' });

  return (
    <Form>
      <Form.Field>
        <label>Edit Name: </label>
        <Input
          autoComplete="off"
          name="name"
          onChange={handleInputChange}
          placeholder={props.name}
        />
      </Form.Field>
      <Form.Field>
        <DeleteModal removeDevice={() => props.removeDevice(values)} />
      </Form.Field>
      <Button
        onClick={() => props.saveFunction(values)}
        color="green"
        type="submit"
      >
        <Icon name="checkmark" />
        Save changes
      </Button>
    </Form>
  );
};

class DeviceSettingsModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      name: this.props.device.name,
    };

    this.updateDevice = this.updateDevice.bind(this);
    this.deleteDevice = this.deleteDevice.bind(this);
    // this.useExternalTempSensor = this.useExternalTempSensor.bind(this);
  }

  closeModal = (e) => {
    this.setState({ openModal: false });
  };

  openModal = (e) => {
    this.setState({ openModal: true });
  };

  updateDevice(values) {
    console.log(values, this.external);
    let { name } = values;
    if (values.name.length === 0) {
      name = this.props.device.name;
    }

    const data = {
      ...this.props.device,
      name,
    };

    if (this.props.device.kind === 'thermostat') {
      const external = values.external
        ? values.external
        : this.props.device.useExternalSensors;
      console.log(external);
      data.useExternalSensors = external;
    }
    console.log(data.useExternalSensors);
    this.props
      .saveDevice(data)
      .then(() => this.setState({ openModal: false }))
      .catch((err) => console.error(
          `settings modal for device ${this.props.id} deletion error`,
          err,
        ));
  }

  deleteDevice() {
    this.props
      .deleteDevice(this.props.device)
      .then(() => this.setState({ openModal: false }))
      .catch((err) => console.error(
          `settings modal for device ${this.props.id} deletion error`,
          err,
        ));
  }

  _editForm = null;

  get editForm() {
    this._editForm = this._editForm || (
      <SettingsForm
        name={this.state.name}
        removeDevice={this.deleteDevice}
        saveFunction={this.updateDevice}
        tempSensor={this.useExternalTempSensor}
      />
    );
    return this._editForm;
  }

  render() {
    return (
      <Modal closeIcon onClose={this.closeModal} open={this.state.openModal}>
        <Modal.Header>
Settings of
          {this.props.device.name}
        </Modal.Header>
        <Modal.Content>{this.editForm}</Modal.Content>
      </Modal>
    );
  }
}

const mapStateToProps = (state, ownProps) => ({
  device: state.devices[ownProps.id],
});
const DeviceSettingsModalContainer = connect(
  mapStateToProps,
  RemoteService,
  null,
  { forwardRef: true },
)(DeviceSettingsModal);
export default DeviceSettingsModalContainer;
