import React from "react";
import ReactDom from "react-dom";
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom"
import createHistory from "history/createBrowserHistory"

import AuthenticationService from "./services/AuthenticationService"

import layout from "./css/layout.css";
import login from "./css/login.css";

import Home from "./components/home/Home.jsx";
import Login from "./components/login/Login.jsx";

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
          <PrivateRoute path="/console" component={Home} />
      </div>
  </Router>
, document.getElementById("tokamak-console"));