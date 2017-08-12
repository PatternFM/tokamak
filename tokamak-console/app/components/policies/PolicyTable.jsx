import React from "react";
import ManagePolicyDialog from "./ManagePolicyDialog.jsx";

const Timestamp = require("react-timestamp");

class PolicyTable extends React.Component {

    render() {
        return (
          <div>
                <ManagePolicyDialog ref="managePolicyDialog" policyUpdated={this.props.policyUpdated} /> 
                
                <table className="display-table">
                  <thead>
                    <tr>
                      <th className="dth left-pad-0">Policy</th>
                      <th className="dth">ID</th>
                      <th className="dth">Created</th>
                      <th className="dth right-pad-0"></th>
                    </tr>
                  </thead>
                  
                  <tbody>
                    {this.props.policies.map((policy) => 
                     <tr key={policy.id}>
                       <td className="dtr left-pad-0">
                         {policy.name}<br/>
                         <span className="description">{policy.description}</span>
                       </td>
                       <td className="dtr">{policy.id}</td>
                       <td className="dtr"><Timestamp time={policy.created/1000} format="full" /></td>
                       <td className="dtr right-pad-0"> 
                         <i className="fa fa-pencil inline-button" style={{marginRight:"5px"}} onClick={ () => this.refs.managePolicyDialog.show(policy) }></i> 
                       </td>
                     </tr>
                    )}
                  </tbody>
                </table>
          </div>
        );
    }
  
}

export default PolicyTable;