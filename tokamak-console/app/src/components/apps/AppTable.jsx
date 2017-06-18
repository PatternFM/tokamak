import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";

const Timestamp = require("react-timestamp");

class AppTable extends React.Component {

    render() {
        return (
            <MuiThemeProvider>
                <Table className="display-table" showCheckboxes={false} selectable={false}>
                  <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                      <TableHeaderColumn className="dth left-pad-0">Client ID</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Name</TableHeaderColumn>
                      <TableHeaderColumn className="dth right-pad-0">Created</TableHeaderColumn>
                    </TableRow>
                  </TableHeader>
                  
                  <TableBody displayRowCheckbox={false}>
                    {this.props.apps.map((app) => 
                     <TableRow key={app.id}>
                       <TableRowColumn className="dtr left-pad-0">{app.id}</TableRowColumn>
                       <TableRowColumn className="dtr">{app.name}</TableRowColumn>
                       <TableRowColumn className="dtr right-pad-0"><Timestamp time={app.created/1000} format="full" /></TableRowColumn>
                     </TableRow>
                    )}
                  </TableBody>
                </Table>
            </MuiThemeProvider>
        );
    }
  
}

export default AppTable;