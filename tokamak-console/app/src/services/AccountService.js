import {BASE_URL} from "../config"
import AuthenticationService from "./AuthenticationService.js"

var AccountService = {
		
    list() {
        return fetch(BASE_URL + "/v1/accounts", {
            headers: { "Accept": "application/json", "Authorization": "Bearer " + AuthenticationService.getAccessToken() }
        })
        .then(function(response) {
            return response.json();
        })
        .then(function(json) {
            return json.errors ? { status:"rejected", errors:json.errors } : { status:"accepted", instance:json };
        })
        .catch(function(error) {
            return { status:"rejected", errors:[{"code":"INT-0001", "message":"Network unavilable"}] };
        });
    }  
    
}

export default AccountService;