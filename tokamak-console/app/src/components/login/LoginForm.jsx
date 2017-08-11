import React from "react";
import {withRouter} from "react-router-dom";
import AuthenticationService from "../../services/AuthenticationService";
import Button from "../ui-controls/Button.jsx";

class LoginForm extends React.Component {

    constructor(props) {
        super(props);
        
        this.state = {
            username: "",
            password: "",
            error: "",
            loading: false
        }
    }

    login(event) {
        if(event) {
            event.preventDefault();
        }
        
        if(!this.complete()) {
            return;
        }
        
        this.setState({ loading:true, error:"" });
        
        let self = this;
        AuthenticationService.login(this.state.username, this.state.password, function(result) {
            setTimeout(function() {
                self.setState({ loading:false });
                if(result.status === "accepted") {
                    self.props.history.push("/apps");
                }
                if(result.status === "rejected") {
                    self.setState({error: result.message});
                }
            }, 300);
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
    
    render() {
        return (
          <div id="login-form">
            <form  method="POST" onSubmit={ this.login.bind(this) }>
              <div id="login-title">Sign In To Tokamak</div>
              
              {this.state.error.length > 0 &&
                 <div className="login-error">{this.state.error}</div>
              }
              {this.state.error.length === 0 &&
                 <div className="login-error">&nbsp;</div>
              }
              
              <input id="username" className="login-textfield" type="text" name="username" value={this.state.username} placeholder="username" onChange={this.changeUsername.bind(this)} autoComplete="off" />
              <input id="password" className="login-textfield" type="password" name="password" value={this.state.password} onChange={this.changePassword.bind(this)} placeholder="password" autoComplete="off" />
              <br/>
              
              <Button className="login-button" loading={this.state.loading} name="Sign In" onClick={ this.login.bind(this) } />
            </form>
          </div>
        );
    }
    
}

export default withRouter(LoginForm);