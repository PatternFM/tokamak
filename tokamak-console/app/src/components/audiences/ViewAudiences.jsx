import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AudienceTable from "./AudienceTable.jsx";
import { NavLink } from "react-router-dom";
import ManageAudienceDialog from "./ManageAudienceDialog.jsx";

class ViewAudiences extends React.Component {

    render() {
        let moreThanOneResult = this.props.audiences.length > 0;
        
        return (
            <div className="animated fadeIn">
            
            <ManageAudienceDialog ref="manageAudienceDialog" audienceCreated={this.props.audienceCreated}  />
            
            {moreThanOneResult &&
              <MuiThemeProvider>
                <div>
                  <div id="header">
                    <div className="title">App Audiences</div>
                    <p className="overview">View, create and manage audiences. <NavLink to="/help#roles">Learn more about audiences.</NavLink></p>
                    <button className="tok-button fixed-top" onClick={() => this.refs.manageAudienceDialog.show()}>+ Create Audience</button>
                  </div>
                  <div className="table-container">
                    <AudienceTable audiences={this.props.audiences} audienceUpdated={this.props.audienceUpdated} audienceDeleted={this.props.audienceDeleted} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">App Audiences</h2>
                <p className="simple-message">The <em>audience</em> or "aud" claim in a JWT defines the intended recipients of a token. Since it's an optional claim, you don't need to define an audience for your oauth apps. <NavLink to="/help#audiences">Learn more about audiences.</NavLink></p>
                <br/><br/>
                <button className="tok-button center" onClick={() => this.refs.manageAudienceDialog.show()}>+ Create Audience</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewAudiences;