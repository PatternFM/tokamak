import RestService from "./RestService.js"

var PasswordPolicy = {
        
    list() {
        return RestService.list("/v1/policies");
    },        
        
    update(passwordPolicy) {
        return RestService.put("/v1/policies/" + passwordPolicy.id, passwordPolicy);
    },

    findByName(name) {
        return RestService.get("/v1/policies/name/" + name);
    },
    
}

export default PasswordPolicy;