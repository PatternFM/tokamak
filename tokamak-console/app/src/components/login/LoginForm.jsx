import React from "react";
import {withRouter} from "react-router-dom";
import AuthenticationService from "../../services/AuthenticationService";

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
        event.preventDefault();
        
        let self = this;
        
        this.setState({ loading:true });
        AuthenticationService.login(this.state.username, this.state.password, function(result) {
            if(result.status === "accepted") {
                self.props.history.push("/apps");
            }
            if(result.status === "rejected") {
                self.setState({error: result.message});
            }
            self.setState({ loading:false });
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
            {!this.state.loading &&
            <form  method="POST" onSubmit={this.login.bind(this)}>
              <div id="login-title">Sign In To Tokamak</div>
              
              {this.state.error.length > 0 &&
                 <div className="login-error">{this.state.error}</div>
              }
              
              <input id="username" className="login-textfield" type="text" name="username" value={this.state.username} placeholder="username" onChange={this.changeUsername.bind(this)} autoComplete="off" />
              <input id="password" className="login-textfield" type="password" name="password" value={this.state.password} onChange={this.changePassword.bind(this)} placeholder="password" autoComplete="off" />
              <br/>
              <input type="submit" name="login" value="Sign In" disabled={!this.complete()} />
            </form>
            }
            
            {this.state.loading &&
              <form>
              <div id="login-title">Sign In To Tokamak</div>
              <p className="loading-content">Signing In</p>
              <br/><br/>
              <svg className="spinner" width="45px" height="45px" viewBox="0 0 66 66">
                <circle className="path" fill="none" strokeWidth="6" strokeLinecap="round" cx="33" cy="33" r="30" />
              </svg>
              </form>
              }           
          </div>
        );
    }
    
}

export default withRouter(LoginForm);