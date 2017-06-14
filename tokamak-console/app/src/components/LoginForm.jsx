import React from "react";
import {withRouter} from "react-router-dom";
import AuthenticationService from "../services/AuthenticationService"

class LoginForm extends React.Component {

    constructor(props) {
        super(props);
        
        this.state = {
            username: "",
            password: "",
            error: ""
        }
    }

    login(event) {
        event.preventDefault();
        
        let self = this;
        
        AuthenticationService.login(this.state.username, this.state.password, function(result) {
            if(result.status === "accepted") {
                self.props.history.push("/console");
            }
            if(result.status === "rejected") {
                self.setState({error: result.message});
            }
        });
    }

    changeUsername(event) {
        this.setState({username: event.target.value});
    }
  
    changePassword(event) {
        this.setState({password: event.target.value});
    }

    complete() {
        return this.state.username && this.state.password && this.state.username.length > 0 && this.state.password.length > 0;
    }
    
    componentWillMount() {
        const { history } = this.props;
    
        if(AuthenticationService.isAuthenticated()) {
            console.log("auth - is authenticated.");
            history.push("/console");
        }
    }

    render() {
        return (
            <form id="login-form" method="POST" onSubmit={this.login.bind(this)}>
              <div id="login-title">Sign In To Tokamak</div>
              <input id="username" className="login-textfield" type="text" name="username" value={this.state.username} placeholder="username" onChange={this.changeUsername.bind(this)} autoComplete="off" />
              <input id="password" className="login-textfield" type="password" name="password" value={this.state.password} onChange={this.changePassword.bind(this)} placeholder="password" autoComplete="off" />
              <br/>
              <input type="submit" name="login" value="Sign In" disabled={!this.complete()} />
              {this.state.error.length > 0 &&
                 <div className="login-error">{this.state.error}</div>
              }
            </form>
        );
    }
    
}

export default withRouter(LoginForm);