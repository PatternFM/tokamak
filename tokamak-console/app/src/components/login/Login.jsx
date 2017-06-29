import React, { Component } from "react";

import AuthenticationService from "../../services/AuthenticationService";
import LoginForm from "./LoginForm.jsx"

class Login extends Component {

    componentWillMount() {
        const { history } = this.props;
    
        if(AuthenticationService.isAuthenticated()) {
            history.push("/apps");
        }
    }

    render() {
      return (
          <div id="lg-container">
            <LoginForm />
          </div>
      );
    }
}

export default Login;