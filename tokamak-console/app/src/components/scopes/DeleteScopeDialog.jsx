import React from 'react';
import Dialog from 'material-ui/Dialog';
import ScopeService from "../../services/ScopeService";

class DeleteScopeDialog extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
            error: null,
            scope: null,
            loading: false,
            open: false
        };
    }

    show(scope) {
        this.setState({ open:true });
        this.setState({ scope:scope });
    }

    hide() {
        this.setState({ open:false });
        this.setState({ scope:null });
        this.setState({ error:null });
    }

    delete() {        
        this.setState({ loading:true });
        
        let self = this;
        ScopeService.delete(self.state.scope).then(function(result) {
            if(result.status === "accepted") {
                self.props.scopeDeleted(result.instance);
                self.hide();
                self.setState({ scope:null });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.errors[0].message });
            }
            self.setState({ loading:false });
        });
    }

    render() {
        let name = this.state.scope ? this.state.scope.name : "";
        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">Delete Scope</div>
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

export default DeleteScopeDialog;