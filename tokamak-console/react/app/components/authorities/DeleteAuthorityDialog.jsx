import React from 'react';
import Dialog from 'material-ui/Dialog';
import AuthorityService from "../../services/AuthorityService";

class DeleteAuthorityDialog extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
            error: null,
            authority: null,
            loading: false,
            open: false
        };
    }

    show(authority) {
        this.setState({ open:true });
        this.setState({ authority:authority });
    }

    hide() {
        this.setState({ open:false });
        this.setState({ authority:null });
        this.setState({ error:null });        
    }

    delete() {        
        this.setState({ loading:true });
        
        let self = this;
        AuthorityService.delete(self.state.authority).then(function(result) {
            if(result.status === "accepted") {
                self.props.authorityDeleted(result.instance);
                self.hide();
                self.setState({ authority:null });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
        });
    }

    render() {
        let name = this.state.authority ? this.state.authority.name : "";
        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">Delete Authority</div>
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

export default DeleteAuthorityDialog;