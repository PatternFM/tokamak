import React, { Component } from "react";
import ApplicationMenuBar from "./ApplicationMenuBar.jsx"

class Layout extends Component {

  render() {
    return (
      <div id="container">
        <ApplicationMenuBar />
        {this.props.children}
      </div>
    );
  }
}

export default Layout;
