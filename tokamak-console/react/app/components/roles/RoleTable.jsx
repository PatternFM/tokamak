import React from "react";
import ManageRoleDialog from "./ManageRoleDialog.jsx";
import DeleteRoleDialog from "./DeleteRoleDialog.jsx";

const Timestamp = require("react-timestamp");

class RoleTable extends React.Component {

    render() {
        return (
          <div>
                <ManageRoleDialog ref="manageRoleDialog" roleUpdated={this.props.roleUpdated} /> 
                <DeleteRoleDialog ref="deleteRoleDialog" roleDeleted={this.props.roleDeleted} />
                
                <table className="display-table">
                  <thead>
                    <tr>
                      <th className="dth left-pad-0">Role</th>
                      <th className="dth">ID</th>
                      <th className="dth">Created</th>
                      <th className="dth right-pad-0"></th>
                    </tr>
                  </thead>
                  
                  <tbody>
                    {this.props.roles.map((role) => 
                     <tr key={role.id}>
                       <td className="dtr left-pad-0">
                         {role.name}<br/>
                         <span className="description">{role.description}</span>
                       </td>
                       <td className="dtr">{role.id}</td>
                       <td className="dtr"><Timestamp time={role.created/1000} format="full" /></td>
                       <td className="dtr right-pad-0"> 
                         <i className="fa fa-times inline-button" onClick={ () => this.refs.deleteRoleDialog.show(role) }></i>
                         <i className="fa fa-pencil inline-button" style={{marginRight:"5px"}} onClick={ () => this.refs.manageRoleDialog.show(role) }></i> 
                       </td>
                     </tr>
                    )}
                  </tbody>
                </table>
          </div>
        );
    }
  
}

export default RoleTable;