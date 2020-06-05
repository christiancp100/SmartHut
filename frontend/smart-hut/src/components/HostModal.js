import React, { Component } from 'react';
import {
  Button,
  Header,
  Modal,
  Icon,
  Form,
  Responsive,
  Dropdown,
} from 'semantic-ui-react';
import { connect } from 'react-redux';
import { RemoteService, Forms } from '../remote';
import { appActions } from '../storeActions';

class HostModal extends Component {
  constructor(props) {
    super(props);
    this.state = { guests: [], users: [] };

    this.props
      .fetchGuests()
      .then(() => {
        this.setState({
          ...this.state,
          guests: this.props.guests.map((u) => u.id),
        });
      })
      .catch(console.error);

    Forms.fetchAllUsers()
      .then((users) => this.setState({
          ...this.state,
          users: users
            .filter((u) => u.id !== this.props.currentUserId)
            .map((u) => ({
              key: u.id,
              text: `@${u.username} (${u.name})`,
              value: u.id,
            })),
        }))
      .catch(console.error);

    this.saveGuestSettings = this.saveGuestSettings.bind(this);
    this.closeModal = this.closeModal.bind(this);
    this.openModal = this.openModal.bind(this);
    this.setGuests = this.setGuests.bind(this);
    this.saveGuestSettings = this.saveGuestSettings.bind(this);
  }

  setGuests(_, guests) {
    this.setState({ guests: guests.value });
  }

  closeModal() {
    this.setState({ openModal: false });
  }

  openModal() {
    this.setState({ openModal: true });
  }

  saveGuestSettings() {
    this.props
      .updateGuests(this.state.guests)
      .then(this.closeModal)
      .catch(console.error);
  }

  render() {
    return (
      <>
        <Responsive minWidth={768}>
          <Button icon labelPosition="left" inverted onClick={this.openModal}>
            <Icon name="plus" size="small" />
            Invitation settings
          </Button>
        </Responsive>
        <Responsive maxWidth={768}>
          <Button icon fluid labelPosition="left" onClick={this.openModal}>
            <Icon name="plus" size="small" />
            Invitation settings
          </Button>
        </Responsive>

        <Modal closeIcon onClose={this.closeModal} open={this.state.openModal}>
          <Header>Select guests</Header>
          <Modal.Content>
            <Form>
              <Form.Field style={{ marginTop: '1rem' }}>
                <label>Select which users are your guests: </label>
                <Dropdown
                  name="guests"
                  placeholder="Select Guests"
                  fluid
                  multiple
                  onChange={this.setGuests}
                  options={this.state.users}
                  value={this.state.guests}
                />
              </Form.Field>
            </Form>
          </Modal.Content>
          <Modal.Actions>
            <Button color="red" onClick={this.closeModal}>
              <Icon name="remove" />
              {' '}
Discard changes
            </Button>

            <Button color="green" onClick={this.saveGuestSettings}>
              <Icon name="checkmark" />
              {' '}
Save changes
            </Button>
          </Modal.Actions>
        </Modal>
      </>
    );
  }
}

const setActiveHost = (activeHost) => (dispatch) => dispatch(appActions.setActiveHost(activeHost));

const mapStateToProps = (state) => ({
  guests: state.guests,
  currentUserId: state.userInfo.id,
});
const HostModalContainer = connect(
  mapStateToProps,
  { ...RemoteService, setActiveHost },
  null,
  { forwardRef: true },
)(HostModal);
export default HostModalContainer;
