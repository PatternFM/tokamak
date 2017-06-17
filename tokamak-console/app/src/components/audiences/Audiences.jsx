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
            loading: false,
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
        });
        this.setState({ loading:false });
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewAudiences audiences={this.state.audiences} />;
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

export default Audiences;