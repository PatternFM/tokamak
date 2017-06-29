import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AudienceTable from "./AudienceTable.jsx";
import { NavLink } from "react-router-dom";

class ViewAudiences extends React.Component {

    render() {
        let moreThanOneResult = this.props.audiences.length > 0;
        
        return (
            <div className="animated fadeIn">
            {moreThanOneResult &&
              <MuiThemeProvider>
                <div>
                  <div id="header"><div className="title">App Audiences</div></div>
                  <div className="table-container">
                    <AudienceTable audiences={this.props.audiences} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">App Audiences</h2>
                <p className="simple-message">The <em>audience</em> or "aud" claim in a JWT defines the intended recipients of a token. Since it's an optional claim, you don't need to define an audience for your oauth apps. <NavLink to="/help">Learn more about audiences.</NavLink></p>
                <br/><br/>
                <button className="tok-button center">Create Audience</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewAudiences;