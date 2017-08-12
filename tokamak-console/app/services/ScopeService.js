import RestService from "./RestService.js"

var ScopeService = {
		
    list() {
        return RestService.list("/v1/scopes");
    },  
    
    create(name, description) {
        return RestService.post("/v1/scopes", { name:name, description:description });
    },

    update(id, name, description) {
        return RestService.put("/v1/scopes/" + id, { name:name, description:description });
    },

    delete(scope) {
        return RestService.delete("/v1/scopes/" + scope.id, scope);
    }     
    
}

export default ScopeService;