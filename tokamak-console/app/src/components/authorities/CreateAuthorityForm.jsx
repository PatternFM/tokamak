import React from 'react';
import Dialog from 'material-ui/Dialog';
import AuthorityService from "../../services/AuthorityService";

class CreateAuthorityForm extends React.Component {
    propTypes: {
        authorityCreated: React.PropTypes.func
    }
    
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            description: "",
            error: "",
            loading: false,
            open: false
        };
    }

    handleOpen() {
        this.setState({open:true});
    }

    handleClose() {
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
                self.handleClose();
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
        return (
            <Dialog modal={true} open={this.state.open}>
              <div className="modal-title">Create Authority</div>
              
              <table style={{width:"100%", padding:"0 60px 30px 60px"}}>
                <tr>
                  <td className="form-key">Authority Name</td>
                  <td className="form-value"><input className="tok-textfield" type="text" name="name" value={this.state.name} onChange={this.nameChanged.bind(this)} autoComplete="off" /></td>
                </tr>
                <tr>
                  <td className="form-key">Description</td>
                  <td><textarea className="tok-textfield" name="description" value={this.state.description} onChange={this.descriptionChanged.bind(this)} autoComplete="off" /></td>
                </tr>                
              </table>
              
              <div style={{textAlign:"center", paddingBottom:"30px"}}>
                <button className="tok-button center" style={{marginRight:"10px"}} onClick={() => this.create()}>Create</button>
                <button className="tok-button tok-cancel center" onClick={() => this.handleClose()}>Cancel</button>
              </div>
            </Dialog>
        );
    }
    
}

export default CreateAuthorityForm;