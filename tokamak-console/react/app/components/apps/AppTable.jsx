import React from "react";
import ManageClientDialog from "./ManageClientDialog.jsx";
import DeleteClientDialog from "./DeleteClientDialog.jsx";
import Pagination from "../pagination/Pagination.jsx";

const Timestamp = require("react-timestamp");

class AppTable extends React.Component {

    render() {
        return (
          <div>
            <ManageClientDialog ref="manageClientDialog" clientUpdated={this.props.clientUpdated} /> 
            <DeleteClientDialog ref="deleteClientDialog" clientDeleted={this.props.clientDeleted} />
          
            <table className="display-table">
                <thead>
                    <tr>
                      <th className="dth left-pad-0">App</th>
                      <th className="dth">ID</th>
                      <th className="dth">Created</th>
                      <th className="dth right-pad-0"></th>
                    </tr>
                </thead>
                <tbody>  
                    {this.props.apps.payload.map((app) => 
                     <tr>
                       <td className="dtr left-pad-0">
                         {app.name}<br/>
                         <span className="description">{app.description}</span>
                       </td>
                       <td className="dtr">{app.id}</td>
                       <td className="dtr"><Timestamp time={app.created/1000} format="full" /></td>
                       <td className="dtr right-pad-0"> 
                         <i className="fa fa-times inline-button" onClick={ () => this.refs.deleteClientDialog.show(app) }></i>
                         <i className="fa fa-pencil inline-button" style={{marginRight:"5px"}} onClick={ () => this.refs.manageClientDialog.show(app) }></i> 
                       </td>
                     </tr>
                    )}
                </tbody>
            </table>
            
            <Pagination records={this.props.apps} pageRequested={this.props.pageRequested} />
          </div>
        );
    }
  
}

export default AppTable;