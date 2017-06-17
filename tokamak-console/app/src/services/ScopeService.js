import RestService from "./RestService.js"

var ScopeService = {
		
    list() {
        return RestService.list("/v1/scopes");
    }  
    
}

export default ScopeService;