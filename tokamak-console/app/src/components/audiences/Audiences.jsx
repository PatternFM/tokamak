import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewAudiences from "./ViewAudiences.jsx";
import AudienceService from "../../services/AudienceService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Audiences extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            audiences: [],
            loading: true,
            error: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        AudienceService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({audiences: result.instance.audiences}, function() { });
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
    }

    render() {
        let page = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewAudiences audiences={this.state.audiences} />;
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

export default Audiences;