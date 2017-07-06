import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import RoleTable from "./RoleTable.jsx";
import { NavLink } from "react-router-dom";
import ManageRoleDialog from "./ManageRoleDialog.jsx";

class ViewRoles extends React.Component {

    render() {
        let moreThanOneResult = this.props.roles && this.props.roles.length > 0;
        
        return (
            <div className="animated fadeIn">
            
            <ManageRoleDialog ref="manageRoleDialog" roleCreated={this.props.roleCreated} />
            
            {moreThanOneResult &&
              <MuiThemeProvider>
                <div>
                  <div id="header">
                    <div className="title">User Roles</div>
                    <p className="overview">View, create and manage user roles. <NavLink to="/help#roles">Learn more about roles.</NavLink></p>
                    <button className="tok-button fixed-top" onClick={() => this.refs.manageRoleDialog.show()}>+ Create Role</button>
                  </div>
                  <div className="table-container">
                    <RoleTable roles={this.props.roles} roleUpdated={this.props.roleUpdated} roleDeleted={this.props.roleDeleted} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any roles yet</h2>
                <button className="tok-button center margin-top-50" onClick={() => this.refs.manageRoleDialog.show()}>Create Role</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewRoles;