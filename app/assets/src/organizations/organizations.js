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
    .controller("OrganizationListCtrl", function ($scope, Organizations, Accounts, toaster, ngDialog) {

        $scope.organization = {};
        $scope.organizations = [];
        $scope.accounts = [];
        $scope.editing = false;
        $scope.dialog = null;

        var resetOrganization = function () {
            $scope.organization = {};
            $scope.organization.accountId = $scope.accounts[0].id;
        };

        $scope.refresh = function () {
            Accounts.findAll().success(function (data) {
                $scope.accounts = data;
                resetOrganization();
            });
            Organizations.findAll().success(function (data) {
                $scope.organizations = data;
            });
        };

        $scope.save = function (organization, form) {
            if (form.$valid) {
                Organizations.save(organization).success(function () {
                    $scope.refresh();
                    $scope.editing = false;
                    toaster.pop('success', "Organização salva com sucesso...");
                    form.submitted = false;
                    $scope.dialog.close();
                }).error(function (err) {
                    toaster.pop('error', "Erro ao tentar salvar organização...");
                });
            } else {
                form.submitted = true;
            }
        };

        $scope.remove = function (id) {
            Organizations.remove(id).success(function () {
                $scope.refresh();
                $scope.editing = false;
                resetOrganization();
                toaster.pop('success', 'A organização foi excluida com sucesso!');
            });
        };

        $scope.newOrganization = function () {
            $scope.organization = {};
            $scope.dialog = ngDialog.open({
                template: '/assets/src/organizations/organizations-form.tpl.html',
                scope: $scope
            });
        };

        $scope.edit = function (id) {
            Organizations.findOne(id).success(function (data) {
                $scope.organization = data;
                $scope.editing = true;
            });
            $scope.dialog = ngDialog.open({
                template: '/assets/src/organizations/organizations-form.tpl.html',
                scope: $scope
            });
        };

        $scope.cancelEdit = function () {
            $scope.organization = {};
            $scope.editing = false;
        };

        $scope.findAccountWithId = function (id) {
            var el;
            $scope.accounts.forEach(function (e) {
                if(e.id == id) {
                    el = e;
                }
            });
            return el;
        };

        $scope.refresh();

    })
;