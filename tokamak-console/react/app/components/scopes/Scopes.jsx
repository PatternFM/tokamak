import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewScopes from "./ViewScopes.jsx";
import ViewScope from "./ViewScope.jsx";
import ScopeService from "../../services/ScopeService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Scopes extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            scopes: [],
            loading: false,
            error: null,
            scope: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        ScopeService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ result:result.instance.scopes });
                if(result.instance.scopes) {
                    this.setState({ scope:result.instance.scopes[0] });
                }
                else {
                    this.setState({ scope:{ } });
                }
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
    }

    scopeClicked(target) {
        this.setState({ scope:target });
    }

    scopeCreated(scope) {
        var result = this.state.result;
        var scopes = result.slice();
        scopes.unshift(scope);
        result = scopes;
        this.setState({ result:result });
        this.setState({ scope:scope });
    }

    scopeUpdated(scope) {
        var result = this.state.result;
        var scopes = result.slice();
        var index = scopes.findIndex(function(a) { return a.id === scope.id });
        if(index !== -1) {
            scopes[index] = scope;
        }
        result = scopes;
        this.setState({ result:result });
        this.setState({ scope:scope });
    }

    scopeDeleted(scope) {
        var result = this.state.result;
        var scopes = result.slice();
        var index = scopes.findIndex(function(a) { return a.id === scope.id });
        if(index !== -1) {
            scopes.splice(index, 1);
        }
        result = scopes;
        this.setState({ result:result });
        
        if(scopes.length > 1 && index > 0) {
            this.setState({ scope:scopes[index - 1] });
        }
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <div><ViewScopes scopes={this.state.result} scopeClicked={ this.scopeClicked.bind(this) } scopeCreated={ this.scopeCreated.bind(this) } scopeUpdated={ this.scopeUpdated.bind(this) } scopeDeleted={ this.scopeDeleted.bind(this) } />  <ViewScope scope={this.state.scope} scopeUpdated={ this.scopeUpdated.bind(this) } scopeDeleted={ this.scopeDeleted.bind(this) } /></div>;
        let render = this.state.loading ? <Loader /> : output;
        
        return (
            <Layout>
                <MuiThemeProvider>
                   <div className="content-holder">
                     {render}
                   </div>
                </MuiThemeProvider>
            </Layout>
        );
    }
    
}

export default Scopes;