import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AuthorityTable from "./AuthorityTable.jsx";

class ViewAuthorities extends React.Component {

    render() {
        return (
            <div>
            {this.props.authorities &&
              <MuiThemeProvider>
                  <div className="table-container">
                    <h1>App Authorities</h1>
                    <AuthorityTable authorities={this.props.authorities} />
                  </div>
              </MuiThemeProvider>
            }
            {!this.props.authorities &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any authorities yet</h2>
                <button className="tok-button center margin-top-50">Create Authority</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewAuthorities;