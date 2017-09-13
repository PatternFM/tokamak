import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewApps from "./ViewApps.jsx";
import ViewApp from "./ViewApp.jsx";
import ClientService from "../../services/ClientService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Apps extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            result: {},
            loading: false,
            error: null,
            app: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        ClientService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ result:result.instance });
                if(result.instance.payload) {
                    this.setState({ app:result.instance.payload[0] });
                }
                else {
                    this.setState( {app:{}} );
                }
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState( { loading:false } );
        });
    }

    clientClicked(target) {
        this.setState({app:target});
    }

    clientCreated(client) {
        var result = this.state.result;
        var clients = result.payload.slice();
        clients.unshift(client);
        result.payload = clients;
        this.setState({ result:result });
    }

    clientUpdated(client) {
        var result = this.state.result;
        var clients = result.payload.slice();
        var index = clients.findIndex(function(a) { return a.id === client.id });
        if(index !== -1) {
            clients[index] = client;
        }
        result.payload = clients;
        this.setState({ result:result });
    }

    clientDeleted(client) {
        var result = this.state.result;
        var clients = result.payload.slice();
        var index = clients.findIndex(function(a) { return a.id === client.id });
        if(index !== -1) {
            clients.splice(index, 1);
        }
        result.payload = clients;
        this.setState({ result:result });

        if(clients.length > 1 && index > 0) {
            this.setState({ app:clients[index - 1] });
        }        
    }

    pageRequested(page) {
        this.setState({ loading:true });
        ClientService.list(page).then((result) => {
            if(result.status === "accepted") {
                this.setState({ result: result.instance }, function() { });
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState( { loading:false } );
        });
    } 

    render() {
        let page = this.state.error != null ? <ApplicationError error={this.state.error} /> : <div><ViewApps apps={this.state.result} clientClicked={ this.clientClicked.bind(this) } clientCreated={ this.clientCreated.bind(this) } clientUpdated={ this.clientUpdated.bind(this) } clientDeleted={ this.clientDeleted.bind(this) } pageRequested={ this.pageRequested.bind(this) }/> <ViewApp app={this.state.app} clientClicked={ this.clientClicked.bind(this) } clientCreated={ this.clientCreated.bind(this) } clientUpdated={ this.clientUpdated.bind(this) } clientDeleted={ this.clientDeleted.bind(this) } /></div>;
        let output = this.state.loading ? <Loader /> : page;
        
        return (
            <Layout>
                <MuiThemeProvider>
                  <div>
                    {output}
                  </div>
                </MuiThemeProvider>
            </Layout>
        );
    }
    
}

export default Apps;