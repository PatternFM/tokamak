import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewAccounts from "./ViewAccounts.jsx";
import ViewAccount from "./ViewAccount.jsx";
import AccountService from "../../services/AccountService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Accounts extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            accounts: [],
            loading: false,
            error: null,
            account: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        AccountService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ result:result.instance });
                if(result.instance.payload) {
                    this.setState({ account:result.instance.payload[0] });
                }
                else {
                    this.setState( {account:{}} );
                }
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
    }

    accountClicked(target) {
        this.setState({account:target});
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
        
        if(accounts.length > 1 && index > 0) {
            this.setState({ account:accounts[index - 1] });
        }
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <div><ViewAccounts accounts={this.state.result} accountClicked={ this.accountClicked.bind(this) } accountCreated={ this.accountCreated.bind(this) } accountUpdated={ this.accountUpdated.bind(this) } accountDeleted={ this.accountDeleted.bind(this) } />  <ViewAccount account={this.state.account} accountUpdated={ this.accountUpdated.bind(this) } accountDeleted={ this.accountDeleted.bind(this) } /></div>;
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