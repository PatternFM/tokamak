import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewAuthorities from "./ViewAuthorities.jsx";
import AuthorityService from "../../services/AuthorityService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Authorities extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            authorities: [],
            loading: false,
            error: null
        };
    }

    authorityCreated(authority) {
        var authorities = this.state.authorities.slice();
        authorities.unshift(authority);
        this.setState({ authorities: authorities });
    }

    authorityUpdated(authority) {
        var authorities = this.state.authorities.slice();
        var index = authorities.findIndex(function(a) {return a.id === authority.id});
        if(index !== -1) {
            authorities[index] = authority;
        }
        this.setState({ authorities: authorities });
    }

    authorityDeleted(authority) {
        var authorities = this.state.authorities.slice();
        var index = authorities.findIndex(function(a) {return a.id === authority.id});
        if(index !== -1) {
            authorities.splice(index, 1);
        }
        this.setState({ authorities: authorities });
    }

    componentWillMount() {
        this.setState({ loading:true });
        AuthorityService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ authorities: result.instance.authorities }, function() { });
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        }); 
    }

    render() {
        let page = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewAuthorities authorities={this.state.authorities} authorityCreated={ this.authorityCreated.bind(this) } authorityUpdated={ this.authorityUpdated.bind(this) } authorityDeleted={ this.authorityDeleted.bind(this) } />;
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

export default Authorities;