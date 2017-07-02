import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import RoleTable from "./RoleTable.jsx";
import { NavLink } from "react-router-dom";

class ViewRoles extends React.Component {

    render() {
        let moreThanOneResult = this.props.roles && this.props.roles.length > 0;
        
        return (
            <div className="animated fadeIn">
            {moreThanOneResult &&
              <MuiThemeProvider>
                <div>
                  <div id="header">
                    <div className="title">User Roles</div>
                    <p className="overview">View, create and manage user roles. <NavLink to="/help#roles">Learn more about roles.</NavLink></p>
                    <button className="tok-button fixed-top">+ Create Role</button>
                  </div>
                  <div className="table-container">
                    <RoleTable roles={this.props.roles} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any roles yet</h2>
                <button className="tok-button center margin-top-50">Create Role</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewRoles;