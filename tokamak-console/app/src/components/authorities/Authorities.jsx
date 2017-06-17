import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import ViewAuthorities from "./ViewAuthorities.jsx";
import AuthorityService from "../../services/AuthorityService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Authorities extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            authorities: [],
            error: null
        };
    }

    componentWillMount() {
        AuthorityService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({authorities: result.instance.authorities}, function() { });
            }
            else {
                this.setState({error:result});
            }
        });
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewAuthorities authorities={this.state.authorities} />;
        
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

export default Authorities;