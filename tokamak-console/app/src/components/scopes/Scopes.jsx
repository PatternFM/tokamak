import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import ViewScopes from "./ViewScopes.jsx";
import ScopeService from "../../services/ScopeService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Scopes extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            scopes: [],
            error: null
        };
    }

    componentWillMount() {
        ScopeService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({scopes: result.instance.scopes}, function() { });
            }
            else {
                this.setState({ error:result.errors[0] });
            }
        });
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewScopes scopes={this.state.scopes} />;
        
        return (
            <Layout>
                <MuiThemeProvider>
                   <div className="content-holder">
                     {output}
                   </div>
                </MuiThemeProvider>
            </Layout>
        );
    }
    
}

export default Scopes;