import React from "react";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';
import FontIcon from 'material-ui/FontIcon';

const Timestamp = require("react-timestamp");

class AudienceTable extends React.Component {

    viewAudienceDetails(audience) {
        this.props.audienceClicked(audience);
    }

    render() {
        return (
          <div>
            <div className="scrollable-results">
              {this.props.audiences.map((audience) => 
                <div className="result" key={audience.id} onClick={ () => this.viewAudienceDetails(audience) }>
                    <h4>{audience.name}</h4> 
                    <p>{audience.description}</p>
                </div>
              )}
            
              <Pagination records={this.props.audiences} pageRequested={this.props.pageRequested} />
              <br/><br/><br/><br/><br/><br/><br/><br/>
              <br/><br/><br/><br/><br/><br/><br/><br/>
            </div>
          </div>
        );
    }
  
}

export default AudienceTable;