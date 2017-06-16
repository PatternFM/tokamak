import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import ViewAccounts from "./ViewAccounts.jsx";
import AccountService from "../../services/AccountService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Accounts extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            accounts: [],
            error: null
        };
    }

    componentWillMount() {
        AccountService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({accounts: result.instance.accounts}, function() { });
            }
            else {
                this.setState({error:result});
            }
        });
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewAccounts accounts={this.state.accounts} />;
        
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

export default Accounts;