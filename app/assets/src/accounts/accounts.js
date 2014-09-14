angular.module("docstore.accounts", [])
    .factory("Accounts", ["$http", function ($http) {
        return {
           /* save: function (data) {
                return $http.post("/api/organizations", data);
            },
            remove: function (id) {
                return $http.delete("/api/organizations/" + id);
            },
            findOne: function (id) {
                return $http.get("/api/organizations/" + id);
            },*/
            findAll: function () {
                return $http.get("/api/accounts");
            }
        };
    }])
;