import React from "react";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import AppTable from "./AppTable.jsx";
import { NavLink } from "react-router-dom";
import ManageClientDialog from "./ManageClientDialog.jsx";
import FontIcon from 'material-ui/FontIcon';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';

class ViewApps extends React.Component {

    render() {
        const buttonTheme = getMuiTheme({
            palette: {
                primary1Color: "#F44336"
            }
        });
        
        let moreThanOneResult = this.props.apps.payload && this.props.apps.payload.length > 0;
        
        return (
            <div className="results-panel">
            
            <ManageClientDialog ref="manageClientDialog" clientCreated={this.props.clientCreated} />
            
            {moreThanOneResult  &&
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
                  
                  <div className="table-container">
                    <AppTable apps={this.props.apps} clientClicked={this.props.clientClicked} clientUpdated={this.props.clientUpdated} clientDeleted={this.props.clientDeleted} pageRequested={this.props.pageRequested} />
                  </div>
                </div>
              </MuiThemeProvider>
            }
            {!moreThanOneResult &&
              <div className="error-page">
                <h2 className="error-title">You haven't created any apps yet</h2>
                <button className="tok-button center margin-top-50" onClick={() => this.refs.manageClientDialog.show()}>Create App</button>
              </div>
            }
            
            </div>
        );
    }
  
}

export default ViewApps;