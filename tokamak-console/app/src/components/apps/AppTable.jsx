import React from "react";
const Timestamp = require("react-timestamp");

class AppTable extends React.Component {

    render() {
        return (
            <table className="display-table">
                <thead>
                    <tr>
                      <th className="dth left-pad-0">App</th>
                      <th className="dth">ID</th>
                      <th className="dth">Created</th>
                      <th className="dth right-pad-0"></th>
                    </tr>
                </thead>
                <tbody>  
                    {this.props.apps.map((app) => 
                     <tr>
                       <td className="dtr left-pad-0">
                         {app.name}<br/>
                         <span className="description">{app.description}</span>
                       </td>
                       <td className="dtr">{app.id}</td>
                       <td className="dtr"><Timestamp time={app.created/1000} format="full" /></td>
                       <td className="dtr right-pad-0"> 
                         <i className="fa fa-times inline-button"></i>
                         <i className="fa fa-pencil inline-button" style={{marginRight:"5px"}}></i> 
                       </td>
                     </tr>
                    )}
                </tbody>
            </table>
        );
    }
  
}

export default AppTable;