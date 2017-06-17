import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import ScopeTable from "./ScopeTable.jsx";

class ViewScopes extends React.Component {

    render() {
        return (
            <div className="animated fadeIn">
            {this.props.scopes &&
              <MuiThemeProvider>
                  <div className="table-container">
                    <h1>App Scopes</h1>
                    <ScopeTable scopes={this.props.scopes} />
                  </div>
              </MuiThemeProvider>
            }
            {!this.props.scopes &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any scopes yet</h2>
                <button className="tok-button center margin-top-50">Create Scope</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewScopes;