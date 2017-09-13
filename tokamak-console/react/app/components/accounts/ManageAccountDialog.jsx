import React from 'react';
import Dialog from 'material-ui/Dialog';
import TextField from 'material-ui/TextField';
import AccountService from "../../services/AccountService";
import RoleService from "../../services/RoleService.js";
import PasswordPolicyService from "../../services/PasswordPolicyService.js";
import { MuiThemeProvider } from 'material-ui/styles';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import FontIcon from 'material-ui/FontIcon';
import RaisedButton from 'material-ui/RaisedButton';

class CreateAccountForm extends React.Component {
    propTypes: {
        accountCreated: React.PropTypes.func,
        accountUpdated: React.PropTypes.func
    }
    
    constructor(props) {
        super(props);
        
        this.state = {
            username:"",
            password:"",
            
            roles:[],
            selectedRoles:[],
            
            newPassword:"",
            confirmNewPassword:"",            
            
            error: "",
            loading: false,
            update: false,
            open: false
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        RoleService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ roles:result.instance.roles }, function() { } );
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
        
        PasswordPolicyService.findByName("account-password-policy").then((result) => {
            if(result.status === "accepted") {
                this.setState({ policy:result.instance }, function() { } );
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });        
    }

    show(account) {
        if(account) {
            this.setState({ id:account.id, username:account.username, update:true, selectedRoles:account.roles });
        }
        this.setState({ open:true });
    }

    hide() {
        this.setState({ error:null });
        this.setState({ open:false });
    }

    usernameChanged(event) {
        this.setState({ username:event.target.value });
    }

    passwordChanged(event) {
        this.setState({ password:event.target.value });
    }

    newPasswordChanged(event) {
        this.setState({ newPassword:event.target.value });
    }

    confirmNewPasswordChanged(event) {
        this.setState({ confirmNewPassword:event.target.value });
    }

    toggle(role, event) {
        let selected = this.state.selectedRoles.slice();
        let index = selected.findIndex(function(obj) { return obj.name === event.target.name; });

        if(index === -1) {
            selected.push(role);
        }
        else {
            selected.splice(index, 1);
        }
        
        this.setState({ selectedRoles:selected });
    }

