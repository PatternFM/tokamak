import React from 'react';
import Dialog from 'material-ui/Dialog';
import ClientService from "../../services/ClientService";
import ScopeService from "../../services/ScopeService.js";
import AudienceService from "../../services/AudienceService.js";
import AuthorityService from "../../services/AuthorityService.js";
import GrantTypeService from "../../services/GrantTypeService.js";
import Button from "../ui-controls/Button.jsx";

class CreateClientForm extends React.Component {
    propTypes: {
        clientCreated: React.PropTypes.func,
        clientUpdated: React.PropTypes.func
    }
    
    constructor(props) {
        super(props);
        
        this.state = {
            clientId:"",
            clientSecret:"",
            name:"",
            description:"",
            accessTokenValiditySeconds: null,
            refreshTokenValiditySeconds: null,
            redirectUri:"",
            
            scopes:[],
            selectedScopes:[],
            
            audiences:[],
            selectedAudiences:[],
            
            authorities:[],
            selectedAuthorities:[],
            
            grantTypes:[],
            selectedGrantTypes:[],
            
            error: "",
            loading: false,
            update: false,
            open: false
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        
        ScopeService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ scopes:result.instance.scopes }, function() { } );
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
        
        AuthorityService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ authorities:result.instance.authorities }, function() { } );
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        }); 
        
        AudienceService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ audiences:result.instance.audiences }, function() { } );
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        }); 
        
        GrantTypeService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ grantTypes:result.instance.grantTypes }, function() { } );
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });                        
    }

    show(client) {
        if(client) {
            this.setState({ id:client.id, clientId:client.clientId, name:client.name, description:client.description, update:true, accessTokenValiditySeconds: client.accessTokenValiditySeconds, refreshTokenValiditySeconds: client.refreshTokenValiditySeconds, redirectUri: client.redirectUri, selectedScopes:client.scopes, selectedAuthorities:client.authorities, selectedAudiences:client.audiences, selectedGrantTypes:client.grantTypes });
        }
        this.setState({ open:true });
    }

    hide() {
        this.setState({ open:false });
    }

    clientIdChanged(event) {
        this.setState({ clientId:event.target.value });
    }

    clientSecretChanged(event) {
        this.setState({ clientSecret:event.target.value });
    }

    nameChanged(event) {
        this.setState({ name:event.target.value });
    }

    descriptionChanged(event) {
        this.setState({ description:event.target.value });
    }

    accessTokenValiditySecondsChanged(event) {
        this.setState({ accessTokenValiditySeconds:event.target.value });
    }

    refreshTokenValiditySecondsChanged(event) {
        this.setState({ refreshTokenValiditySeconds:event.target.value });
    }

    redirectUriChanged(event) {
        this.setState({ redirectUri:event.target.value });
    }

    toggleScope(scope, event) {
        let selected = this.state.selectedScopes.slice();
        let index = selected.findIndex(function(obj) { return obj.name === event.target.name; });

        if(index === -1) {
            selected.push(scope);
        }
        else {
            selected.splice(index, 1);
        }
        
        this.setState({ selectedScopes:selected });
    }

    toggleAudience(audience, event) {
        let selected = this.state.selectedAudiences.slice();
        let index = selected.findIndex(function(obj) { return obj.name === event.target.name; });

        if(index === -1) {
            selected.push(audience);
        }
        else {
            selected.splice(index, 1);
        }
        
        this.setState({ selectedAudiences:selected });
    }

    toggleAuthority(authority, event) {
        let selected = this.state.selectedAuthorities.slice();
        let index = selected.findIndex(function(obj) { return obj.name === event.target.name; });

        if(index === -1) {
            selected.push(authority);
        }
        else {
            selected.splice(index, 1);
        }
        
        this.setState({ selectedAuthorities:selected });
    }

    toggleGrantType(grantType, event) {
        let selected = this.state.selectedGrantTypes.slice();
        let index = selected.findIndex(function(obj) { return obj.name === event.target.name; });

        if(index === -1) {
            selected.push(grantType);
        }
        else {
            selected.splice(index, 1);
        }
        
        this.setState({ selectedGrantTypes:selected });
    }

    create() {        
        this.setState({ loading:true });
        
        let self = this;
        ClientService.create({ clientId:this.state.clientId, clientSecret:this.state.clientSecret, name:this.state.name, description:this.state.description, accessTokenValiditySeconds: this.state.accessTokenValiditySeconds, refreshTokenValiditySeconds: this.state.refreshTokenValiditySeconds, redirectUri: this.state.redirectUri, scopes:this.state.selectedScopes, grantTypes:this.state.selectedGrantTypes, authorities:this.state.selectedAuthorities, audiences:this.state.selectedAudiences }).then(function(result) {
            setTimeout(function() {
            if(result.status === "accepted") {
                self.setState({ clientId:"" });
                self.setState({ clientSecret:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
                self.setState({ selectedScopes:[] });
                self.setState({ selectedAudiences:[] });
                self.setState({ selectedAuthorities:[] });
                self.setState({ selectedGrantTypes:[] });
                self.props.clientCreated(result.instance);
                self.hide();
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
            }, 300);
        });
    }

    update() {        
        this.setState({ loading:true });

        let self = this;
        ClientService.update({ id:this.state.id, name:this.state.name, description:this.state.description, accessTokenValiditySeconds: this.state.accessTokenValiditySeconds, refreshTokenValiditySeconds: this.state.refreshTokenValiditySeconds, redirectUri: this.state.redirectUri, scopes:this.state.selectedScopes, authorities:this.state.selectedAuthorities, grantTypes:this.state.selectedGrantTypes, audiences:this.state.selectedAudiences }).then(function(result) {
          setTimeout(function() {
            if(result.status === "accepted") {
                self.setState({ clientId:"" });
                self.setState({ clientSecret:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
                self.setState({ selectedScopes:[] });
                self.setState({ selectedAudiences:[] });
                self.setState({ selectedAuthorities:[] });
                self.setState({ selectedGrantTypes:[] });
                self.props.clientUpdated(result.instance);
                self.hide();
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
          }, 300);
        });
    }

    isScopeChecked(scope) {
       let result = this.state.selectedScopes.filter(function(obj) {
           return obj.name === scope.name; 
       });
       return result.length !== 0;
    }

    isGrantTypeChecked(grantType) {
       let result = this.state.selectedGrantTypes.filter(function(obj) {
           return obj.name === grantType.name; 
       });
       return result.length !== 0;
    }

    isAuthorityChecked(authority) {
       let result = this.state.selectedAuthorities.filter(function(obj) {
           return obj.name === authority.name; 
       });
       return result.length !== 0;
    }

    isAudienceChecked(audience) {
       let result = this.state.selectedAudiences.filter(function(obj) {
           return obj.name === audience.name; 
       });
       return result.length !== 0;
    }

    render() {
        let title = this.state.update ? "Update App" : "Create App";
        let button = this.state.update ? <button className="tok-button center" style={{marginRight:"10px"}} onClick={ () => this.update() }>Update</button> : <button className="tok-button center" style={{marginRight:"10px"}} onClick={() => this.create()}>Create</button>;
        let clientSecretField = this.state.update ? <div><div className="tok-textfield-disabled" style={{ width:"70%", float:"left" }}>&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;</div><i className="change-password">change secret</i></div> : <input className="tok-textfield" type="password" name="clientSecret" onChange={this.clientSecretChanged.bind(this)} autoComplete="off" />;
        let clientIdField = this.state.update ? <div className="tok-textfield-disabled">{this.state.clientId}</div> : <input autoFocus className="tok-textfield" type="text" name="name" value={this.state.username} onChange={this.clientIdChanged.bind(this)} autoComplete="off" />;

        return (
            <Dialog modal={true} contentStyle={{width:"80%", maxWidth:"none"}} open={this.state.open}>
              <div className="modal-title">{title}</div>
              
              <div style={{width:"50%", float:"left"}}>
              {this.state.error && this.state.error.length > 0 &&
                 <div className="validation-error">{this.state.error}</div>
              }        
              
              <table style={{width:"100%", padding:"0 60px 30px 60px"}}>
                <tr>
                  <td className="form-key">Client ID</td>
                  <td className="form-value">{clientIdField}</td>
                </tr>
                <tr>
                  <td className="form-key">Client Secret</td>
                  <td className="form-value">{clientSecretField}</td>
                </tr>  
                <tr>
                  <td className="form-key">App Name</td>
                  <td className="form-value"><input className="tok-textfield" type="text" name="name" value={this.state.name} onChange={this.nameChanged.bind(this)} autoComplete="off" /></td>
                </tr>  
                <tr>
                  <td className="form-key">Description</td>
                  <td><textarea className="tok-textfield" name="description" value={this.state.description} onChange={this.descriptionChanged.bind(this)} autoComplete="off" /></td>
                </tr>  
                <tr>
                  <td className="form-key">Access Token TTL</td>
                  <td className="form-value"><input className="tok-textfield" type="text" name="accessTokenValiditySeconds" value={this.state.accessTokenValiditySeconds} onChange={this.accessTokenValiditySecondsChanged.bind(this)} autoComplete="off" /></td>
                </tr>     
                <tr>
                  <td className="form-key">Refresh Token TTL</td>
                  <td className="form-value"><input className="tok-textfield" type="text" name="refreshTokenValiditySeconds" value={this.state.refreshTokenValiditySeconds} onChange={this.refreshTokenValiditySecondsChanged.bind(this)} autoComplete="off" /></td>
                </tr> 
                <tr>
                  <td className="form-key">Redirect URL</td>
                  <td className="form-value"><input className="tok-textfield" type="text" name="redirectUri" value={this.state.redirectUri} onChange={this.redirectUriChanged.bind(this)} autoComplete="off" /></td>
                </tr>                                                                                                       
              </table>
              
              <div style={{textAlign:"center", paddingBottom:"30px"}}>
{button}
<button className="tok-button tok-cancel center" onClick={() => this.hide()}>Cancel</button>
</div>
              
              
              </div>
              
              <div style={{width:"50%", height:"600px", overflow:"scroll"}}>
<div style={{width:"100%", padding:"0 65px 0 65px"}}>
     <div className="form-key form-header">Grant Types</div>
</div>
<table className="display-table select-table">
  <tbody>
    {this.state.grantTypes.map((grantType) => 
     <tr key={grantType.id}>
       <td className="dtr left-pad-0" style={{ width:"20px" }}>
         <input type="checkbox" name={grantType.name} onChange={ this.toggleGrantType.bind(this, grantType) } defaultChecked={this.isGrantTypeChecked(grantType)}></input>
       </td>
       <td className="dtr left-pad-0" style={{ whiteSpace:"nowrap", width:"200px" }}>
         {grantType.name}
       </td>
       <td className="dtr right-pad-0">
         <span className="description">{grantType.description}</span>
       </td>                       
     </tr>
    )}
  </tbody>
</table>  

<div style={{width:"100%", padding:"0 65px 0 65px"}}>
     <div className="form-key form-header">Scopes</div>
</div>
<table className="display-table select-table">
  <tbody>
    {this.state.scopes.map((scope) => 
     <tr key={scope.id}>
       <td className="dtr left-pad-0" style={{ width:"20px" }}>
         <input type="checkbox" name={scope.name} onChange={ this.toggleScope.bind(this, scope) } defaultChecked={this.isScopeChecked(scope)}></input>
       </td>
       <td className="dtr left-pad-0" style={{ whiteSpace:"nowrap", width:"200px"}}>
         {scope.name}
       </td>
       <td className="dtr right-pad-0">
         <span className="description">{scope.description}</span>
       </td>                       
     </tr>
    )}
  </tbody>
</table>              

{this.state.authorities.length > 0 &&
<div>
  <div style={{width:"100%", padding:"0 65px 0 65px"}}>
     <div className="form-key form-header">Authorities</div>
  </div>
<table className="display-table select-table">
  <tbody>
    {this.state.authorities.map((authority) => 
     <tr key={authority.id}>
       <td className="dtr left-pad-0" style={{ width:"20px" }}>
         <input type="checkbox" name={authority.name} onChange={ this.toggleAuthority.bind(this, authority) } defaultChecked={this.isAuthorityChecked(authority)}></input>
       </td>
       <td className="dtr left-pad-0" style={{ whiteSpace:"nowrap", width:"200px" }}>
         {authority.name}
       </td>
       <td className="dtr right-pad-0">
         <span className="description">{authority.description}</span>
       </td>                       
     </tr>
    )}
  </tbody>
</table> 
</div>
}

{this.state.audiences.length > 0 &&
<div>
  <div style={{width:"100%", padding:"0 65px 0 65px"}}>
     <div className="form-key form-header">Audiences</div>
  </div>
  <table className="display-table select-table">
    <tbody>
    {this.state.audiences.map((audience) => 
     <tr key={audience.id}>
       <td className="dtr left-pad-0" style={{ width:"20px" }}>
         <input type="checkbox" name={audience.name} onChange={ this.toggleAudience.bind(this, audience) } defaultChecked={this.isAudienceChecked(audience)}></input>
       </td>
       <td className="dtr left-pad-0" style={{ whiteSpace:"nowrap", width:"200px" }}>
         {audience.name}
       </td>
       <td className="dtr right-pad-0">
         <span className="description">{audience.description}</span>
       </td>                       
     </tr>
    )}
    </tbody>
  </table>  
</div>
}               
              </div>
            </Dialog>
        );
    }
    
}

export default CreateClientForm;