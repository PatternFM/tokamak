import RestService from "./RestService.js"

var AccountService = {
        
    list() {
        return RestService.list("/v1/accounts");
    }  
    
}

export default AccountService;