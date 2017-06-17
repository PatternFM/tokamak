import RestService from "./RestService.js"

var AuthorityService = {
        
    list() {
        return RestService.list("/v1/authorities");
    }  
    
}

export default AuthorityService;