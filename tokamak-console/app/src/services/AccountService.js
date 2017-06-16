import {BASE_URL} from "../config"
import AuthenticationService from "./AuthenticationService.js"

var AccountService = {
		
    list() {
        return fetch(BASE_URL + "/v1/accounts", {
            headers: {
                "Accept": "application/json",
                "Authorization": "Bearer " + AuthenticationService.getAccessToken()
            }
        })
        .then(function(response) {
            if(response.ok) {
        	   return response.json();
        	}
        	throw {status:"rejected", code:response.status, errors:response.json().errors};
        })
        .then(function(json) {
        	return {status:"accepted", "instance":json};
        })
        .catch(function(error) {
            return error.status ? error : {status:"rejected", code:0, errors:[{"code":"INT-0001", "message":"Network unavilable"}]}
        });
    }  
    
}

export default AccountService;