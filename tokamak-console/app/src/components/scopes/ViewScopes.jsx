import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import ScopeTable from "./ScopeTable.jsx";

class ViewScopes extends React.Component {

    render() {
        return (
            <div className="animated fadeIn">
            {this.props.scopes && this.props.scopes.length > 0  &&
              <MuiThemeProvider>
                <div>
                  <div id="header"><div className="title">App Scopes</div></div>
                  <div className="table-container">
                    <ScopeTable scopes={this.props.scopes} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!this.props.scopes || this.props.scopes.length === 0 &&
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