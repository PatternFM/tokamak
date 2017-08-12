import React, { Component } from "react";

import NavBar from "./NavBar.jsx"

class Layout extends Component {
  render() {
    return (
      <div id="container">
        <NavBar />
        <div id="content">
          {this.props.children}
        </div>
      </div>
    );
  }
}

export default Layout;
