import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AudienceTable from "./AudienceTable.jsx";

class ViewAudiences extends React.Component {

    render() {
        return (
            <div>
            {this.props.audience &&
              <MuiThemeProvider>
                  <div className="table-container">
                    <h1>App Audiences</h1>
                    <AudienceTable audiences={this.props.audiences} />
                  </div>
              </MuiThemeProvider>
            }
            {!this.props.audience &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any audiences yet</h2>
                <button className="tok-button center margin-top-50">Create Audience</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewAudiences;