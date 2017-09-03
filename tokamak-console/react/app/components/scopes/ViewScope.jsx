import React from "react";
import ManageScopeDialog from "./ManageScopeDialog.jsx";
import DeleteScopeDialog from "./DeleteScopeDialog.jsx";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';

const Timestamp = require("react-timestamp");

class ViewScope extends React.Component {

    render() {
        return (
          <div className="view-container">
             <div className="view-header">
             
             </div>
             
             <table className="details-table">
                <tbody>
                  <tr>
                    <td>Name</td>
                    <td>{this.props.scope.name}</td>
                  </tr>   
                  <tr>
                    <td>Description</td>
                    <td>{this.props.scope.description}</td>
                  </tr> 
                  <tr>
                    <td>Created</td>
                    <td>123</td>
                  </tr> 
                  <tr>
                    <td>Updated</td>
                    <td>123</td>
                  </tr> 
                  <tr>
                    <td>ID</td>
                    <td>{this.props.scope.id}</td>
                  </tr>                                                                                         
                </tbody>
             </table>
             
             
          </div>
        );
    }
  
}

export default ViewScope;