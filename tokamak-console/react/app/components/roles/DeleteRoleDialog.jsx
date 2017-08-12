import React from 'react';
import Dialog from 'material-ui/Dialog';
import RoleService from "../../services/RoleService";

class DeleteRoleDialog extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
            error: null,
            role: null,
            loading: false,
            open: false
        };
    }

    show(role) {
        this.setState({ open:true });
        this.setState({ role:role });
    }

    hide() {
        this.setState({ open:false });
        this.setState({ role:null });
        this.setState({ error:null });
    }

    delete() {        
        this.setState({ loading:true });
        
        let self = this;
        RoleService.delete(self.state.role).then(function(result) {
            if(result.status === "accepted") {
                self.props.roleDeleted(result.instance);
                self.hide();
                self.setState({ role:null });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
        });
    }

    render() {
        let name = this.state.role ? this.state.role.name : "";
        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">Delete Role</div>
              <div className="modal-message">
                 <p>Are you sure you want to delete <strong>{name}</strong>?</p>
              </div>
              
              {this.state.error && this.state.error.length > 0 &&
                 <div className="validation-error">{this.state.error}</div>
              }              
              
              <div style={{textAlign:"center", paddingBottom:"30px"}}>
                <button className="tok-button center" style={{marginRight:"10px"}} onClick={() => this.delete()}>Delete</button>
                <button className="tok-button tok-cancel center" onClick={() => this.hide()}>Cancel</button>
              </div>
            </Dialog>
        );
    }
    
}

export default DeleteRoleDialog;