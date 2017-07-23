import RestService from "./RestService.js"

var ClientService = {
        
    list(page) {
        return page? RestService.list("/v1/clients?page=" + page) : RestService.list("/v1/clients");
    },
    
    create(client) {
        return RestService.post("/v1/clients", client);
    },

    update(client) {
        return RestService.put("/v1/clients/" + client.id, client);
    },

    updateSecret(client, secret) {
        return RestService.put("/v1/clients/" + client.id + "/secrets", {newSecret:secret});
    },

    delete(client) {
        return RestService.delete("/v1/clients/" + client.id, client);
    }

}

export default ClientService;