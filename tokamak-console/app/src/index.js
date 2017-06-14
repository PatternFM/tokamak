import React from 'react';
import ReactDom from 'react-dom';
import { BrowserRouter as Router, Route } from 'react-router-dom'
import createHistory from 'history/createBrowserHistory'

import AuthenticationService from "./services/AuthenticationService"

import layout from './css/layout.css';
import login from './css/login.css';

import Home from './pages/Home.jsx';
import Login from './pages/Login.jsx';

const history = createHistory();

function authenticated() {
    return AuthenticationService.isAuthenticated();
}

ReactDom.render(
  <Router history={history}>
      <div>
          <Route exact path="/" component={Login} />
          <Route path="/login" component={Login} />
          <Route onEnter={authenticated}>
              <Route path="/console" component={Home} />
          </Route>
      </div>
  </Router>
, document.getElementById("tokamak-console"));