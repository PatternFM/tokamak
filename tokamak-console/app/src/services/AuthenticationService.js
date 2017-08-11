import {BASE_URL} from "../config"
import {CLIENT_CREDENTIALS} from "../config"

var AuthenticationService = {
		
    login(username, password, callback) {
        fetch(BASE_URL + "/login", {
        	credentials: "include",
        	method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
            },
            body: JSON.stringify( {username:username, password:password} ),
        })
        .then(function(response) {
        	if(response.status === 200) {
        		response.headers.forEach(function(val, key) { console.log(key + ' -> ' + val); });
        		response.json().then(function(data) {  
        			localStorage.setItem("authenticated", true);
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
    
    logout() {
        localStorage.removeItem("authenticated");
    },
    
    isAuthenticated() {
        return !!localStorage.authenticated;
    }    
    
}

export default AuthenticationService;