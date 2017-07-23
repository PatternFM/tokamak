import React from 'react';
import Dialog from 'material-ui/Dialog';
import AccountService from "../../services/AccountService";
import RoleService from "../../services/RoleService.js";

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
        console.log("UPDATE");
        this.setState( { updatePassword: true } );
    }

    updatePassword() {        
        this.setState({ loading:true });

        if(this.state.newPassword !== this.state.confirmNewPassword) {
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
        let button = this.state.update ? <button className="tok-button center" style={{marginRight:"10px"}} onClick={ () => this.update() }>Update</button> : <button className="tok-button center" style={{marginRight:"10px"}} onClick={() => this.create()}>Create</button>;
        let usernameField = this.state.update ? <div className="tok-textfield-disabled">{this.state.username}</div> : <input autoFocus className="tok-textfield" type="text" name="name" value={this.state.username} onChange={this.usernameChanged.bind(this)} autoComplete="off" />;
        let passwordField = this.state.update ? <div><div className="tok-textfield-disabled" style={{ width:"68%", float:"left" }}>&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;</div><i className="change-password" onClick={ () => this.changePassword() }>change password</i></div> : <input className="tok-textfield" type="password" name="name" value={this.state.password} onChange={this.passwordChanged.bind(this)} autoComplete="off" />;

        return (
            <Dialog modal={true} contentStyle={{width:"80%", maxWidth:"none"}} open={this.state.open} autoScrollBodyContent={true}>
              {this.state.updatePassword && 
                  <div className="animated fadeIn">
                    <div className="modal-title">Update Password</div>
                    
                    <div style={{width:"50%", float:"left"}}>
                      {this.state.error && this.state.error.length > 0 &&
                        <div className="validation-error">{this.state.error}</div>
                      }        
              
                      <table style={{width:"100%", padding:"0 60px 30px 60px"}}>
                        <tr>
                          <td className="form-key">New Password</td>
                          <td className="form-value"><input className="tok-textfield" type="password" value={this.state.newPassword} onChange={this.newPasswordChanged.bind(this)} autoComplete="off" /></td>
                        </tr>
                        <tr>
                          <td className="form-key">Confirm Password</td>
                          <td className="form-value"><input className="tok-textfield" type="password" value={this.state.confirmNewPassword} onChange={this.confirmNewPasswordChanged.bind(this)} autoComplete="off" /></td>
                        </tr>                                                                                                        
                      </table>
              
                      <div style={{textAlign:"center", paddingBottom:"30px"}}>
                        <button className="tok-button center" style={{marginRight:"10px"}} onClick={ () => this.updatePassword() }>Update</button>
                        <button className="tok-button tok-cancel center" onClick={() => this.cancelUpdatePassword()}>Cancel</button>
                      </div>
                      <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
                    </div>                    
                  </div>
              }            
            
              {!this.state.updatePassword &&
                <div className="animated fadeIn">            
              <div className="modal-title">{title}</div>
              
              <div style={{width:"50%", float:"left"}}>
              {this.state.error && this.state.error.length > 0 &&
                 <div className="validation-error">{this.state.error}</div>
              }        
              
              <table style={{width:"100%", padding:"0 60px 30px 60px"}}>
                <tr>
                  <td className="form-key">Username</td>
                     <td className="form-value">{usernameField}</td>
                </tr>
                <tr>
                  <td className="form-key">Password</td>
                  <td className="form-value">{passwordField}</td>
                </tr>                                
              </table>
              
              <div style={{textAlign:"center", paddingBottom:"30px"}}>
                {button}
                <button className="tok-button tok-cancel center" onClick={() => this.hide()}>Cancel</button>
              </div>              
              </div>
              
              <div style={{width:"50%", height:"600px", overflow:"scroll"}}>
                  <div style={{width:"100%", padding:"0 65px 0 65px"}}>
                    <div className="form-key form-header">Roles</div>
                  </div>
                <table className="display-table select-table">
                  <tbody>
                    {this.state.roles.map((role) => 
                     <tr key={role.id}>
                       <td className="dtr left-pad-0">
                         <input type="checkbox" name={role.name} onChange={ this.toggle.bind(this, role) } defaultChecked={this.isChecked(role)}></input>
                       </td>
                       <td className="dtr left-pad-0" style={{ whiteSpace:"nowrap" }}>
                         {role.name}
                       </td>
                       <td className="dtr right-pad-0">
                         <span className="description">{role.description}</span>
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