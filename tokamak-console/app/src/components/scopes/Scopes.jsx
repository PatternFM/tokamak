import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import ViewScopes from "./ViewScopes.jsx";
import ScopeService from "../../services/ScopeService.js";

class Scopes extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            scopes: []
        };
    }

    componentWillMount() {
        ScopeService.list().then((data) => {
            this.setState({scopes: data}, function() {});
        });
    }

  render() {
    return (
        <Layout>
            <MuiThemeProvider>
               <div className="content-holder">
                 <h1>App Scopes</h1>
                 <ViewScopes />
               </div>
            </MuiThemeProvider>
        </Layout>
    );
  }
}

export default Scopes;