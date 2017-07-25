import RestService from "./RestService.js"

var PasswordPolicy = {
        
    update(passwordPolicy) {
        return RestService.put("/v1/policies/" + passwordPolicy.id, passwordPolicy);
    },

    findByName(name) {
        return RestService.get("/v1/policies/name/" + name);
    },
    
}

export default PasswordPolicy;