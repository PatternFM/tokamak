import React from 'react';
import Dialog from 'material-ui/Dialog';
import AudienceService from "../../services/AudienceService";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import RaisedButton from 'material-ui/RaisedButton';
import FontIcon from 'material-ui/FontIcon';

class DeleteAudienceDialog extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
            error: null,
            audience: null,
            loading: false,
            open: false
        };
    }

    show(audience) {
        this.setState({ open:true });
        this.setState({ audience:audience });
    }

    hide() {
        this.setState({ open:false });
        this.setState({ audience:null });
        this.setState({ error:null });
    }

    delete() {        
        this.setState({ loading:true });
        
        let self = this;
        AudienceService.delete(self.state.audience).then(function(result) {
            if(result.status === "accepted") {
                self.props.audienceDeleted(result.instance);
                self.hide();
                self.setState({ audience:null });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
        });
    }

    render() {
        const buttonTheme = getMuiTheme({
            palette: {
                primary1Color: "#F44336",
                accent1Color: "#DDDDDD"
            }
        });
            
        let name = this.state.audience ? this.state.audience.name : "";
        let warn = "#FB8C00";
           
        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">Delete Audience</div>
              
              {this.state.error && this.state.error.length > 0 &&
                 <div className="validation-error modal-validation-error margin-top-40">
                   <div className="warn"><FontIcon className="material-icons" color={warn}>warning</FontIcon></div>
                   <p>{this.state.error}</p>
                   <br style={{clear:"both"}}/>
                 </div>
              } 
                           
              <p className="modal-message-center">Are you sure you want to delete <strong>{name}</strong>?</p>
              
              <MuiThemeProvider muiTheme={buttonTheme}>
                <div style={{textAlign:"center", paddingBottom:"30px"}}>
                  <RaisedButton primary={true} onClick={() => this.delete()} className="mui-button-standard margin-top-40 margin-bottom-20 margin-right-10" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Delete"></RaisedButton>
                  <RaisedButton secondary={true} onClick={() => this.hide()} className="mui-button-standard margin-top-40 margin-bottom-20" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto", display:"inline-block", padding:"20px", color:"#333"}} overlayStyle={{height:"auto",borderRadius:"3px", color:"#333"}} label="Cancel"></RaisedButton>
                  {this.state.loading && <div className="progress modal-progress"><div className="indeterminate"></div></div> }
                </div>
              </MuiThemeProvider>
            </Dialog>
        );
    }
    
}

export default DeleteAudienceDialog;