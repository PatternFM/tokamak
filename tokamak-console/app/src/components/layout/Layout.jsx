import React, { Component } from "react";

import Header from "./Header.jsx"
import Footer from "./Footer.jsx"
import NavBar from "./NavBar.jsx"

class Layout extends Component {
  render() {
    return (
      <div id="container">
        <Header />
        <NavBar />
        <div id="content">
          {this.props.children}
        </div>
      </div>
    );
  }
}

export default Layout;
