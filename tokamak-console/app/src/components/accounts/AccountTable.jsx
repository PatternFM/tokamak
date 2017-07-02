import React from "react";
const Timestamp = require("react-timestamp");

class AccountTable extends React.Component {

    render() {
        return (
                <table className="display-table">
                  <thead>
                    <tr>
                      <th className="dth left-pad-0" align="left">Account</th>
                      <th className="dth" align="left">ID</th>
                      <th className="dth" align="left">Created</th>
                      <th className="dth right-pad-0" align="left"></th>
                    </tr>
                  </thead>
                  
                  <tbody>
                    {this.props.accounts.map((account) => 
                     <tr>
                       <td className="dtr left-pad-0" align="left">
                         {account.username}<br/>
                         <span className="description">{account.name}</span>
                       </td>
                       <td className="dtr" align="left">{account.id}</td>
                       <td className="dtr" align="left"><Timestamp time={account.created/1000} format="full" /></td>
                       <td className="dtr right-pad-0"> 
                         <i className="fa fa-times inline-button"></i>
                         <i className="fa fa-pencil inline-button" style={{marginRight:"5px"}}></i> 
                       </td>
                     </tr>
                    )}
                  </tbody>
                </table>
        );
    }
  
}

export default AccountTable;