import React from "react";
import ManageAccountDialog from "./ManageAccountDialog.jsx";
import DeleteAccountDialog from "./DeleteAccountDialog.jsx";

const Timestamp = require("react-timestamp");

class AccountTable extends React.Component {

    render() {
        return (
           <div>
                <ManageAccountDialog ref="manageAccountDialog" accountUpdated={this.props.accountUpdated} /> 
                <DeleteAccountDialog ref="deleteAccountDialog" accountDeleted={this.props.accountDeleted} />
                        
                <table className="display-table">
                  <thead>
                    <tr>
                      <th className="dth left-pad-0">Account</th>
                      <th className="dth">ID</th>
                      <th className="dth">Created</th>
                      <th className="dth right-pad-0"></th>
                    </tr>
                  </thead>
                  
                  <tbody>
                    {this.props.accounts.map((account) => 
                     <tr key={account.id}>
                       <td className="dtr left-pad-0">
                         {account.username}<br/>
                         <span className="description">{account.name}</span>
                       </td>
                       <td className="dtr">{account.id}</td>
                       <td className="dtr"><Timestamp time={account.created/1000} format="full" /></td>
                       <td className="dtr right-pad-0"> 
                         <i className="fa fa-times inline-button" onClick={ () => this.refs.deleteAccountDialog.show(account) }></i>
                         <i className="fa fa-pencil inline-button" style={{marginRight:"5px"}} onClick={ () => this.refs.manageAccountDialog.show(account) }></i> 
                       </td>
                     </tr>
                    )}
                  </tbody>
                </table>
            </div>
        );
    }
  
}

export default AccountTable;