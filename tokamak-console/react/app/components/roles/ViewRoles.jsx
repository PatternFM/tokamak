import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import RoleTable from "./RoleTable.jsx";
import { NavLink } from "react-router-dom";
import ManageRoleDialog from "./ManageRoleDialog.jsx";
import FontIcon from 'material-ui/FontIcon';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import RaisedButton from 'material-ui/RaisedButton';

class ViewRoles extends React.Component {

    render() {
        const buttonTheme = getMuiTheme({
            palette: {
                primary1Color: "#F44336"
            }
        });
        
        let moreThanOneResult = this.props.roles && this.props.roles.length > 0;
        
        return (
            <div>
              <ManageRoleDialog ref="manageRoleDialog" roleCreated={this.props.roleCreated} />
              
              {!moreThanOneResult &&
                <div className="full-page-notice">
                  <h2>You haven't created any roles yet</h2>
                  <MuiThemeProvider muiTheme={buttonTheme}>
                    <RaisedButton primary={true} onClick={() => this.refs.manageRoleDialog.show()} className="mui-button-fixed margin-top-40 margin-bottom-20" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Create Role"></RaisedButton>
                  </MuiThemeProvider>  
                </div>
              }
            
              {moreThanOneResult &&
                <div className="results-panel">
                  <MuiThemeProvider>
                    <div>
                      <div id="header">
                        <h2>User Roles</h2>
                        <MuiThemeProvider muiTheme={buttonTheme}>
                          <FloatingActionButton className="overlay-add-button" onClick={() => this.refs.manageRoleDialog.show()}>
                            <ContentAdd />
                          </FloatingActionButton>   
                        </MuiThemeProvider>                    
                      </div>
                      <RoleTable roles={this.props.roles} roleClicked={this.props.roleClicked} roleUpdated={this.props.roleUpdated} roleDeleted={this.props.roleDeleted} pageRequested={this.props.pageRequested} />
                    </div>
                  </MuiThemeProvider>
                </div>
               }            
            </div>
        );
    }
  
}

export default ViewRoles;