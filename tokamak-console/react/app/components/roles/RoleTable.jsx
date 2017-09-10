import React from "react";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';
import FontIcon from 'material-ui/FontIcon';

const Timestamp = require("react-timestamp");

class RoleTable extends React.Component {

    viewRoleDetails(role) {
        this.props.roleClicked(role);
    }

    render() {
        return (
          <div>
            <div className="scrollable-results">
              {this.props.roles.map((role) => 
                <div className="result" key={role.id} onClick={ () => this.viewRoleDetails(role) }>
                    <h4>{role.name}</h4> 
                    <p>{role.description}</p>
                </div>
              )}
            
              <Pagination records={this.props.roles} pageRequested={this.props.pageRequested} />
              <br/><br/><br/><br/><br/><br/><br/><br/>
              <br/><br/><br/><br/><br/><br/><br/><br/>
            </div>
          </div>
        );
    }
  
}

export default RoleTable;