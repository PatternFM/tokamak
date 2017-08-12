import React from "react";
import {withRouter} from "react-router-dom";
import AuthenticationService from "../../services/AuthenticationService";

import { MuiThemeProvider, createMuiTheme } from 'material-ui/styles';
import { withStyles, createStyleSheet } from 'material-ui/styles';
import createPalette from 'material-ui/styles/palette';
import { blue, red } from 'material-ui/colors';
import Button from 'material-ui/Button';
import Paper from 'material-ui/Paper';
import Typography from 'material-ui/Typography';

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
          <MuiThemeProvider>
             <paper>
               HI
               <button>click</button>
             </paper>
          </MuiThemeProvider>
        );
    }
    
}

export default withRouter(LoginForm);