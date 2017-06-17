import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import AudienceTable from "./AudienceTable.jsx";

class ViewAudiences extends React.Component {

    render() {
        console.log("audiences? " + this.props.audiences);
        
        return (
            <div className="animated fadeIn">
            {this.props.audiences && this.props.audiences.length > 0 &&
              <MuiThemeProvider>
                <div>
                  <div id="header"><div className="title">App Audiences</div></div>
                  <div className="table-container">
                    <AudienceTable audiences={this.props.audiences} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!this.props.audiences || this.props.audiences.length === 0 &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any audiences yet</h2>
                <button className="tok-button center margin-top-50">Create Audience</button>
              </div>
            }
            </div>
        );
    }
  
}

export default ViewAudiences;