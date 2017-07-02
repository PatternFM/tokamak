import React from "react";
const Timestamp = require("react-timestamp");

class AuthorityTable extends React.Component {

    render() {
        return (
                <table className="display-table">
                  <thead>
                    <tr>
                      <th className="dth left-pad-0">Authority</th>
                      <th className="dth">ID</th>
                      <th className="dth">Created</th>
                      <th className="dth right-pad-0"></th>
                    </tr>
                  </thead>
                  
                  <tbody>
                    {this.props.authorities.map((authority) => 
                     <tr key={authority.id}>
                       <td className="dtr left-pad-0">
                         {authority.name}<br/>
                         <span className="description">{authority.description}</span>
                       </td>
                       <td className="dtr">{authority.id}</td>
                       <td className="dtr"><Timestamp time={authority.created/1000} format="full" /></td>
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

export default AuthorityTable;