import React, { Component } from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";

import LoginForm from "./LoginForm.jsx"

class Login extends Component {
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