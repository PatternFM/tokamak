import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";

const Timestamp = require("react-timestamp");

class AccountTable extends React.Component {

    render() {
        return (
            <MuiThemeProvider>
                <Table className="display-table" showCheckboxes={false} selectable={false}>
                  <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
                    <TableRow>
                      <TableHeaderColumn className="dth left-pad-0">Account ID</TableHeaderColumn>
                      <TableHeaderColumn className="dth">Username</TableHeaderColumn>
                      <TableHeaderColumn className="dth right-pad-0">Created</TableHeaderColumn>
                    </TableRow>
                  </TableHeader>
                  
                  <TableBody displayRowCheckbox={false}>
                    {this.props.accounts.map((account) => 
                     <TableRow key={account.id}>
                       <TableRowColumn className="dtr left-pad-0">{account.id}</TableRowColumn>
                       <TableRowColumn className="dtr">{account.username}</TableRowColumn>
                       <TableRowColumn className="dtr right-pad-0"><Timestamp time={account.created/1000} format="full" /></TableRowColumn>
                     </TableRow>
                    )}
                  </TableBody>
                </Table>
            </MuiThemeProvider>
        );
    }
  
}

export default AccountTable;