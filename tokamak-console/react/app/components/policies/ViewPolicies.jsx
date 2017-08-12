import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import PolicyTable from "./PolicyTable.jsx";
import { NavLink } from "react-router-dom";
import ManagePolicyDialog from "./ManagePolicyDialog.jsx";

class ViewPolicies extends React.Component {

    render() {
        let moreThanOneResult = this.props.policies && this.props.policies.length > 0;
        
        return (
            <div className="animated fadeIn">
            
            <ManagePolicyDialog ref="managePolicyDialog" policyCreated={this.props.policyCreated} />
            
            {moreThanOneResult &&
              <MuiThemeProvider>
                <div>
                  <div id="header">
                    <div className="title">Password Policies</div>
                    <p className="overview">View, create and manage password policies.</p>
                  </div>
                  <div className="table-container">
                    <PolicyTable policies={this.props.policies} policyUpdated={this.props.policyUpdated} policyDeleted={this.props.policyDeleted} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any policies yet</h2>
                <button className="tok-button center margin-top-50" onClick={() => this.refs.managePolicyDialog.show()}>Create Policy</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewPolicies;