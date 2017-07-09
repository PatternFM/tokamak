import RestService from "./RestService.js"

var GrantTypeService = {
        
    list() {
        return RestService.list("/v1/grant_types");
    }
    
}

export default GrantTypeService;