    create() {        
        this.setState({ loading:true });
        
        let self = this;
        AccountService.create({ username:this.state.username, password:this.state.password, roles:this.state.selectedRoles }).then(function(result) {
            if(result.status === "accepted") {
                self.props.accountCreated(result.instance);
                self.hide();
                self.setState({ username:"" });
                self.setState({ password:"" });
                self.setState({ selectedRoles:[] });
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
        AccountService.update({ id:this.state.id, username:this.state.username, roles:this.state.selectedRoles }).then(function(result) {
            if(result.status === "accepted") {
                self.props.accountUpdated(result.instance);
                self.hide();
                self.setState({ username:"" });
                self.setState({ password:"" });
                self.setState({ selectedRoles:[] });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
        });
    }

    changePassword() {
        this.setState( { updatePassword: true } );
    }

    updatePassword() {        
        this.setState({ loading:true });

        if(this.state.newPassword !== this.state.confirmNewPassword) {
            this.setState({ loading:false });
            this.setState({ error:"Your passwords do not match." });
            return;
        }

        let self = this;
        AccountService.updatePassword({ id:this.state.id }, this.state.newPassword).then(function(result) {
          setTimeout(function() {
            if(result.status === "accepted") {
                self.setState({ updatePassword: false });
                self.setState({ newPassword:"" });
                self.setState({ confirmNewPassword:"" });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
          }, 300);
        });
    }

    cancelUpdatePassword() {
         this.setState( { error: "" } );
         this.setState( { updatePassword: false } );
         this.setState( { newPassword: "" } );
         this.setState( { confirmNewPassword: "" } );
    }

    isChecked(role) {
       let result = this.state.selectedRoles.filter(function(obj) {
           return obj.name === role.name; 
       });
       return result.length !== 0;
    }

    render() {
        let title = this.state.update ? "Update Account" : "Create Account";
        let button = this.state.update ? <RaisedButton primary={true} onClick={() => this.update()} className="mui-button-standard margin-top-40 margin-bottom-20 margin-right-10" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Update"></RaisedButton> : <RaisedButton primary={true} onClick={() => this.create()} className="mui-button-standard margin-top-40 margin-bottom-20 margin-right-10" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Create"></RaisedButton>;
        let usernameField = this.state.update ? <TextField style={{width:"100%"}} id="username" disabled={true} floatingLabelText="Username" value={this.state.username} onChange={this.usernameChanged.bind(this)} /> : <TextField style={{width:"100%"}} id="name" floatingLabelText="Username" value={this.state.username} onChange={this.usernameChanged.bind(this)} />;
        let passwordField = this.state.update ? <div><TextField style={{width:"70%"}} id="password" disabled={true} floatingLabelText="Password" value="&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;" onChange={this.usernameChanged.bind(this)} /><i className="change-password" onClick={ () => this.changePassword() }>change password</i></div> : <TextField style={{width:"100%"}} id="password" floatingLabelText="Password" value={this.state.password} onChange={this.passwordChanged.bind(this)} />;

        let warn = "#FB8C00";

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

        return (
            <Dialog modal={true} contentStyle={{width:"80%", maxWidth:"none"}} open={this.state.open} autoScrollBodyContent={true}>
              {this.state.updatePassword && 
                  <div className="animated fadeIn">
                    <div className="modal-title">Update Password</div>
                    
                    <div style={{width:"50%", float:"left"}}>
                      {this.state.error && this.state.error.length > 0 &&
                        <div className="validation-error margin-top-40 margin-left-50 margin-right-50">
                          <div className="warn"><FontIcon className="material-icons" color={warn}>warning</FontIcon></div>
                          <p>{this.state.error}</p>
                          <br style={{clear:"both"}} />
                        </div>
                      }        
              
                      <MuiThemeProvider muiTheme={inputTheme}>
                        <div style={{padding:"15px 100px"}}>
                          <TextField style={{width:"100%"}} type="password" floatingLabelText="New Password" value={this.state.newPassword} onChange={this.newPasswordChanged.bind(this)} />
                          <TextField style={{width:"100%"}} type="password" floatingLabelText="Confirm Password" value={this.state.confirmNewPassword} onChange={this.confirmNewPasswordChanged.bind(this)} />
                        </div>
                      </MuiThemeProvider> 
                      
                      <MuiThemeProvider muiTheme={buttonTheme}>
                        <div style={{textAlign:"center", paddingBottom:"30px"}}>
                          <RaisedButton primary={true} onClick={() => this.updatePassword()} className="mui-button-standard margin-top-40 margin-bottom-20 margin-right-10" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto",display:"inline-block",padding:"20px"}} overlayStyle={{height:"auto",borderRadius:"3px"}} label="Update"></RaisedButton>
                          <RaisedButton secondary={true} onClick={() => this.cancelUpdatePassword()} className="mui-button-standard margin-top-40 margin-bottom-20" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto", display:"inline-block", padding:"20px", color:"#333"}} overlayStyle={{height:"auto",borderRadius:"3px", color:"#333"}} label="Cancel"></RaisedButton>
                          {this.state.loading && <div className="progress modal-progress"><div className="indeterminate"></div></div> }
                        </div>
                      </MuiThemeProvider>                      
                      
                      <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
                    </div>  
                    
                <div style={{width:"50%", height:"600px", overflow:"scroll"}}>
                  <div style={{width:"100%", padding:"25px 65px 0 65px"}}>
                    <div className="select-table-header">Password Policy</div>
                  </div>
                  
                  <table className="display-table select-table">
                    <tbody>
                      <tr>
                        <td className="dtr left-pad-0">The password must be at least {this.state.policy.minLength} characters long.</td>
                      </tr>
                      {this.state.policy.requireSpecialCharacter &&
                      <tr>
                        <td className="dtr left-pad-0">At least one special character is required.</td>
                      </tr>
                      }
                      {this.state.policy.requireNumericCharacter &&
                      <tr>
                        <td className="dtr left-pad-0">At least one numeric character is required.</td>
                      </tr>
                      }     
                      {this.state.policy.requireLowercaseCharacter &&
                      <tr>
                        <td className="dtr left-pad-0">At least one lowercase character is required.</td>
                      </tr>
                      }  
                      {this.state.policy.requireUppercaseCharacter &&
                      <tr>
                        <td className="dtr left-pad-0">At least one uppercase character is required.</td>
                      </tr>
                      }       
                      {this.state.policy.rejectCommonPasswords   &&
                      <tr>
                        <td className="dtr left-pad-0">Common passwords are not allowed.</td>
                      </tr>
                      }                       
                    </tbody>
                  </table>
                </div>                     
              </div>
              }            
            
              {!this.state.updatePassword &&
                <div className="animated fadeIn">            
                  <div className="modal-title">{title}</div>
              
                  <div style={{width:"50%", float:"left"}}>
                    {this.state.error && this.state.error.length > 0 &&
                      <div className="validation-error margin-top-40 margin-left-50 margin-right-50">
                        <div className="warn"><FontIcon className="material-icons" color={warn}>warning</FontIcon></div>
                        <p>{this.state.error}</p>
                        <br style={{clear:"both"}} />
                      </div>
                    }        
              
                    <div style={{padding:"15px 100px"}}>
                      <MuiThemeProvider muiTheme={inputTheme}>
                        <div>
                          {usernameField}
                          {passwordField}
                        </div>
                      </MuiThemeProvider>
              
                      <MuiThemeProvider muiTheme={buttonTheme}>
                        <div style={{textAlign:"center", paddingBottom:"30px"}}>
                          {button}
                          <RaisedButton secondary={true} onClick={() => this.hide()} className="mui-button-standard margin-top-40 margin-bottom-20" disabledBackgroundColor="rgba(0,0,0,0.12)" disabledLabelColor="#999" buttonStyle={{height:"auto",lineHeight:"auto"}} labelStyle={{height:"auto", display:"inline-block", padding:"20px", color:"#333"}} overlayStyle={{height:"auto",borderRadius:"3px", color:"#333"}} label="Cancel"></RaisedButton>
                          {this.state.loading && <div className="progress modal-progress"><div className="indeterminate"></div></div> }
                        </div>
                      </MuiThemeProvider>
                    </div>
                  </div>
              
                  <div style={{width:"50%", height:"600px", overflow:"scroll"}}>
                    <div style={{width:"100%", padding:"0 65px 0 65px", marginTop:"20px"}}>
                      <div className="select-table-header">Roles</div>
                    </div>
                    <table className="display-table select-table">
                      <tbody>
                        {this.state.roles.map((role) => 
                          <tr key={role.id}>
                            <td className="dtr left-pad-0" style={{ width:"20px" }}>
                              <input type="checkbox" name={role.name} onChange={ this.toggle.bind(this, role) } defaultChecked={this.isChecked(role)}></input>
                            </td>
                            <td className="dtr left-pad-0">
                              {role.name}
                            </td>                       
                          </tr>
                        )}
                      </tbody>
                    </table>              
                  </div>
                </div>
            }
            </Dialog>
        );
    }
    
}

export default CreateAccountForm;