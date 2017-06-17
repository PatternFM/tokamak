import React from "react";

class ApplicationError extends React.Component {
  render() {
    return (
        <div className="content-holder animated fadeIn">
          {this.props.error.code === 0 &&
            <div className="error-page">
            <h2 className="error-title">You appear to be offline</h2>
            <p className="error-message">Please check your network connection and try again.</p>
            <img src="/img/offline.png" alt="" />
            </div>
          }
          {this.props.error.code !== 0 &&
            <div className="error-page">
            <h2 className="error-title">Something went wrong</h2>
            <p className="error-message">Your request could not be completed, please try again later.</p>
            <img src="/img/system-error.png" alt="" />
            </div>
          }          
        </div>
    );
  }
}

export default ApplicationError;