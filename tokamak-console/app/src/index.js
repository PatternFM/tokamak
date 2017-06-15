import React from "react";
import ReactDom from "react-dom";
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom"
import createHistory from "history/createBrowserHistory"

import AuthenticationService from "./services/AuthenticationService"

import layout from "./css/layout.css";
import login from "./css/login.css";

import Login from "./components/login/Login.jsx";
import Logout from "./components/logout/Logout.jsx";
import Clients from "./components/clients/Clients.jsx";
import Scopes from "./components/scopes/Scopes.jsx";
import Roles from "./components/roles/Roles.jsx";
import Audiences from "./components/audiences/Audiences.jsx";
import Authorities from "./components/authorities/Authorities.jsx";
import Accounts from "./components/accounts/Accounts.jsx";
import Settings from "./components/settings/Settings.jsx";

const history = createHistory();

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

ReactDom.render(
  <Router history={history}>
      <div>
          <Route exact path="/" component={Login} />
          <Route path="/login" component={Login} />
          <Route path="/logout" component={Logout} />
          
          <PrivateRoute path="/clients" component={Clients} />
          <PrivateRoute path="/scopes" component={Scopes} />
          <PrivateRoute path="/authorities" component={Authorities} />
          <PrivateRoute path="/audiences" component={Audiences} />
          <PrivateRoute path="/accounts" component={Accounts} />
          <PrivateRoute path="/roles" component={Roles} />
          <PrivateRoute path="/settings" component={Settings} />
      </div>
  </Router>
, document.getElementById("tokamak-console"));