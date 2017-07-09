import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AppTable from "./AppTable.jsx";
import { NavLink } from "react-router-dom";

class ViewApps extends React.Component {

    render() {
        let moreThanOneResult = this.props.apps.payload && this.props.apps.payload.length > 0;
        
        return (
            <div className="animated fadeIn">
            {moreThanOneResult  &&
              <MuiThemeProvider>
                <div>
                  <div id="header">
                    <div className="title">OAuth Apps</div>
                    <p className="overview">View, create and manage apps to interact with OAuth2 protected endpoints. <NavLink to="/help#apps">Learn more about apps.</NavLink></p>
                    <button className="tok-button fixed-top">+ Create App</button>
                  </div>
                  <div className="table-container">
                    <AppTable apps={this.props.apps} pageRequested={this.props.pageRequested} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any apps yet</h2>
                <button className="tok-button center margin-top-50">Create App</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewApps;