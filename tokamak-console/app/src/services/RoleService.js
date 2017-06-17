import RestService from "./RestService.js"

var RoleService = {
        
    list() {
        return RestService.list("/v1/roles");
    }  
    
}

export default RoleService;