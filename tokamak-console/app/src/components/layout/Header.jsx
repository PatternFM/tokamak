import React, { Component } from 'react';
import { Link } from 'react-router-dom';

class Header extends Component {
  render() {
    return (
        <div id="header">
           <Link to="/logout">Sign Out</Link>
        </div>
    );
  }
}

export default Header;