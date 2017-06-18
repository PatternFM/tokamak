import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AuthorityTable from "./AuthorityTable.jsx";

class ViewAuthorities extends React.Component {

    render() {
        let moreThanOneResult = this.props.authorities && this.props.authorities.length > 0;
        
        return (
            <div className="animated fadeIn">
            {moreThanOneResult &&
              <MuiThemeProvider>
                <div>
                  <div id="header"><div className="title">App Authorities</div></div>
                  <div className="table-container">
                    <AuthorityTable authorities={this.props.authorities} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
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