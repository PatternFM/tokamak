import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";

import Layout from "../layout/Layout.jsx";

class Users extends React.Component {
  render() {
    return (
        <Layout>
            <MuiThemeProvider>
               <div className="content-holder">
                 <h1>Accounts</h1>
               </div>
            </MuiThemeProvider>
        </Layout>
    );
  }
}

export default Users;