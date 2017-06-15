import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class NavBar extends React.Component {
  render() {
    return (
        <div id="navbar">
          <div id="navbar-top">

          </div> 
          <ul>
            <li><Link to="/clients">Clients</Link></li>
            <li><Link to="/scopes">Scopes</Link></li>
            <li><Link to="/authorities">Authorities</Link></li>
            <li><Link to="/audiences">Audiences</Link></li>
            <li><Link to="/accounts">Accounts</Link></li>
            <li><Link to="/roles">Roles</Link></li>
            <li><Link to="/settings">Settings</Link></li>
          </ul>
        </div>
    );
  }
}

export default NavBar;