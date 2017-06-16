import {BASE_URL} from "../config"
import {CLIENT_CREDENTIALS} from "../config"

var AuthenticationService = {
		
    login(username, password, callback) {
        fetch(BASE_URL + "/oauth/token", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Accept": "application/json",
                "Authorization": "Basic " + CLIENT_CREDENTIALS
            },
            body: "grant_type=password&username=" + username + "&password=" + password
        })
        .then(function(response) {
        	if(response.status === 200) {
        		response.json().then(function(data) {  
        	        localStorage.access_token = data.access_token;
        	        localStorage.refresh_token = data.refresh_token;
           		    callback({status:"accepted", error: ""});
        		    return;
        	    }); 
        	}
        	if(response.status === 400) {
        		 callback({status:"rejected", error:"invalid_credentials", message: "The username or password you have entered is invalid, try again."});
        		 return;
        	}
        	if(response.status === 500) {
        		 callback({status: "rejected", error:"internal_error", message: "Tokamak is currently unavailable, please try again later."});
        		 return;
        	}
        	if(response.status === 503) {
        		 callback({status:"rejected", error:"service_unavailable", message: "Tokamak is currently unavailable, please try again later."});
        		 return;
        	}        	
        });
    },		
		
    getAccessToken() {
    	return localStorage.getItem("access_token");
    },
    
    logout() {
        localStorage.removeItem("access_token");
        localStorage.removeItem("refresh_token");
    },
    
    isAuthenticated() {
        return !!localStorage.access_token;
    }    
    
}

export default AuthenticationService;