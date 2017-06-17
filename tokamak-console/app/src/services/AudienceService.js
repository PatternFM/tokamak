import RestService from "./RestService.js"

var AudienceService = {
        
    list() {
        return RestService.list("/v1/audiences");
    }  
    
}

export default AudienceService;