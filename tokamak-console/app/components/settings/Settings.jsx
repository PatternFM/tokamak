import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";

import Layout from "../layout/Layout.jsx";

class Settings extends React.Component {
  render() {
    return (
        <Layout>
            <MuiThemeProvider>
               <div className="content-holder">
                 <h1>Settings</h1>
               </div>
            </MuiThemeProvider>
        </Layout>
    );
  }
}

export default Settings;