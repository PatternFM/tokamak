import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";

class Clients extends React.Component {
  render() {
    return (
        <Layout>
            <MuiThemeProvider>
               <div className="content-holder">
                 <h1>OAuth Clients</h1>
               </div>
            </MuiThemeProvider>
        </Layout>
    );
  }
}

export default Clients;