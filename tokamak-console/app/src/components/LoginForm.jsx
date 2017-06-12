import React, { Component } from 'react';

class LoginForm extends Component {

  login(e) {
     e.preventDefault();
     console.log("LOGIN CALLED");
     
     fetch('http://localhost:9600/v1/oauth/token', {
         method: 'POST',
         body: JSON.stringify({
             username: 'foo',
             password: 'foo',
         })
     }).then((response) => console.log("REULT"))
     
     console.log("HERE??");
  }

  render() {
    return (
      <form id="login-form" method="POST" onSubmit={this.login}>
        <div id="login-title">Tokamak Console</div>
        <input id="username" className="login-textfield" type="text" name="username" placeholder="username" />
        <input id="password" className="login-textfield" type="password" name="password" placeholder="password" />
        <br/>
        <input type="submit" name="login" value="Sign In" />
      </form>
    );
  }
}

export default LoginForm;