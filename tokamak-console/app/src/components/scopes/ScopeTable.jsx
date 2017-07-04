import React from "react";
import ManageScopeDialog from "./ManageScopeDialog.jsx";
import DeleteScopeDialog from "./DeleteScopeDialog.jsx";

const Timestamp = require("react-timestamp");

class AudienceTable extends React.Component {

    render() {
        return (
          <div>
                <ManageScopeDialog ref="manageScopeDialog" scopeUpdated={this.props.scopeUpdated} /> 
                <DeleteScopeDialog ref="deleteScopeDialog" scopeDeleted={this.props.scopeDeleted} />  
                      
                <table className="display-table">
                  <thead>
                    <tr>
                      <th className="dth left-pad-0">Scope</th>
                      <th className="dth">ID</th>
                      <th className="dth">Created</th>
                      <th className="dth right-pad-0"></th>
                    </tr>
                  </thead>
                  
                  <tbody>
                    {this.props.scopes.map((scope) => 
                     <tr>
                       <td className="dtr left-pad-0">
                         {scope.name}<br/>
                         <span className="description">{scope.description}</span>
                       </td>
                       <td className="dtr">{scope.id}</td>
                       <td className="dtr"><Timestamp time={scope.created/1000} format="full" /></td>
                       <td className="dtr right-pad-0"> 
                         <i className="fa fa-times inline-button" onClick={() => this.refs.deleteScopeDialog.show(scope)}></i>
                         <i className="fa fa-pencil inline-button" style={{marginRight:"5px"}} onClick={() => this.refs.manageScopeDialog.show(scope)}></i> 
                       </td>
                     </tr>
                    )}
                  </tbody>
                </table>
          </div>
        );
    }
  
}

export default AudienceTable;