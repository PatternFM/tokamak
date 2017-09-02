import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AppTable from "./AppTable.jsx";
import { NavLink } from "react-router-dom";
import ManageClientDialog from "./ManageClientDialog.jsx";
import FontIcon from 'material-ui/FontIcon';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import RaisedButton from 'material-ui/RaisedButton';

class ViewApps extends React.Component {

    render() {
        const buttonTheme = getMuiTheme({
            palette: {
                primary1Color: "#F44336"
            }
        });
        
        let moreThanOneResult = this.props.apps.payload && this.props.apps.payload.length > 0;
        
        return (
            <div>
              <ManageClientDialog ref="manageClientDialog" clientCreated={this.props.clientCreated} />
              
              {!moreThanOneResult &&
                <div className="full-page-notice">
                  <h2>You haven't created any apps yet</h2>
                  <MuiThemeProvider muiTheme={buttonTheme}>
                    <RaisedButton primary={true} onClick={() => this.refs.manageClientDialog.show()} className="mui-button-fixed margin-top-40 margin-bottom-20" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Create App"></RaisedButton>
                  </MuiThemeProvider>  
                </div>
              }
            
              {moreThanOneResult &&
                <div className="results-panel">
                  <MuiThemeProvider>
                    <div>
                      <div id="header">
                        <h2>OAuth Apps</h2>
                        <MuiThemeProvider muiTheme={buttonTheme}>
                          <FloatingActionButton className="overlay-add-button" onClick={() => this.refs.manageClientDialog.show()}>
                            <ContentAdd />
                          </FloatingActionButton>   
                        </MuiThemeProvider>                    
                      </div>
                      <AppTable apps={this.props.apps} clientClicked={this.props.clientClicked} clientUpdated={this.props.clientUpdated} clientDeleted={this.props.clientDeleted} pageRequested={this.props.pageRequested} />
                    </div>
                  </MuiThemeProvider>
                </div>
               }            
            </div>
        );
    }
  
}

export default ViewApps;