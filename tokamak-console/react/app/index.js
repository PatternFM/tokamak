import React from "react";
import ReactDom from "react-dom";
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom"

import AuthenticationService from "./services/AuthenticationService"

import Login from "./components/login/Login.jsx";
import Logout from "./components/logout/Logout.jsx";
import Apps from "./components/apps/Apps.jsx";
import Scopes from "./components/scopes/Scopes.jsx";
import Roles from "./components/roles/Roles.jsx";
import Audiences from "./components/audiences/Audiences.jsx";
import Authorities from "./components/authorities/Authorities.jsx";
import Accounts from "./components/accounts/Accounts.jsx";
import Policies from "./components/policies/Policies.jsx";
import Settings from "./components/settings/Settings.jsx";
import AuditHistory from "./components/audit/AuditHistory.jsx";
import history from "./components/history/history.js"
import injectTapEventPlugin from 'react-tap-event-plugin';

var FontAwesome = require('react-fontawesome');

const PrivateRoute = ({component: Component, ...rest}) => (
  <Route {...rest} render={props => (
    AuthenticationService.isAuthenticated() ? (
      <Component {...props} />
    ) : (
      <Redirect to={{
        pathname: "/login",
        state: {from: props.location}
      }}/>
    )
  )}/>
)

injectTapEventPlugin();

ReactDom.render(
  <Router history={history}>
      <div>
          <Route exact path="/" component={Login} />
          <Route path="/login" component={Login} />
          <Route path="/logout" component={Logout} />
          
          <PrivateRoute path="/apps" component={Apps} />
          <PrivateRoute path="/scopes" component={Scopes} />
          <PrivateRoute path="/authorities" component={Authorities} />
          <PrivateRoute path="/audiences" component={Audiences} />
          <PrivateRoute path="/accounts" component={Accounts} />
          <PrivateRoute path="/roles" component={Roles} />
          <PrivateRoute path="/policies" component={Policies} />
          <PrivateRoute path="/settings" component={Settings} />
          <PrivateRoute path="/audit" component={AuditHistory} />
      </div>
  </Router>
, document.getElementById("tokamak-console"));