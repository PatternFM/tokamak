import React from 'react';
import Dialog from 'material-ui/Dialog';
import ScopeService from "../../services/ScopeService";

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
        let title = this.state.update ? "Update Scope" : "Create Scope";
        let button = this.state.update ? <button className="tok-button center" style={{marginRight:"10px"}} onClick={ () => this.update() }>Update</button> : <button className="tok-button center" style={{marginRight:"10px"}} onClick={() => this.create()}>Create</button>;
        let inputv = this.state.update ? <div className="tok-textfield-disabled">{this.state.name}</div> : <input autoFocus className="tok-textfield" type="text" name="name" value={this.state.name} onChange={this.nameChanged.bind(this)} autoComplete="off" />;

        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">{title}</div>
              
              {this.state.error && this.state.error.length > 0 &&
                 <div className="validation-error">{this.state.error}</div>
              }        
              
              <table style={{width:"100%", padding:"0 60px 30px 60px"}}>
                <tr>
                  <td className="form-key">Scope Name</td>
                  <td className="form-value">{inputv}</td>
                </tr>
                <tr>
                  <td className="form-key">Description</td>
                  <td><textarea className="tok-textfield" name="description" value={this.state.description} onChange={this.descriptionChanged.bind(this)} autoComplete="off" /></td>
                </tr>                
              </table>
              
              <div style={{textAlign:"center", paddingBottom:"30px"}}>
                {button}
                <button className="tok-button tok-cancel center" onClick={() => this.hide()}>Cancel</button>
              </div>
            </Dialog>
        );
    }
    
}

export default CreateScopeForm;