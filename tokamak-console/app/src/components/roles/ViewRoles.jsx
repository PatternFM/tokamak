import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import RoleTable from "./RoleTable.jsx";

class ViewRoles extends React.Component {

    render() {
        return (
            <div className="animated fadeIn">
            {this.props.roles && this.props.roles.length > 0 &&
              <MuiThemeProvider>
                <div>
                  <div id="header"><div className="title">Account Roles</div></div>
                  <div className="table-container">
                    <RoleTable roles={this.props.roles} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!this.props.roles || this.props.roles.length === 0 &&
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