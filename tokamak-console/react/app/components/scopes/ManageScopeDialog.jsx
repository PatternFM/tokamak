import React from 'react';
import Dialog from 'material-ui/Dialog';
import ScopeService from "../../services/ScopeService";
import TextField from 'material-ui/TextField';
import { MuiThemeProvider } from 'material-ui/styles';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import FontIcon from 'material-ui/FontIcon';
import RaisedButton from 'material-ui/RaisedButton';

class CreateScopeForm extends React.Component {
    propTypes: {
        scopeCreated: React.PropTypes.func,
        scopeUpdated: React.PropTypes.func
    }
    
    constructor(props) {
        super(props);
        
        this.state = {
            name: "",
            description: "",
            error: "",
            scope_id: "",
            loading: false,
            update: false,
            open: false
        };
    }

    show(scope) {
        if(scope) {
            this.setState({ scope_id:scope.id, name:scope.name, description:scope.description, update:true });
        }
        this.setState({ open:true });
    }

    hide() {
        this.setState({ error:null });    
        this.setState({open:false});
    }

    nameChanged(event) {
        this.setState({ name:event.target.value });
    }

    descriptionChanged(event) {
        this.setState({ description:event.target.value });
    }

    create() {        
        this.setState({ loading:true });
        
        let self = this;
        ScopeService.create(this.state.name, this.state.description).then(function(result) {
            if(result.status === "accepted") {
                self.props.scopeCreated(result.instance);
                self.hide();
                self.setState({ scope_id:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
        });
    }

    update() {        
        this.setState({ loading:true });

        let self = this;
        ScopeService.update(this.state.scope_id, this.state.name, this.state.description).then(function(result) {
            if(result.status === "accepted") {
                self.props.scopeUpdated(result.instance);
                self.hide();
                self.setState({ scope_id:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
        });
    }


    render() {
        let title = this.state.update ? "Edit Scope" : "Create Scope";
        let button = this.state.update ? <RaisedButton primary={true} onClick={() => this.update()} className="mui-button-standard margin-top-40 margin-bottom-20 margin-right-10" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Update"></RaisedButton> : <RaisedButton primary={true} onClick={() => this.create()} className="mui-button-standard margin-top-40 margin-bottom-20 margin-right-10" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Create"></RaisedButton>;

        const buttonTheme = getMuiTheme({
           palette: {
             primary1Color: "#F44336",
             accent1Color: "#DDDDDD"
           }
        });

        const inputTheme = getMuiTheme({
           palette: {
             primary1Color: "#0088FF"
           }
        });
        
        let warn = "#FB8C00";

        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">{title}</div>
              
              <MuiThemeProvider muiTheme={inputTheme}>
                <div className="modal-form-container">
                  {this.state.error && this.state.error.length > 0 &&
                    <div className="validation-error margin-top-40">
                      <div className="warn"><FontIcon className="material-icons" color={warn}>warning</FontIcon></div>
                      <p>{this.state.error}</p>
                      <br style={{clear:"both"}} />
                    </div>
                  } 
                
                  <TextField style={{width:"100%"}} id="name" floatingLabelText="Scope Name" value={this.state.name} onChange={this.nameChanged.bind(this)} />
                  <TextField style={{width:"100%"}} id="description" multiLine={true} floatingLabelText="Description" value={this.state.description} onChange={this.descriptionChanged.bind(this)} />
                </div>
              </MuiThemeProvider>
              
              <MuiThemeProvider muiTheme={buttonTheme}>
                <div style={{textAlign:"center", paddingBottom:"30px"}}>
                  {button}
                  <RaisedButton secondary={true} onClick={() => this.hide()} className="mui-button-standard margin-top-40 margin-bottom-20" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto", display:"inline-block", padding:"20px", color:"#333"}} overlayStyle={{height:"auto",borderRadius:"3px", color:"#333"}} label="Cancel"></RaisedButton>
                  {this.state.loading && <div className="progress modal-progress"><div className="indeterminate"></div></div> }
                </div>
              </MuiThemeProvider>
            </Dialog>
        );
    }
    
}

export default CreateScopeForm;