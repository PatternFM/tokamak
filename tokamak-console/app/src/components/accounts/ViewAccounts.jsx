import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AccountTable from "./AccountTable.jsx";

class ViewAccounts extends React.Component {

    render() {
        let moreThanOneResult = this.props.accounts && this.props.accounts.length > 0;
         
        return (
            <div className="animated fadeIn">
            {moreThanOneResult &&
              <MuiThemeProvider>
                <div>
                  <div id="header"><div className="title">Accounts</div></div>
                  <div className="table-container">
                    <AccountTable accounts={this.props.accounts} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any accounts yet</h2>
                <button className="tok-button center margin-top-50">Create Account</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewAccounts;