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
             
             <table>
                <tbody>
                  <tr>
                    <td>Scope ID</td>
                    <td>123</td>
                  </tr>                   
                </tbody>
             </table>
             
             
          </div>
        );
    }
  
}

export default ViewScope;