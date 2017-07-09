import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AppTable from "./AppTable.jsx";
import { NavLink } from "react-router-dom";
import ManageClientDialog from "./ManageClientDialog.jsx";

class ViewApps extends React.Component {

    render() {
        let moreThanOneResult = this.props.apps.payload && this.props.apps.payload.length > 0;
        
        return (
            <div className="animated fadeIn">
            
            <ManageClientDialog ref="manageClientDialog" clientCreated={this.props.clientCreated} />
            
            {moreThanOneResult  &&
              <MuiThemeProvider>
                <div>
                  <div id="header">
                    <div className="title">OAuth Apps</div>
                    <p className="overview">View, create and manage apps to interact with OAuth2 protected endpoints. <NavLink to="/help#apps">Learn more about apps.</NavLink></p>
                    <button className="tok-button fixed-top" onClick={() => this.refs.manageClientDialog.show()}>+ Create App</button>
                  </div>
                  <div className="table-container">
                    <AppTable apps={this.props.apps} clientUpdated={this.props.clientUpdated} clientDeleted={this.props.clientDeleted} pageRequested={this.props.pageRequested} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any apps yet</h2>
                <button className="tok-button center margin-top-50" onClick={() => this.refs.manageClientDialog.show()}>Create App</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewApps;