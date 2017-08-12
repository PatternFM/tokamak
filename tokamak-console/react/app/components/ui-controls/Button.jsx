import React from "react";
import {withRouter} from "react-router-dom";
import AuthenticationService from "../../services/AuthenticationService";

class Button extends React.Component {

    click() {
        this.props.onClick();
    }

    
    render() {
        let className = this.props.loading ? this.props.className + " round-button" : this.props.className;
        let content = this.props.loading ? <svg className="spinner button-spinner" width="20px" height="20px" viewBox="0 0 66 66"><circle className="path" fill="none" strokeWidth="6" strokeLinecap="round" cx="33" cy="33" r="30" /></svg> : this.props.name;
        
        return (
          <div style={{display:"inline-block"}}>
              <input type="submit" style={{display:"none"}} />
              <div className={className} onClick={ () => this.click() }>
                {content}
              </div>  
                           
          </div>
        );
    }
    
}

export default Button;