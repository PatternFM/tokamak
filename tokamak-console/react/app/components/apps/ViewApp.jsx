import React from "react";
import ManageClientDialog from "./ManageClientDialog.jsx";
import DeleteClientDialog from "./DeleteClientDialog.jsx";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';

const Timestamp = require("react-timestamp");

class ViewApp extends React.Component {

    render() {
        return (
          <div className="view-container">
             <div className="view-header">
             
             </div>
             
             <table>
                <tbody>
                  <tr>
                    <td>Client ID</td>
                    <td>{this.props.app.clientId}</td>
                  </tr> 
                    <tr>
                      <td className="form-key">App Name</td>
                      <td>{this.props.app.name}</td>
                    </tr>  
                    <tr>
                      <td className="form-key">Description</td>
                      <td>{this.props.app.description}</td>
                    </tr>  
                    <tr>
                      <td className="form-key">Access Token TTL</td>
                      <td className="form-value">{this.props.app.accessTokenValiditySeconds}</td>
                    </tr>     
                    <tr>
                      <td className="form-key">Refresh Token TTL</td>
                      <td className="form-value">{this.props.app.refreshTokenValiditySeconds}</td>
                    </tr> 
                    <tr>
                      <td className="form-key">Redirect URL</td>
                      <td className="form-value">{this.props.app.redirectUri}</td>
                    </tr>                   
                </tbody>
             </table>
             
             
          </div>
        );
    }
  
}

export default ViewApp;