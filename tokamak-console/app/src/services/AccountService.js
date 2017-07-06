import RestService from "./RestService.js"

var AccountService = {
        
    list() {
        return RestService.list("/v1/accounts");
    }, 
    
    create(account) {
        return RestService.post("/v1/accounts", account);
    },

    update(account) {
        return RestService.put("/v1/accounts/" + account.id, account);
    },

    delete(account) {
        return RestService.delete("/v1/accounts/" + account.id, account);
    }
}

export default AccountService;