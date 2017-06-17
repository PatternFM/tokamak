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
            accounts: [],
            loading: false,
            error: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        AccountService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ accounts: result.instance.accounts }, function() { });
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState( { loading:false } );
        });
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewAccounts accounts={this.state.accounts} />;
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