import React from 'react';
import Dialog from 'material-ui/Dialog';
import ClientService from "../../services/ClientService";
import ScopeService from "../../services/ScopeService.js";

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
            
            scopes:[],
            selectedScopes:[],
            
            audiences:[],
            selectedAudiences:[],
            
            authorities:[],
            selectedAuthorities:[],
            
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
    }

    show(client) {
        if(client) {
            this.setState({ id:client.id, username:client.username, update:true, selectedScopes:client.scopes });
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

    toggle(scope, event) {
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

    create() {        
        this.setState({ loading:true });
        
        let self = this;
        ClientService.create({ clientId:this.state.clientId, clientSecret:this.state.clientSecret, name:this.state.name, description:this.state.description, scopes:this.state.selectedScopes }).then(function(result) {
            if(result.status === "accepted") {
                self.props.clientCreated(result.instance);
                self.hide();
                self.setState({ clientId:"" });
                self.setState({ clientSecret:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
                self.setState({ selectedScopes:[] });
                self.setState({ selectedAudiences:[] });
                self.setState({ selectedAuthorities:[] });
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
        ClientService.update({ id:this.state.id, username:this.state.username, scopes:this.state.selectedScopes }).then(function(result) {
            if(result.status === "accepted") {
                self.props.clientUpdated(result.instance);
                self.hide();
                self.setState({ clientId:"" });
                self.setState({ clientSecret:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
                self.setState({ selectedScopes:[] });
                self.setState({ selectedAudiences:[] });
                self.setState({ selectedAuthorities:[] });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
        });
    }

    isChecked(scope) {
       let result = this.state.selectedScopes.filter(function(obj) {
           return obj.name === scope.name; 
       });
       return result.length !== 0;
    }

    render() {
        let title = this.state.update ? "Update Client" : "Create Client";
        let button = this.state.update ? <button className="tok-button center" style={{marginRight:"10px"}} onClick={ () => this.update() }>Update</button> : <button className="tok-button center" style={{marginRight:"10px"}} onClick={() => this.create()}>Create</button>;
        let clientSecretField = this.state.update ? <div><div className="tok-textfield-disabled" style={{ width:"70%", float:"left" }}>&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;</div><i className="change-password">change password</i></div> : <input className="tok-textfield" type="password" name="name" value={this.state.password} onChange={this.clientSecretChanged.bind(this)} autoComplete="off" />;
        let clientIdField = this.state.update ? <div className="tok-textfield-disabled">{this.state.clientId}</div> : <input autoFocus className="tok-textfield" type="text" name="name" value={this.state.username} onChange={this.clientIdChanged.bind(this)} autoComplete="off" />;

        return (
            <Dialog modal={true} open={this.state.open} autoScrollBodyContent={true}>
              <div className="modal-title">{title}</div>
              
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
              </table>
              
              <div className="form-key" style={{textAlign:"left", width:"100%", padding:"0 65px 0 65px"}}>Scopes</div>
                <table className="display-table select-table">
                  <tbody>
                    {this.state.scopes.map((scope) => 
                     <tr key={scope.id}>
                       <td className="dtr left-pad-0">
                         <input type="checkbox" name={scope.name} onChange={ this.toggle.bind(this, scope) } defaultChecked={this.isChecked(scope)}></input>
                       </td>
                       <td className="dtr left-pad-0" style={{ whiteSpace:"nowrap" }}>
                         {scope.name}
                       </td>
                       <td className="dtr right-pad-0">
                         <span className="description">{scope.description}</span>
                       </td>                       
                     </tr>
                    )}
                  </tbody>
                </table>              
              
              <div style={{textAlign:"center", paddingBottom:"30px"}}>
                {button}
                <button className="tok-button tok-cancel center" onClick={() => this.hide()}>Cancel</button>
              </div>
            </Dialog>
        );
    }
    
}

export default CreateClientForm;