import React from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import Layout from "../layout/Layout.jsx";
import Loader from "../layout/Loader.jsx";
import ViewAudiences from "./ViewAudiences.jsx";
import ViewAudience from "./ViewAudience.jsx";
import AudienceService from "../../services/AudienceService.js";
import ApplicationError from "../error/ApplicationError.jsx";

class Audiences extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            audiences: [],
            loading: false,
            error: null,
            audience: null
        };
    }

    componentWillMount() {
        this.setState({ loading:true });
        AudienceService.list().then((result) => {
            if(result.status === "accepted") {
                this.setState({ result:result.instance.audiences });
                if(result.instance.audiences) {
                    this.setState({ audience:result.instance.audiences[0] });
                }
                else {
                    this.setState({ audience:{ } });
                }
            }
            else {
                this.setState({ error:result.errors[0] });
            }
            this.setState({ loading:false });
        });
    }

    audienceClicked(target) {
        this.setState({ audience:target });
    }

    audienceCreated(audience) {
        var result = this.state.result;
        var audiences = result.slice();
        audiences.unshift(audience);
        result = audiences;
        this.setState({ result:result });
        this.setState({ audience:audience });
    }

    audienceUpdated(audience) {
        var result = this.state.result;
        var audiences = result.slice();
        var index = audiences.findIndex(function(a) { return a.id === audience.id });
        if(index !== -1) {
            audiences[index] = audience;
        }
        result = audiences;
        this.setState({ result:result });
        this.setState({ audience:audience });
    }

    audienceDeleted(audience) {
        var result = this.state.result;
        var audiences = result.slice();
        var index = audiences.findIndex(function(a) { return a.id === audience.id });
        if(index !== -1) {
            audiences.splice(index, 1);
        }
        result = audiences;
        this.setState({ result:result });
        
        if(audiences.length > 1 && index > 0) {
            this.setState({ audience:audiences[index - 1] });
        }
    }

    render() {
        let output = this.state.error != null ? <ApplicationError error={this.state.error} /> : <div><ViewAudiences audiences={this.state.result} audienceClicked={ this.audienceClicked.bind(this) } audienceCreated={ this.audienceCreated.bind(this) } audienceUpdated={ this.audienceUpdated.bind(this) } audienceDeleted={ this.audienceDeleted.bind(this) } />  <ViewAudience audience={this.state.audience} audienceUpdated={ this.audienceUpdated.bind(this) } audienceDeleted={ this.audienceDeleted.bind(this) } /></div>;
        let render = this.state.loading ? <Loader /> : output;
        
        return (
            <Layout>
                <MuiThemeProvider>
                   <div className="content-holder">
                     {render}
                   </div>
                </MuiThemeProvider>
            </Layout>
        );
    }
    
}

export default Audiences;