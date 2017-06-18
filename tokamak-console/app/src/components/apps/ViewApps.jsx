import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AppTable from "./AppTable.jsx";

class ViewApps extends React.Component {

    render() {
        console.log("HAVE " + this.props.apps.length + " apps");
        return (
            <div className="animated fadeIn">
            {this.props.apps && this.props.apps.length > 0  &&
              <MuiThemeProvider>
                <div>
                  <div id="header"><div className="title">Apps</div></div>
                  <div className="table-container">
                    <AppTable apps={this.props.apps} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!this.props.apps || this.props.apps.length === 0 &&
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