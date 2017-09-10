import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AuthorityTable from "./AuthorityTable.jsx";
import { NavLink } from "react-router-dom";
import ManageAuthorityDialog from "./ManageAuthorityDialog.jsx";
import FontIcon from 'material-ui/FontIcon';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import RaisedButton from 'material-ui/RaisedButton';

class ViewAuthorities extends React.Component {

    render() {
        const buttonTheme = getMuiTheme({
            palette: {
                primary1Color: "#F44336"
            }
        });
        
        let moreThanOneResult = this.props.authorities && this.props.authorities.length > 0;
        
        return (
            <div>
              <ManageAuthorityDialog ref="manageAuthorityDialog" authorityCreated={this.props.authorityCreated} />
              
              {!moreThanOneResult &&
                <div className="full-page-notice">
                  <h2>You haven't created any authorities yet</h2>
                  <MuiThemeProvider muiTheme={buttonTheme}>
                    <RaisedButton primary={true} onClick={() => this.refs.manageAuthorityDialog.show()} className="mui-button-fixed margin-top-40 margin-bottom-20" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Create Authority"></RaisedButton>
                  </MuiThemeProvider>  
                </div>
              }
            
              {moreThanOneResult &&
                <div className="results-panel">
                  <MuiThemeProvider>
                    <div>
                      <div id="header">
                        <h2>App Authorities</h2>
                        <MuiThemeProvider muiTheme={buttonTheme}>
                          <FloatingActionButton className="overlay-add-button" onClick={() => this.refs.manageAuthorityDialog.show()}>
                            <ContentAdd />
                          </FloatingActionButton>   
                        </MuiThemeProvider>                    
                      </div>
                      <AuthorityTable authorities={this.props.authorities} authorityClicked={this.props.authorityClicked} authorityUpdated={this.props.authorityUpdated} authorityDeleted={this.props.authorityDeleted} pageRequested={this.props.pageRequested} />
                    </div>
                  </MuiThemeProvider>
                </div>
               }            
            </div>
        );
    }
  
}

export default ViewAuthorities;