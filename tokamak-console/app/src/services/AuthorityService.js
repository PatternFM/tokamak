import RestService from "./RestService.js"

var AuthorityService = {
        
    list() {
        return RestService.list("/v1/authorities");
    },  
    
    create(name, description) {
        return RestService.post("/v1/authorities", {name:name, description:description});
    },

    update(id, name, description) {
        return RestService.put("/v1/authorities/" + id, {name:name, description:description});
    },

    delete(authority) {
        return RestService.delete("/v1/authorities/" + authority.id, authority);
    }

}

export default AuthorityService;