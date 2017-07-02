import {BASE_URL} from "../config"
import AuthenticationService from "./AuthenticationService.js"

var RestService = {
		
    list(endpoint) {
        return fetch(BASE_URL + endpoint, {
            headers: { "Accept": "application/json", "Authorization": "Bearer " + AuthenticationService.getAccessToken() }
        })
        .then(function(response) {
            if(response.ok) {
                return response.json();
            }
            throw Error("Received " + response.status + " reponse code from the Tokamak Server.");
        })
        .then(function(json) {
        	return json.errors ? { status:"rejected", errors:json.errors } : { status:"accepted", instance:json };
        })
        .catch(function(error) {
            return { status:"rejected", errors:[{ code:"INT-0001", message:error }] };
        });
    },  
    
    post(endpoint, payload) {
        return fetch(BASE_URL + endpoint, {
            method: "POST",
            body: JSON.stringify(payload),
        	headers: { "Accept": "application/json", "Content-Type": "application/json", "Authorization": "Bearer " + AuthenticationService.getAccessToken() }
        })
        .then(function(response) {
            if(response.ok) {
                return response.json();
            }
            throw Error("Received " + response.status + " reponse code from the Tokamak Server.");
        })
        .then(function(json) {
        	return json.errors ? { status:"rejected", errors:json.errors } : { status:"accepted", instance:json };
        })
        .catch(function(error) {
            return { status:"rejected", errors:[{ code:"INT-0001", message:error }] };
        });    	
    },
    
    put(endpoint, payload) {
        return fetch(BASE_URL + endpoint, {
            method: "PUT",
            body: JSON.stringify(payload),
        	headers: { "Accept": "application/json", "Content-Type": "application/json", "Authorization": "Bearer " + AuthenticationService.getAccessToken() }
        })
        .then(function(response) {
            if(response.ok) {
                return response.json();
            }
            throw Error("Received " + response.status + " reponse code from the Tokamak Server.");
        })
        .then(function(json) {
        	return json.errors ? { status:"rejected", errors:json.errors } : { status:"accepted", instance:json };
        })
        .catch(function(error) {
            return { status:"rejected", errors:[{ code:"INT-0001", message:error }] };
        });    	
    }    
    
}

export default RestService;