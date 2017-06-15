import React, { Component } from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";

import AuthenticationService from "../../services/AuthenticationService";

class Logout extends React.Component {

    componentWillMount() {
        console.log("COMPONENT WILL MOUNT");
        
        const { history } = this.props;
        AuthenticationService.logout();
        history.push("/login");
    }

    render() {
      return (
          <div></div>
      );
    }
}

export default Logout;