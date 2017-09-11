import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import ManageAccountDialog from "./ManageAccountDialog.jsx";
import DeleteAccountDialog from "./DeleteAccountDialog.jsx";
import Pagination from "../pagination/Pagination.jsx";
import Paper from "material-ui/Paper";
import Moment from "moment";
import RaisedButton from "material-ui/RaisedButton";
import FlatButton from "material-ui/FlatButton";
import FontIcon from "material-ui/FontIcon";
import ActionDelete from "material-ui/svg-icons/action/delete";
import ActionEdit from "material-ui/svg-icons/content/create";

const Timestamp = require("react-timestamp");

class ViewAccount extends React.Component {

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
            <ManageAccountDialog ref="manageAccountDialog" accountUpdated={this.props.accountUpdated} /> 
            <DeleteAccountDialog ref="deleteAccountDialog" accountDeleted={this.props.accountDeleted} />
             <div className="view-header">
               <h2>{this.props.account.username}</h2>
               <MuiThemeProvider muiTheme={buttonHeaderTheme}>
                 <div className="actions">
                   <FlatButton onClick={() => this.refs.manageAccountDialog.show(this.props.account)} className="margin-right-10" label="Edit Account" style={{color:"#EEE"}} labelStyle={{color:"#EEE"}} icon={<ActionEdit />}></FlatButton>
                   <FlatButton onClick={() => this.refs.deleteAccountDialog.show(this.props.account)} label="Delete Account" style={{color:"#EEE"}} labelStyle={{color:"#EEE"}} icon={<ActionDelete />}></FlatButton>
                 </div>
               </MuiThemeProvider>    
             </div>
             
             <table className="details-table">
                <tbody>
                  <tr>
                    <td className="key">Username</td>
                    <td>{this.props.account.username}</td>
                  </tr>   
                  <tr>
                    <td className="key">Password</td>
                    <td>&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;</td>
                  </tr>
                  <tr>
                    <td className="key">Roles</td>
                    <td>
                      {this.props.account.roles.map((role) => 
                        <div className="list-item">{role.name}</div>
                      )}
                    </td>
                  </tr>
                  <tr>
                    <td className="key">Created</td>
                    <td>{Moment(this.props.account.created).format("MMMM Do YYYY, h:mm a")}</td>
                  </tr> 
                  <tr>
                    <td className="key">Updated</td>
                    <td>{Moment(this.props.account.updated).format("MMMM Do YYYY, h:mm a")}</td>
                  </tr> 
                  <tr>
                    <td className="key">ID</td>
                    <td>{this.props.account.id}</td>
                  </tr>                                                                                         
                </tbody>
             </table>
             
          </div>
        );
    }
  
}

export default ViewAccount;