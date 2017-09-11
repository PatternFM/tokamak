import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import ManageClientDialog from "./ManageClientDialog.jsx";
import DeleteClientDialog from "./DeleteClientDialog.jsx";
import Moment from "moment";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';
import RaisedButton from "material-ui/RaisedButton";
import FlatButton from "material-ui/FlatButton";
import FontIcon from "material-ui/FontIcon";
import ActionDelete from "material-ui/svg-icons/action/delete";
import ActionEdit from "material-ui/svg-icons/content/create";

const Timestamp = require("react-timestamp");

class ViewApp extends React.Component {

    render() {
        const buttonTheme = getMuiTheme({
            palette: {
                primary1Color: "#F44336"
            }
        });

        const buttonHeaderTheme = getMuiTheme({
            palette: {
                primary1Color: "#FFFFFF"
            }
        });
            
        return (
          <div className="view-container">
            <ManageClientDialog ref="manageClientDialog" clientUpdated={this.props.clientUpdated} /> 
            <DeleteClientDialog ref="deleteClientDialog" clientDeleted={this.props.clientDeleted} />
             <div className="view-header">
               <h2>{this.props.app.name}</h2>
               <MuiThemeProvider muiTheme={buttonHeaderTheme}>
                 <div className="actions">
                   <FlatButton onClick={() => this.refs.manageClientDialog.show(this.props.app)} className="margin-right-10" label="Edit App" style={{color:"#EEE"}} labelStyle={{color:"#EEE"}} icon={<ActionEdit />}></FlatButton>
                   <FlatButton onClick={() => this.refs.deleteClientDialog.show(this.props.app)} label="Delete App" style={{color:"#EEE"}} labelStyle={{color:"#EEE"}} icon={<ActionDelete />}></FlatButton>
                 </div>
               </MuiThemeProvider>              
             </div>
             
             <table className="details-table">
                <tbody>
                    <tr>
                      <td className="key">App Name</td>
                      <td>{this.props.app.name}</td>
                    </tr>  
                    <tr>
                      <td className="key">Description</td>
                      <td>{this.props.app.description}</td>
                    </tr> 
                  <tr>
                    <td className="key">Client ID</td>
                    <td>{this.props.app.clientId}</td>
                  </tr>
                  <tr>
                    <td className="key">Client Secret</td>
                    <td>&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;</td>
                  </tr>                      
                    <tr>
                      <td className="key">Access Token TTL</td>
                      <td className="form-value">{this.props.app.accessTokenValiditySeconds}</td>
                    </tr>     
                    <tr>
                      <td className="key">Refresh Token TTL</td>
                      <td className="form-value">{this.props.app.refreshTokenValiditySeconds}</td>
                    </tr> 
                    <tr>
                      <td className="key">Redirect URL</td>
                      <td>{this.props.app.redirectUri}</td>
                    </tr> 
                  <tr>
                    <td className="key">Scopes</td>
                    <td>
                      {this.props.app.scopes.map((scope) => 
                        <div className="list-item">{scope.name}</div>
                      )}
                    </td>
                  </tr> 
                  <tr>
                    <td className="key">Authorities</td>
                    <td>
                      {this.props.app.authorities.map((authority) => 
                        <div className="list-item">{authority.name}</div>
                      )}
                    </td>
                  </tr> 
                  <tr>
                    <td className="key">Audiences</td>
                    <td>
                      {this.props.app.audiences.map((audience) => 
                        <div className="list-item">{audience.name}</div>
                      )}
                    </td>
                  </tr>                                                         
                  <tr>
                    <td className="key">Created</td>
                    <td>{Moment(this.props.app.created).format("MMMM Do YYYY, h:mm a")}</td>
                  </tr> 
                  <tr>
                    <td className="key">Updated</td>
                    <td>{Moment(this.props.app.updated).format("MMMM Do YYYY, h:mm a")}</td>
                  </tr> 
                  <tr>
                    <td className="key">ID</td>
                    <td>{this.props.app.id}</td>
                  </tr>                                     
                </tbody>
             </table>
             
             
          </div>
        );
    }
  
}

export default ViewApp;