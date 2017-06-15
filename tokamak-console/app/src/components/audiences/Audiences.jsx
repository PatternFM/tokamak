import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";

class Audiences extends React.Component {
  render() {
    return (
        <Layout>
            <MuiThemeProvider>
                <h1>Audiences</h1>
            </MuiThemeProvider>
        </Layout>
    );
  }
}

export default Audiences;