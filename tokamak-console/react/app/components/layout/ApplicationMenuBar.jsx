import React from "react";

import { MuiThemeProvider } from 'material-ui/styles';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AppBar from 'material-ui/AppBar';
import MenuItem from 'material-ui/MenuItem';
import Drawer from 'material-ui/Drawer';
import { NavLink } from "react-router-dom";
import FlatButton from 'material-ui/FlatButton';
import AuthenticationService from "../../services/AuthenticationService";

class ApplicationMenuBar extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            open: false
        }
    }

    toggle() {
        this.setState({open: !this.state.open});
    }

    logout() {
        AuthenticationService.logout();
        window.location.replace("/login");
    }

    close() {
        this.setState({open: false});
    }

    render() {
        const barTheme = getMuiTheme({
           palette: {
             primary1Color: "#111"
           }
        });  
  
        return (
            <MuiThemeProvider muiTheme={barTheme}>
              <div id="menu-items">
                <AppBar onLeftIconButtonTouchTap={this.toggle.bind(this)} onRightIconButtonTouchTap={this.logout.bind(this)} className="application-bar" iconElementRight={<FlatButton style={{marginTop:"6px"}} label="Sign Out" />} />
                <Drawer open={this.state.open} onRequestChange={(open) => this.setState({open})} docked={false}>
                    <AppBar iconElementLeft={<span></span>} className="application-bar" />
                    <br/><br style={{paddingBottom:"30px"}}/>
                    <MenuItem onTouchTap={this.close.bind(this)} style={{marginTop:"60px"}}><NavLink to="/apps">OAuth Apps</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/scopes">App Scopes</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/authorities" activeClassName="active">App Authorities</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)} className="menu-spacer"><NavLink to="/audiences" activeClassName="active">App Audiences</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/accounts" activeClassName="active">User Accounts</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)} className="menu-spacer"><NavLink to="/roles" activeClassName="active">User Roles</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/policies" activeClassName="active">Password Policies</NavLink></MenuItem>
                </Drawer>
              </div>
            </MuiThemeProvider>
        );
    }
  
}

export default ApplicationMenuBar;