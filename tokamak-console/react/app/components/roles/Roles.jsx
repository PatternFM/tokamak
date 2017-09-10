import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewRoles from "./ViewRoles.jsx";
import ViewRole from "./ViewRole.jsx";
import RoleService from "../../services/RoleService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Roles extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            roles: [],
            loading: false,
            error: null,
            role: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        RoleService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ result:result.instance.roles });
                if(result.instance.roles) {
                    this.setState({ role:result.instance.roles[0] });
                }
                else {
                    this.setState({ role:{ } });
                }
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
    }

    roleClicked(target) {
        this.setState({ role:target });
    }

    roleCreated(role) {
        var result = this.state.result;
        var roles = result.slice();
        roles.unshift(role);
        result = roles;
        this.setState({ result:result });
        this.setState({ role:role });
    }

    roleUpdated(role) {
        var result = this.state.result;
        var roles = result.slice();
        var index = roles.findIndex(function(a) { return a.id === role.id });
        if(index !== -1) {
            roles[index] = role;
        }
        result = roles;
        this.setState({ result:result });
        this.setState({ role:role });
    }

    roleDeleted(role) {
        var result = this.state.result;
        var roles = result.slice();
        var index = roles.findIndex(function(a) { return a.id === role.id });
        if(index !== -1) {
            roles.splice(index, 1);
        }
        result = roles;
        this.setState({ result:result });
        
        if(roles.length > 1) {
            this.setState({ role:roles[index - 1] });
        }
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <div><ViewRoles roles={this.state.result} roleClicked={ this.roleClicked.bind(this) } roleCreated={ this.roleCreated.bind(this) } roleUpdated={ this.roleUpdated.bind(this) } roleDeleted={ this.roleDeleted.bind(this) } />  <ViewRole role={this.state.role} roleUpdated={ this.roleUpdated.bind(this) } roleDeleted={ this.roleDeleted.bind(this) } /></div>;
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