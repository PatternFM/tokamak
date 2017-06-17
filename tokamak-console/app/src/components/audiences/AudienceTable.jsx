import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";

const Timestamp = require("react-timestamp");

class AudienceTable extends React.Component {

    render() {
        return (
            <MuiThemeProvider>
                <Table className="display-table" showCheckboxes={false} selectable={false}>
                  <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                      <TableHeaderColumn className="dth left-pad-0">ID</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Name</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Description</TableHeaderColumn>
                      <TableHeaderColumn className="dth right-pad-0">Created</TableHeaderColumn>
                    </TableRow>
                  </TableHeader>
                  
                  <TableBody displayRowCheckbox={false}>
                    {this.props.audiences.map((audience) => 
                     <TableRow key={audience.id}>
                       <TableRowColumn className="dtr left-pad-0">{audience.id}</TableRowColumn>
                       <TableRowColumn className="dtr">{audience.name}</TableRowColumn>
                       <TableRowColumn className="dtr">{audience.description}</TableRowColumn>
                       <TableRowColumn className="dtr right-pad-0"><Timestamp time={audience.created/1000} format="full" /></TableRowColumn>
                     </TableRow>
                    )}
                  </TableBody>
                </Table>
            </MuiThemeProvider>
        );
    }
  
}

export default AudienceTable;