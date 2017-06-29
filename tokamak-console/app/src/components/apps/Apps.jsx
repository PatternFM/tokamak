import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewApps from "./ViewApps.jsx";
import ClientService from "../../services/ClientService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Apps extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            apps: [],
            loading: false,
            error: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        ClientService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ apps: result.instance.payload }, function() { });
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState( { loading:false } );
        });
    }

    render() {
        let page = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewApps apps={this.state.apps} />;
        let output = this.state.loading ? <Loader /> : page;
        
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

export default Apps;