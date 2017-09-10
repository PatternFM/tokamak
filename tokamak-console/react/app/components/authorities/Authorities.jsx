import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewAuthorities from "./ViewAuthorities.jsx";
import ViewAuthority from "./ViewAuthority.jsx";
import AuthorityService from "../../services/AuthorityService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Authorities extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            authorities: [],
            loading: false,
            error: null,
            authority: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        AuthorityService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ result:result.instance.authorities });
                if(result.instance.authorities) {
                    this.setState({ authority:result.instance.authorities[0] });
                }
                else {
                    this.setState({ authority:{ } });
                }
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
    }

    authorityClicked(target) {
        this.setState({ authority:target });
    }

    authorityCreated(authority) {
        var result = this.state.result;
        var authorities = result.slice();
        authorities.unshift(authority);
        result = authorities;
        this.setState({ result:result });
        this.setState({ authority:authority });
    }

    authorityUpdated(authority) {
        var result = this.state.result;
        var authorities = result.slice();
        var index = authorities.findIndex(function(a) { return a.id === authority.id });
        if(index !== -1) {
            authorities[index] = authority;
        }
        result = authorities;
        this.setState({ result:result });
        this.setState({ authority:authority });
    }

    authorityDeleted(authority) {
        var result = this.state.result;
        var authorities = result.slice();
        var index = authorities.findIndex(function(a) { return a.id === authority.id });
        if(index !== -1) {
            authorities.splice(index, 1);
        }
        result = authorities;
        this.setState({ result:result });
        
        if(authorities.length > 1) {
            this.setState({ authority:authorities[index - 1] });
        }
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <div><ViewAuthorities authorities={this.state.result} authorityClicked={ this.authorityClicked.bind(this) } authorityCreated={ this.authorityCreated.bind(this) } authorityUpdated={ this.authorityUpdated.bind(this) } authorityDeleted={ this.authorityDeleted.bind(this) } />  <ViewAuthority authority={this.state.authority} authorityUpdated={ this.authorityUpdated.bind(this) } authorityDeleted={ this.authorityDeleted.bind(this) } /></div>;
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

export default Authorities;