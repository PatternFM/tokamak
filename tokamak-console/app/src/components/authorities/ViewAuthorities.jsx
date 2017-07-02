import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AuthorityTable from "./AuthorityTable.jsx";
import { NavLink } from "react-router-dom";
import CreateAuthorityForm from "./CreateAuthorityForm.jsx"

class ViewAuthorities extends React.Component {
    
    render() {
        let moreThanOneResult = this.props.authorities && this.props.authorities.length > 0;
        
        return (
            <div className="animated fadeIn">
            
            <CreateAuthorityForm ref="createAuthorityForm" authorityCreated={this.props.onNewAuthority} authorityUpdated={this.props.onUpdatedAuthority}  />
            
            {moreThanOneResult &&
              <MuiThemeProvider>
                <div>
                  <div id="header">
                    <div className="title">App Authorities</div>
                    <p className="overview">View, create and manage OAuth2 app authorities. <NavLink to="/help#authorities">Learn more about authorities.</NavLink></p>
                    <button className="tok-button fixed-top" onClick={() => this.refs.createAuthorityForm.handleOpen()}>+ Create Authority</button>
                  </div>
                  <div className="table-container">
                    <AuthorityTable authorities={this.props.authorities} authorityCreated={this.props.onNewAuthority} authorityUpdated={this.props.onUpdatedAuthority} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">App Authorities</h2>
                <p className="simple-message">The <em>authority</em> claim is a Tokamak specific claim, which enumerates the authorities (roles) assigned to a client. Since it's an optional claim, you don't need to define authorities for your oauth apps. <NavLink to="/help#authorities">Learn more about authorities.</NavLink></p>
                <br/><br/>
                <button className="tok-button center" onClick={() => this.refs.createAuthorityForm.handleOpen()}>+ Create Authority</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewAuthorities;