import React from "react";
import {withRouter} from "react-router-dom";
import AuthenticationService from "../../services/AuthenticationService";

import { MuiThemeProvider } from 'material-ui/styles';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import { withStyles, createStyleSheet } from 'material-ui/styles';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import Paper from 'material-ui/Paper';
import {blue900} from 'material-ui/styles/colors';

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
        const theme = getMuiTheme({
           palette: {
             primary1Color: blue900
           }
        });
        
        return (
          <MuiThemeProvider muiTheme={theme}>
             <Paper className="login-container">
               <h2>Sign in to the Tokamak Console</h2>
               <TextField style={{width:"100%"}} id="username" floatingLabelText="Username" value={this.state.username} onChange={this.changeUsername.bind(this)} />
               <TextField style={{width:"100%"}} id="password" floatingLabelText="Password" value={this.state.password} onChange={this.changePassword.bind(this)} type="password" />
               <RaisedButton primary={true} className="mui-button-full margin-top-40" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Sign In"></RaisedButton>
             </Paper>
          </MuiThemeProvider>
        );
    }
    
}

export default withRouter(LoginForm);