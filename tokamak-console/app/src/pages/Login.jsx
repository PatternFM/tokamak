import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import LoginForm from '../components/LoginForm.jsx'

class Login extends Component {
  render() {
    return (
        <div id="login-container">
            <MuiThemeProvider>
                    <LoginForm />
            </MuiThemeProvider>
        </div>
    );
  }
}

export default Login;