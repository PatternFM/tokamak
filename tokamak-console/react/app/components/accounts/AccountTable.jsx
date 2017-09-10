import React from "react";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';
import FontIcon from 'material-ui/FontIcon';

const Timestamp = require("react-timestamp");

class AccountTable extends React.Component {

    viewAccountDetails(account) {
        this.props.accountClicked(account);
    }

    render() {
        return (
          <div>
            <div className="scrollable-results">
              {this.props.accounts.payload.map((account) => 
                <div className="result" key={account.id} onClick={ () => this.viewAccountDetails(account) }>
                    <h4>{account.username}</h4> 
                </div>
              )}
            
              <Pagination records={this.props.accounts} pageRequested={this.props.pageRequested} />
              <br/><br/><br/><br/><br/><br/><br/><br/>
              <br/><br/><br/><br/><br/><br/><br/><br/>
            </div>
          </div>
        );
    }
  
}

export default AccountTable;