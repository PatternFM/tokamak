import React from "react";
import {withRouter} from "react-router-dom";
import AuthenticationService from "../../services/AuthenticationService";

import { MuiThemeProvider } from 'material-ui/styles';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import { withStyles, createStyleSheet } from 'material-ui/styles';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import Paper from 'material-ui/Paper';
import FontIcon from 'material-ui/FontIcon';

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
            self.setState({ loading:false });
            if(result.status === "accepted") {
                self.props.history.push("/apps");
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
    
    render() {
        const buttonTheme = getMuiTheme({
           palette: {
             primary1Color: "#F44336"
           }
        });

        const inputTheme = getMuiTheme({
           palette: {
             primary1Color: "#0088FF"
           }
        });
        
        let warn = "#FB8C00";
        
        return (
           <MuiThemeProvider muiTheme={buttonTheme}>
             <div>
             <div className="login-brand"><img className="login-icon" src="/img/logo.png"></img></div>
             
             <Paper className="login-container">
               <h2>Sign in to Tokamak</h2>
               <p>Manage your OAuth2 server, apps and accounts.</p>
               
               {this.state.error.length > 0 && 
                 <div className="validation-error">
                   <div className="warn"><FontIcon className="material-icons" color={warn}>warning</FontIcon></div>
                   <p>{this.state.error}</p>
                   <br style={{clear:"both"}}/>
                 </div>
               }
               
               <form method="POST" onSubmit={ this.login.bind(this) }>
                 <MuiThemeProvider muiTheme={inputTheme}>
                   <div>
                     <TextField style={{width:"100%"}} id="username" floatingLabelText="Username" value={this.state.username} onChange={this.changeUsername.bind(this)} />
                     <TextField style={{width:"100%", marginTop:"-10px"}} id="password" floatingLabelText="Password" value={this.state.password} onChange={this.changePassword.bind(this)} type="password" />
                   </div>
                 </MuiThemeProvider>
                 
                 <MuiThemeProvider muiTheme={buttonTheme}>
                   <div>
                     <RaisedButton primary={true} onClick={ this.login.bind(this) } className="mui-button-full margin-top-40 margin-bottom-20" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Sign In"></RaisedButton>
                     {this.state.loading && <div className="progress"><div className="indeterminate"></div></div> }
                   </div>
                 </MuiThemeProvider>
                 
                 <input type="submit" style={{display:"none"}}></input>
               </form>
             </Paper>
             </div>
          </MuiThemeProvider>
        );
    }
    
}

export default withRouter(LoginForm);