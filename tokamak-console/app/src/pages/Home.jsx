import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import { Card, CardTitle } from 'material-ui/Card';

import Layout from '../layout/Layout.jsx'


class Home extends Component {
  render() {
    return (
        <Layout>
            <MuiThemeProvider>
                <Card className="container">
                    <CardTitle title="React Application" subtitle="This is the home page." />
                </Card>
            </MuiThemeProvider>
        </Layout>
    );
  }
}

export default Home;