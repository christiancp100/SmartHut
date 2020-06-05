import React from 'react';
import { render } from '@testing-library/react';
import { Router } from 'react-router';
import { createMemoryHistory } from 'history';
import { Provider } from 'react-redux';
import App from './App';
import smartHutStore from './store';

test('redirects to homepage', () => {
  const history = createMemoryHistory();
  render(
    <Router history={history}>
      <Provider store={smartHutStore}>
        <App />
      </Provider>
    </Router>,
  );
  expect(history.location.pathname).toBe('/');
});
