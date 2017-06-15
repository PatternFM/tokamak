import React, { Component } from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";

import AuthenticationService from "../../services/AuthenticationService";
import LoginForm from "./LoginForm.jsx"

class Login extends Component {

    componentWillMount() {
        const { history } = this.props;
    
        if(AuthenticationService.isAuthenticated()) {
            history.push("/clients");
        }
    }

    render() {
      return (
          <div id="lg-container">
              <MuiThemeProvider>
                  <LoginForm />
              </MuiThemeProvider>
          </div>
      );
    }
}

export default Login;