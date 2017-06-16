import React from "react";

import AuthenticationService from "../../services/AuthenticationService";

class Logout extends React.Component {

    componentWillMount() {
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