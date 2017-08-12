import React from 'react';
import Dialog from 'material-ui/Dialog';
import AccountService from "../../services/AccountService";

class DeleteAccountDialog extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
            error: null,
            account: null,
            loading: false,
            open: false
        };
    }

    show(account) {
        this.setState({ open:true });
        this.setState({ account:account });
    }

    hide() {
        this.setState({ open:false });
        this.setState({ account:null });
        this.setState({ error:null });
    }

    delete() {        
        this.setState({ loading:true });
        
        let self = this;
        AccountService.delete(self.state.account).then(function(result) {
            if(result.status === "accepted") {
                self.props.accountDeleted(result.instance);
                self.hide();
                self.setState({ account:null });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
        });
    }

    render() {
        let name = this.state.account ? this.state.account.username : "";
        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">Delete Account</div>
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

export default DeleteAccountDialog;