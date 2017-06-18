import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import ScopeTable from "./ScopeTable.jsx";

class ViewScopes extends React.Component {

    render() {
        let moreThanOneResult = this.props.scopes && this.props.scopes.length > 0;
                
        return (
            <div className="animated fadeIn">
            {moreThanOneResult  &&
              <MuiThemeProvider>
                <div>
                  <div id="header"><div className="title">App Scopes</div></div>
                  <div className="table-container">
                    <ScopeTable scopes={this.props.scopes} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
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