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
                      <TableHeaderColumn className="dth left-pad-0">Name</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Description</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Scope ID</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Created</TableHeaderColumn>
                      <TableHeaderColumn className="dth right-pad-0" style={{width:"60px"}}></TableHeaderColumn>
                    </TableRow>
                  </TableHeader>
                  
                  <TableBody displayRowCheckbox={false}>
                    {this.props.scopes.map((scope) => 
                     <TableRow key={scope.id}>
                       <TableRowColumn className="dtr left-pad-0">{scope.name}</TableRowColumn>
                       <TableRowColumn className="dtr">{scope.description}</TableRowColumn>
                       <TableRowColumn className="dtr">{scope.id}</TableRowColumn>
                       <TableRowColumn className="dtr"><Timestamp time={scope.created/1000} format="full" /></TableRowColumn>
                       <TableRowColumn className="dtr right-pad-0" style={{width:"60px"}}><i className="fa fa-times inline-button"></i></TableRowColumn>
                     </TableRow>
                    )}
                  </TableBody>
                </Table>
            </MuiThemeProvider>
        );
    }
  
}

export default AudienceTable;