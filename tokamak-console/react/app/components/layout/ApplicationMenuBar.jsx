import React from "react";

import { MuiThemeProvider } from 'material-ui/styles';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AppBar from 'material-ui/AppBar';
import MenuItem from 'material-ui/MenuItem';
import Drawer from 'material-ui/Drawer';
import { NavLink } from "react-router-dom";

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

    close() {
        this.setState({open: false});
    }

    render() {
        const barTheme = getMuiTheme({
           palette: {
             primary1Color: "#3F5EF7"
           }
        });  
  
        return (
            <MuiThemeProvider muiTheme={barTheme}>
              <div>
                <AppBar onLeftIconButtonTouchTap={this.toggle.bind(this)} className="application-bar" />
                <Drawer open={this.state.open} onRequestChange={(open) => this.setState({open})} docked={false}>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/apps">OAuth Apps</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/scopes">App Scopes</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/authorities" activeClassName="active">App Authorities</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/audiences" activeClassName="active">App Audiences</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/accounts" activeClassName="active">User Accounts</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/roles" activeClassName="active">User Roles</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}><NavLink to="/policies" activeClassName="active">Password Policies</NavLink></MenuItem>
                    <MenuItem onTouchTap={this.close.bind(this)}></MenuItem>
                </Drawer>
              </div>
            </MuiThemeProvider>
        );
    }
  
}

export default ApplicationMenuBar;