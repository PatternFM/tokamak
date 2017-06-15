import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";

class Scopes extends React.Component {
  render() {
    return (
        <Layout>
            <MuiThemeProvider>
               <div className="content-holder">
                 <h1>Scopes</h1>
               </div>
            </MuiThemeProvider>
        </Layout>
    );
  }
}

export default Scopes;