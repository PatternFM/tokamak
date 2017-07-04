import RestService from "./RestService.js"

var AudienceService = {
        
    list() {
        return RestService.list("/v1/audiences");
    },  
    
    create(name, description) {
        return RestService.post("/v1/audiences", {name:name, description:description});
    },

    update(id, name, description) {
        return RestService.put("/v1/audiences/" + id, {name:name, description:description});
    },

    delete(audience) {
        return RestService.delete("/v1/audiences/" + audience.id, audience);
    }
    
}

export default AudienceService;