import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

class ViewScopes extends React.Component {
  render() {
    return (
        <MuiThemeProvider>
            <div>scope!</div>
        </MuiThemeProvider>
    );
  }
}

export default ViewScopes;