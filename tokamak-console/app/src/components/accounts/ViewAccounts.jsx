import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AccountTable from "./AccountTable.jsx";
import { NavLink } from "react-router-dom";
import ManageAccountDialog from "./ManageAccountDialog.jsx";

class ViewAccounts extends React.Component {

    render() {
        let moreThanOneResult = this.props.accounts && this.props.accounts.length > 0;
         
        return (
            <div className="animated fadeIn">
            
             <ManageAccountDialog ref="manageAccountDialog" accountCreated={this.props.accountCreated} />
            
            {moreThanOneResult &&
              <MuiThemeProvider>
                <div>
                  <div id="header">
                    <div className="title">User Accounts</div>
                    <p className="overview">View, create and manage accounts. <NavLink to="/help#accounts">Learn more about user accounts.</NavLink></p>
                    <button className="tok-button fixed-top" onClick={() => this.refs.manageAccountDialog.show()}>+ Create Account</button>
                  </div>
                  <div className="table-container">
                    <AccountTable accounts={this.props.accounts} accountUpdated={this.props.accountUpdated} accountDeleted={this.props.accountDeleted} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any accounts yet</h2>
                <button className="tok-button center margin-top-50" onClick={() => this.refs.manageAccountDialog.show()}>Create Account</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewAccounts;