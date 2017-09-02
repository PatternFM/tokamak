import React from "react";
import ManageScopeDialog from "./ManageScopeDialog.jsx";
import DeleteScopeDialog from "./DeleteScopeDialog.jsx";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';
import FontIcon from 'material-ui/FontIcon';

const Timestamp = require("react-timestamp");

class ScopeTable extends React.Component {

    viewScopeDetails(scope) {
        this.props.scopeClicked(scope);
    }

    render() {
        return (
          <div>
            <ManageScopeDialog ref="manageScopeDialog" scopeUpdated={this.props.scopeUpdated} /> 
            <DeleteScopeDialog ref="deleteScopeDialog" scopeDeleted={this.props.scopeDeleted} />
          
            <div className="scrollable-results">
              {this.props.scopes.map((scope) => 
                <div className="result" key={scope.id}>
                    <h4 onClick={ () => this.viewScopeDetails(scope) }>{scope.name}</h4> 
                    <p>{scope.description}</p>
                </div>
              )}
            
              <Pagination records={this.props.scopes} pageRequested={this.props.pageRequested} />
              <br/><br/><br/><br/><br/><br/><br/><br/>
              <br/><br/><br/><br/><br/><br/><br/><br/>
            </div>
          </div>
        );
    }
  
}

export default ScopeTable;