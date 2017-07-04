import RestService from "./RestService.js"

var RoleService = {
        
    list() {
        return RestService.list("/v1/roles");
    },
    
    create(name, description) {
        return RestService.post("/v1/roles", { name:name, description:description });
    },

    update(id, name, description) {
        return RestService.put("/v1/roles/" + id, { name:name, description:description });
    },

    delete(role) {
        return RestService.delete("/v1/roles/" + role.id, role);
    }      
    
}

export default RoleService;