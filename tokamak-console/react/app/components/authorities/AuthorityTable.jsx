import React from "react";
import Pagination from "../pagination/Pagination.jsx";
import Paper from 'material-ui/Paper';
import FontIcon from 'material-ui/FontIcon';

const Timestamp = require("react-timestamp");

class AuthorityTable extends React.Component {

    viewAuthorityDetails(authority) {
        this.props.authorityClicked(authority);
    }

    render() {
        return (
          <div>
            <div className="scrollable-results">
              {this.props.authorities.map((authority) => 
                <div className="result" key={authority.id} onClick={ () => this.viewAuthorityDetails(authority) }>
                    <h4>{authority.name}</h4> 
                    <p>{authority.description}</p>
                </div>
              )}
            
              <Pagination records={this.props.authorities} pageRequested={this.props.pageRequested} />
              <br/><br/><br/><br/><br/><br/><br/><br/>
              <br/><br/><br/><br/><br/><br/><br/><br/>
            </div>
          </div>
        );
    }
  
}

export default AuthorityTable;