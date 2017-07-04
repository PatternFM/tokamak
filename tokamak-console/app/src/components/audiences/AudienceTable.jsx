import React from "react";
import ManageAudienceDialog from "./ManageAudienceDialog.jsx";
import DeleteAudienceDialog from "./DeleteAudienceDialog.jsx";

const Timestamp = require("react-timestamp");

class AudienceTable extends React.Component {

    render() {
        return (
          <div>
                <ManageAudienceDialog ref="manageAudienceDialog" audienceUpdated={this.props.audienceUpdated} /> 
                <DeleteAudienceDialog ref="deleteAudienceDialog" audienceDeleted={this.props.audienceDeleted} />
                
                <table className="display-table">
                  <thead>
                    <tr>
                      <th className="dth left-pad-0">Audience</th>
                      <th className="dth">ID</th>
                      <th className="dth">Created</th>
                      <th className="dth right-pad-0"></th>
                    </tr>
                  </thead>
                  
                  <tbody>
                    {this.props.audiences.map((audience) => 
                     <tr>
                       <td className="dtr left-pad-0">
                         {audience.name}<br/>
                         <span className="description">{audience.description}</span>
                       </td>
                       <td className="dtr">{audience.id}</td>
                       <td className="dtr"><Timestamp time={audience.created/1000} format="full" /></td>
                       <td className="dtr right-pad-0"> 
                         <i className="fa fa-times inline-button" onClick={() => this.refs.deleteAudienceDialog.show(audience)}></i>
                         <i className="fa fa-pencil inline-button" style={{marginRight:"5px"}} onClick={() => this.refs.manageAudienceDialog.show(audience)}></i> 
                       </td>
                     </tr>
                    )}
                  </tbody>
                </table>
          </div>
        );
    }
  
}

export default AudienceTable;