import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import App from './App';
import * as serviceWorker from './serviceWorker';
import smartHutStore from './store';

const index = (
  <Provider store={smartHutStore}>
    <App />
  </Provider>
);

ReactDOM.render(index, document.getElementById('root'));
serviceWorker.unregister();
