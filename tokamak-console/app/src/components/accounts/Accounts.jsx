import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewAccounts from "./ViewAccounts.jsx";
import AccountService from "../../services/AccountService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Accounts extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            result: {},
            loading: false,
            error: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        AccountService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ result: result.instance }, function() { });
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState( { loading:false } );
        });
    }

    pageRequested(page) {
        this.setState({ loading:true });
        AccountService.list(page).then((result) => {
            if(result.status === "accepted") {
                this.setState({ result: result.instance }, function() { });
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState( { loading:false } );
        });
    }

accountCreated(account) {
    var result = this.state.result;
    var accounts = result.payload.slice();
    accounts.unshift(account);
    result.payload = accounts;
    this.setState({ result:result });
}

accountUpdated(account) {
    var result = this.state.result;
    var accounts = result.payload.slice();
    var index = accounts.findIndex(function(a) { return a.id === account.id });
    if(index !== -1) {
        accounts[index] = account;
    }
    result.payload = accounts;
    this.setState({ result:result });
}

accountDeleted(account) {
    var result = this.state.result;
    var accounts = result.payload.slice();
    var index = accounts.findIndex(function(a) { return a.id === account.id });
    if(index !== -1) {
        accounts.splice(index, 1);
    }
    result.payload = accounts;
    this.setState({ result:result });
}

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewAccounts accounts={this.state.result} accountCreated={ this.accountCreated.bind(this) } accountUpdated={ this.accountUpdated.bind(this) } accountDeleted={ this.accountDeleted.bind(this) } pageRequested={ this.pageRequested.bind(this) } />;
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

export default Accounts;