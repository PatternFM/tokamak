import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewPolicies from "./ViewPolicies.jsx";
import PasswordPolicyService from "../../services/PasswordPolicyService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Policies extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            policies: [],
            loading: false,
            error: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        PasswordPolicyService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState( {policies: result.instance.policies}, function() { } );
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
    }

    policyCreated(policy) {
        var policies = this.state.policies.slice();
        policies.unshift(policy);
        this.setState({ policies: policies });
    }

    policyUpdated(policy) {
        var policies = this.state.policies.slice();
        var index = policies.findIndex(function(a) {return a.id === policy.id});
        if(index !== -1) {
            policies[index] = policy;
        }
        this.setState({ policies: policies });
    }

    policyDeleted(policy) {
        var policies = this.state.policies.slice();
        var index = policies.findIndex(function(a) {return a.id === policy.id});
        if(index !== -1) {
            policies.splice(index, 1);
        }
        this.setState({ policies: policies });
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewPolicies policies={this.state.policies} policyCreated={ this.policyCreated.bind(this) } policyUpdated={ this.policyUpdated.bind(this) } policyDeleted={ this.policyDeleted.bind(this) } />;
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

export default Policies;