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
                this.setState({ apps: result.instance.apps }, function() { });
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState( { loading:false } );
        });
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewApps apps={this.state.apps} />;
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

export default Apps;