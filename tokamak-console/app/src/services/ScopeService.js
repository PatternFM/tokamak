import {BASE_URL} from "../config"
import AuthenticationService from "./AuthenticationService.js"

var ScopeService = {
		
    list() {
        return fetch(BASE_URL + "/v1/scopes", {
            headers: {
                "Accept": "application/json",
                "Authorization": "Bearer " + AuthenticationService.getAccessToken()
            }
        })
        .then(function(response) {
        	return response.json();
        })
        .then(function(json) {
        	return json;
        })
        .catch(function(error) {
        	console.log("ERROR!");
        });
    }  
    
}

export default ScopeService;