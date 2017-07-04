import React from 'react';
import Dialog from 'material-ui/Dialog';
import AuthorityService from "../../services/AuthorityService";

class CreateAuthorityForm extends React.Component {
    propTypes: {
        authorityCreated: React.PropTypes.func,
        authorityUpdated: React.PropTypes.func
    }
    
    constructor(props) {
        super(props);
        
        this.state = {
            name: "",
            description: "",
            error: "",
            authority_id: "",
            loading: false,
            update: false,
            open: false
        };
    }

    show(authority) {
        if(authority) {
            this.setState({ authority_id:authority.id, name:authority.name, description:authority.description, update:true });
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
        AuthorityService.create(this.state.name, this.state.description).then(function(result) {
            if(result.status === "accepted") {
                self.props.authorityCreated(result.instance);
                self.hide();
                self.setState({ authority_id:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
            }
            if(result.status === "rejected") {
                self.setState({error: result.message});
            }
            self.setState({ loading:false });
        });
    }

    update() {        
        this.setState({ loading:true });

        let self = this;
        AuthorityService.update(this.state.authority_id, this.state.name, this.state.description).then(function(result) {
            if(result.status === "accepted") {
                self.props.authorityUpdated(result.instance);
                self.hide();
                self.setState({ authority_id:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
            }
            if(result.status === "rejected") {
                self.setState({error: result.message});
            }
            self.setState({ loading:false });
        });
    }


    render() {
        let title = this.state.update ? "Update Authority" : "Create Authority";
        let button = this.state.update ? <button className="tok-button center" style={{marginRight:"10px"}} onClick={ () => this.update() }>Update</button> : <button className="tok-button center" style={{marginRight:"10px"}} onClick={() => this.create()}>Create</button>;

        let inputv = this.state.update ? <div className="tok-textfield-disabled">{this.state.name}</div> : <input autoFocus className="tok-textfield" type="text" name="name" value={this.state.name} onChange={this.nameChanged.bind(this)} autoComplete="off" />;

        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">{title}</div>
              
              <table style={{width:"100%", padding:"0 60px 30px 60px"}}>
                <tr>
                  <td className="form-key">Authority Name</td>
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

export default CreateAuthorityForm;