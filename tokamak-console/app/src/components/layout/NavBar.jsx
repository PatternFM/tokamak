import React from "react";
import { NavLink } from "react-router-dom";

class NavBar extends React.Component {
  render() {
    return (
        <div>
        <div id="navbar">
          <ul>
            <li><NavLink to="/apps" activeClassName="active">Apps</NavLink></li>
            <li><NavLink to="/scopes" activeClassName="active">App Scopes</NavLink></li>
            <li><NavLink to="/authorities" activeClassName="active">App Authorities</NavLink></li>
            <li><NavLink to="/audiences" activeClassName="active">App Audiences</NavLink></li>
            <br/><br/>
            <li><NavLink to="/accounts" activeClassName="active">Accounts</NavLink></li>
            <li><NavLink to="/roles" activeClassName="active">Account Roles</NavLink></li>
            <br/><br/>
            <li><NavLink to="/settings" activeClassName="active">Settings</NavLink></li>
            <li><NavLink to="/audit" activeClassName="active">Audit History</NavLink></li>
          </ul>
        </div>
        <div id="navbar-footer"><NavLink to="/logout">Sign Out</NavLink></div>
        </div>
    );
  }
}

export default NavBar;