import React from 'react';
import Dialog from 'material-ui/Dialog';
import AudienceService from "../../services/AudienceService";

class DeleteAudienceDialog extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
            audience: null,
            loading: false,
            open: false
        };
    }

    show(audience) {
        this.setState({ open:true });
        this.setState({ audience:audience });
    }

    hide() {
        this.setState({ open:false });
    }

    delete() {        
        this.setState({ loading:true });
        
        let self = this;
        AudienceService.delete(self.state.audience).then(function(result) {
            if(result.status === "accepted") {
                self.props.audienceDeleted(result.instance);
                self.hide();
                self.setState({ audience:null });
            }
            if(result.status === "rejected") {
                self.setState({ error:result.message });
            }
            self.setState({ loading:false });
        });
    }

    render() {
        let name = this.state.audience ? this.state.audience.name : "";
        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">Delete Audience</div>
              <div className="modal-message">
                 <p>Are you sure you want to delete <strong>{name}</strong>?</p>
              </div>
              <div style={{textAlign:"center", paddingBottom:"30px"}}>
                <button className="tok-button center" style={{marginRight:"10px"}} onClick={() => this.delete()}>Delete</button>
                <button className="tok-button tok-cancel center" onClick={() => this.hide()}>Cancel</button>
              </div>
            </Dialog>
        );
    }
    
}

export default DeleteAudienceDialog;