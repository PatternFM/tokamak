import RestService from "./RestService.js"

var ClientService = {
        
    list() {
        return RestService.list("/v1/clients");
    }  
    
}

export default ClientService;