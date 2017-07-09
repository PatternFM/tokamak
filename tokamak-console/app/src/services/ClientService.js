import RestService from "./RestService.js"

var ClientService = {
        
    list(page) {
        return page? RestService.list("/v1/clients?page=" + page) : RestService.list("/v1/clients");
    }
    
}

export default ClientService;