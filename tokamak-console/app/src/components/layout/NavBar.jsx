import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

class NavBar extends React.Component {
  render() {
    return (
        <div id="navbar">
          <div id="navbar-top">

          </div> 
          <ul>
            <li><NavLink to="/clients" activeClassName="active">Clients</NavLink></li>
            <li><NavLink to="/scopes" activeClassName="active">Scopes</NavLink></li>
            <li><NavLink to="/authorities" activeClassName="active">Authorities</NavLink></li>
            <li><NavLink to="/audiences" activeClassName="active">Audiences</NavLink></li>
            <br/><br/>
            <li><NavLink to="/accounts" activeClassName="active">Accounts</NavLink></li>
            <li><NavLink to="/roles" activeClassName="active">Roles</NavLink></li>
            <br/><br/>
            <li><NavLink to="/settings" activeClassName="active">Settings</NavLink></li>
          </ul>
        </div>
    );
  }
}

export default NavBar;