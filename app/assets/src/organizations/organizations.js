angular.module("docstore.organizations", [])
    .factory("Organizations", ["$http", function ($http) {
        return {
            save: function (data) {
                return $http.post("/api/organizations", data);
            },
            remove: function (id) {
                return $http.delete("/api/organizations/" + id);
            },
            findOne: function (id) {
                return $http.get("/api/organizations/" + id);
            },
            findAll: function () {
                return $http.get("/api/organizations");
            }
        };
    }])
    .controller("OrganizationListCtrl", function ($scope, Organizations, Accounts, toaster) {

        $scope.organization = {};
        $scope.organizations = [];
        $scope.editing = false;

        $scope.refresh = function () {
            Accounts.findAll().success(function (data) {
                $scope.accounts = data;
            });
            Organizations.findAll().success(function (data) {
                $scope.organizations = data;
            });
        };

        $scope.save = function (organization) {
            if ($scope.organizationForm.$valid) {
                Organizations.save(organization).success(function (data) {
                    $scope.organization = {};
                    $scope.refresh();
                    $scope.editing = false;
                    toaster.pop('success', "Organização salva com sucesso...");
                }).error(function (err) {
                    toaster.pop('error', "Erro ao tentar salvar organização...");
                });
            } else {
                $scope.organizationForm.submitted = true;
            }
        };

        $scope.remove = function (id) {
            Organizations.remove(id).success(function () {
                $scope.refresh();
            });
        };

        $scope.edit = function (organization) {
            $scope.organization = organization;
            $scope.editing = true;
        };

        $scope.cancelEdit = function () {
            $scope.organization = {};
            $scope.editing = false;
        };

        $scope.getAccount = function (id) {
          for(var account in $scope.accounts) {
              if(account.id === id) {
                  return account;
              }
          }
        };

        $scope.refresh();

    })
;