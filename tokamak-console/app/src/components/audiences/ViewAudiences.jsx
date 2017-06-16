import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";

const Timestamp = require("react-timestamp");

class ViewAudiences extends React.Component {

    render() {
        return (
            <MuiThemeProvider>
                <div className="table-container">
                <h1>App Audiences</h1>
                <Table className="display-table" showCheckboxes={false} selectable={false}>
                  <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                      <TableHeaderColumn className="dth">ID</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Name</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Description</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Created</TableHeaderColumn>
                    </TableRow>
                  </TableHeader>
                  
                  <TableBody displayRowCheckbox={false}>
                    {this.props.audiences.map((audience) => 
                     <TableRow key={audience.id}>
                       <TableRowColumn className="dtr">{audience.id}</TableRowColumn>
                       <TableRowColumn className="dtr">{audience.name}</TableRowColumn>
                       <TableRowColumn className="dtr">{audience.description}</TableRowColumn>
                       <TableRowColumn className="dtr"><Timestamp time={audience.created/1000} format="full" /></TableRowColumn>
                     </TableRow>
                    )}
                  </TableBody>
                </Table>
                </div>
            </MuiThemeProvider>
        );
    }
  
}

export default ViewAudiences;