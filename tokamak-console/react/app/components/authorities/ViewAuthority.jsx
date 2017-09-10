import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import ManageAuthorityDialog from "./ManageAuthorityDialog.jsx";
import DeleteAuthorityDialog from "./DeleteAuthorityDialog.jsx";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';
import Moment from 'moment';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import FontIcon from 'material-ui/FontIcon';
import ActionDelete from 'material-ui/svg-icons/action/delete';
import ActionEdit from 'material-ui/svg-icons/content/create';

const Timestamp = require("react-timestamp");

class ViewAuthority extends React.Component {

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
            <ManageAuthorityDialog ref="manageAuthorityDialog" authorityUpdated={this.props.authorityUpdated} /> 
            <DeleteAuthorityDialog ref="deleteAuthorityDialog" authorityDeleted={this.props.authorityDeleted} />
             <div className="view-header">
               <h2>{this.props.authority.name}</h2>
               <MuiThemeProvider muiTheme={buttonHeaderTheme}>
                 <div className="actions">
                   <FlatButton onClick={() => this.refs.manageAuthorityDialog.show(this.props.authority)} className="margin-right-10" label="Edit Authority" style={{color:"#EEE"}} labelStyle={{color:"#EEE"}} icon={<ActionEdit />}></FlatButton>
                   <FlatButton onClick={() => this.refs.deleteAuthorityDialog.show(this.props.authority)} label="Delete Authority" style={{color:"#EEE"}} labelStyle={{color:"#EEE"}} icon={<ActionDelete />}></FlatButton>
                 </div>
               </MuiThemeProvider>    
             </div>
             
             <table className="details-table" class="details-table">
                <tbody>
                  <tr>
                    <td className="key">Name</td>
                    <td>{this.props.authority.name}</td>
                  </tr>   
                  <tr>
                    <td className="key">Description</td>
                    <td>{this.props.authority.description}</td>
                  </tr> 
                  <tr>
                    <td className="key">Created</td>
                    <td>{Moment(this.props.authority.created).format('MMMM Do YYYY, h:mm a')}</td>
                  </tr> 
                  <tr>
                    <td className="key">Updated</td>
                    <td>{Moment(this.props.authority.updated).format('MMMM Do YYYY, h:mm a')}</td>
                  </tr> 
                  <tr>
                    <td className="key">ID</td>
                    <td>{this.props.authority.id}</td>
                  </tr>                                                                                         
                </tbody>
             </table>
             
          </div>
        );
    }
  
}

export default ViewAuthority;