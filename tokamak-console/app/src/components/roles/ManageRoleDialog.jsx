import React from 'react';
import Dialog from 'material-ui/Dialog';
import RoleService from "../../services/RoleService";
import Button from "../ui-controls/Button.jsx";

class CreateRoleForm extends React.Component {
    propTypes: {
        roleCreated: React.PropTypes.func,
        roleUpdated: React.PropTypes.func
    }
    
    constructor(props) {
        super(props);
        
        this.state = {
            name: "",
            description: "",
            error: "",
            role_id: "",
            loading: false,
            update: false,
            open: false
        };
    }

    show(role) {
        if(role) {
            this.setState({ role_id:role.id, name:role.name, description:role.description, update:true });
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
        RoleService.create(this.state.name, this.state.description).then(function(result) {
           setTimeout(function() {
            if(result.status === "accepted") {
                self.props.roleCreated(result.instance);
                self.hide();
                self.setState({ role_id:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
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
        RoleService.update(this.state.role_id, this.state.name, this.state.description).then(function(result) {
          setTimeout(function() {
            if(result.status === "accepted") {
                self.props.roleUpdated(result.instance);
                self.hide();
                self.setState({ role_id:"" });
                self.setState({ name:"" });
                self.setState({ description:"" });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
            }, 300);
        });
    }


    render() {
        let title = this.state.update ? "Update Role" : "Create Role";
        let button = this.state.update ? <Button loading={this.state.loading} className="login-button center" name="Update" onClick={ () => this.update() } /> : <Button loading={this.state.loading} className="login-button center" name="Create" onClick={ () => this.create() } />;

        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">{title}</div>
              
              {this.state.error && this.state.error.length > 0 &&
                 <div className="validation-error">{this.state.error}</div>
              }        
              
              <table style={{width:"100%", padding:"0 60px 30px 60px"}}>
                <tr>
                  <td className="form-key">Role Name</td>
                  <td className="form-value"><input autoFocus className="tok-textfield" type="text" name="name" value={this.state.name} onChange={this.nameChanged.bind(this)} autoComplete="off" /></td>
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

export default CreateRoleForm;