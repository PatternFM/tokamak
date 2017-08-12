import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewRoles from "./ViewRoles.jsx";
import RoleService from "../../services/RoleService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Roles extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            roles: [],
            loading: false,
            error: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        RoleService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState( {roles: result.instance.roles}, function() { } );
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
    }

    roleCreated(role) {
        var roles = this.state.roles.slice();
        roles.unshift(role);
        this.setState({ roles: roles });
    }

    roleUpdated(role) {
        var roles = this.state.roles.slice();
        var index = roles.findIndex(function(a) {return a.id === role.id});
        if(index !== -1) {
            roles[index] = role;
        }
        this.setState({ roles: roles });
    }

    roleDeleted(role) {
        var roles = this.state.roles.slice();
        var index = roles.findIndex(function(a) {return a.id === role.id});
        if(index !== -1) {
            roles.splice(index, 1);
        }
        this.setState({ roles: roles });
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <ViewRoles roles={this.state.roles} roleCreated={ this.roleCreated.bind(this) } roleUpdated={ this.roleUpdated.bind(this) } roleDeleted={ this.roleDeleted.bind(this) } />;
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

export default Roles;