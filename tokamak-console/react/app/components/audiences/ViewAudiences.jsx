import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AudienceTable from "./AudienceTable.jsx";
import { NavLink } from "react-router-dom";
import ManageAudienceDialog from "./ManageAudienceDialog.jsx";
import FontIcon from 'material-ui/FontIcon';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import RaisedButton from 'material-ui/RaisedButton';

class ViewAudiences extends React.Component {

    render() {
        const buttonTheme = getMuiTheme({
            palette: {
                primary1Color: "#F44336"
            }
        });
        
        let moreThanOneResult = this.props.audiences && this.props.audiences.length > 0;
        
        return (
            <div>
              <ManageAudienceDialog ref="manageAudienceDialog" audienceCreated={this.props.audienceCreated} />
              
              {!moreThanOneResult &&
                <div className="full-page-notice">
                  <h2>You haven't created any audiences yet</h2>
                  <MuiThemeProvider muiTheme={buttonTheme}>
                    <RaisedButton primary={true} onClick={() => this.refs.manageAudienceDialog.show()} className="mui-button-fixed margin-top-40 margin-bottom-20" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Create Audience"></RaisedButton>
                  </MuiThemeProvider>  
                </div>
              }
            
              {moreThanOneResult &&
                <div className="results-panel">
                  <MuiThemeProvider>
                    <div>
                      <div id="header">
                        <h2>App Audiences</h2>
                        <MuiThemeProvider muiTheme={buttonTheme}>
                          <FloatingActionButton className="overlay-add-button" onClick={() => this.refs.manageAudienceDialog.show()}>
                            <ContentAdd />
                          </FloatingActionButton>   
                        </MuiThemeProvider>                    
                      </div>
                      <AudienceTable audiences={this.props.audiences} audienceClicked={this.props.audienceClicked} audienceUpdated={this.props.audienceUpdated} audienceDeleted={this.props.audienceDeleted} pageRequested={this.props.pageRequested} />
                    </div>
                  </MuiThemeProvider>
                </div>
               }            
            </div>
        );
    }
  
}

export default ViewAudiences;