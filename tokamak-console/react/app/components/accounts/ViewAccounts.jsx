import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AccountTable from "./AccountTable.jsx";
import { NavLink } from "react-router-dom";
import ManageAccountDialog from "./ManageAccountDialog.jsx";
import FontIcon from 'material-ui/FontIcon';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import RaisedButton from 'material-ui/RaisedButton';

class ViewAccounts extends React.Component {

    render() {
        const buttonTheme = getMuiTheme({
            palette: {
                primary1Color: "#F44336"
            }
        });
        
        let moreThanOneResult = this.props.accounts.payload && this.props.accounts.payload.length > 0;
        
        return (
            <div>
              <ManageAccountDialog ref="manageAccountDialog" accountCreated={this.props.accountCreated} />
              
              {!moreThanOneResult &&
                <div className="full-page-notice">
                  <h2>You haven't created any accounts yet</h2>
                  <MuiThemeProvider muiTheme={buttonTheme}>
                    <RaisedButton primary={true} onClick={() => this.refs.manageAccountDialog.show()} className="mui-button-fixed margin-top-40 margin-bottom-20" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Create Account"></RaisedButton>
                  </MuiThemeProvider>  
                </div>
              }
            
              {moreThanOneResult &&
                <div className="results-panel">
                  <MuiThemeProvider>
                    <div>
                      <div id="header">
                        <h2>User Accounts</h2>
                        <MuiThemeProvider muiTheme={buttonTheme}>
                          <FloatingActionButton className="overlay-add-button" onClick={() => this.refs.manageAccountDialog.show()}>
                            <ContentAdd />
                          </FloatingActionButton>   
                        </MuiThemeProvider>                    
                      </div>
                      <AccountTable accounts={this.props.accounts} accountClicked={this.props.accountClicked} accountUpdated={this.props.accountUpdated} accountDeleted={this.props.accountDeleted} pageRequested={this.props.pageRequested} />
                    </div>
                  </MuiThemeProvider>
                </div>
               }            
            </div>
        );
    }
  
}

export default ViewAccounts;