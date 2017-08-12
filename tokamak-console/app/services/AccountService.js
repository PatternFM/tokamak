import RestService from "./RestService.js"

var AccountService = {
        
    list(page) {
        return page? RestService.list("/v1/accounts?page=" + page) : RestService.list("/v1/accounts");
    }, 
    
    create(account) {
        return RestService.post("/v1/accounts", account);
    },

    update(account) {
        return RestService.put("/v1/accounts/" + account.id, account);
    },

    updatePassword(account, password) {
        return RestService.put("/v1/accounts/" + account.id + "/password", { newSecret:password });
    },

    delete(account) {
        return RestService.delete("/v1/accounts/" + account.id, account);
    }
}

export default AccountService;