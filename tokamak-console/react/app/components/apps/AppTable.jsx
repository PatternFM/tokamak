import React from "react";
import ManageClientDialog from "./ManageClientDialog.jsx";
import DeleteClientDialog from "./DeleteClientDialog.jsx";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';
import FontIcon from 'material-ui/FontIcon';

const Timestamp = require("react-timestamp");

class AppTable extends React.Component {

    viewClientDetails(client) {
        this.props.clientClicked(client);
    }

    render() {
        return (
          <div>
            <ManageClientDialog ref="manageClientDialog" clientUpdated={this.props.clientUpdated} /> 
            <DeleteClientDialog ref="deleteClientDialog" clientDeleted={this.props.clientDeleted} />
          
            <div className="scrollable-results">
              {this.props.apps.payload.map((app) => 
                <div className="result" key={app.id}>
                    <h4 onClick={ () => this.viewClientDetails(app) }>{app.name}</h4> 
                    <p>{app.description}</p>
                </div>
              )}
            
              <Pagination records={this.props.apps} pageRequested={this.props.pageRequested} />
              <br/><br/><br/><br/><br/><br/><br/><br/>
              <br/><br/><br/><br/><br/><br/><br/><br/>
            </div>
          </div>
        );
    }
  
}

export default AppTable;