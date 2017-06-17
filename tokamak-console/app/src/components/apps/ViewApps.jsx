import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AppTable from "./AppTable.jsx";

class ViewApps extends React.Component {

    render() {
        return (
            <div className="animated fadeIn">
            {this.props.apps &&
              <MuiThemeProvider>
                  <div className="table-container">
                    <h1>Apps</h1>
                    <AppTable apps={this.props.apps} />
                  </div>
              </MuiThemeProvider>
            }
            {!this.props.apps &&
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