angular.module("docstore.offices", [])
    .factory("Offices", ["$http", function ($http) {
        return {
            save: function (data) {
                return $http.post("/api/offices", data);
            },
            remove: function (id) {
                return $http.delete("/api/offices/" + id);
            },
            findOne: function (id) {
                return $http.get("/api/offices/" + id);
            },
            findAll: function () {
                return $http.get("/api/offices");
            }
        };
    }])
    .controller("OfficesListCtrl", function ($scope, Offices, Organizations, toaster, ngDialog) {

        $scope.office = {};
        $scope.offices = [];
        $scope.organizations = [];
        $scope.editing = false;
        $scope.dialog = null;

        var resetOffice = function () {
            $scope.office = {};
            $scope.office.organizationId = $scope.organizations[0].id;
        };

        $scope.refresh = function () {
            Organizations.findAll().success(function (data) {
                $scope.organizations = data;
                resetOffice();
            });
            Offices.findAll().success(function (data) {
                $scope.offices = data;
            });
        };

        $scope.save = function (office, form) {
            if (form.$valid) {
                Offices.save(office).success(function () {
                    $scope.refresh();
                    $scope.editing = false;
                    toaster.pop('success', "Escritório salvo com sucesso...");
                    form.submitted = false;
                    $scope.dialog.close();
                }).error(function (err) {
                    toaster.pop('error', "Erro ao tentar salvar o escritório...");
                });
            } else {
                form.submitted = true;
            }
        };

        $scope.remove = function (id) {
            Offices.remove(id).success(function () {
                $scope.refresh();
                $scope.editing = false;
                resetOffice();
                toaster.pop('success', 'O escritório foi excluido com sucesso!');
            });
        };

        $scope.createNew = function () {
            $scope.office = {};
            $scope.dialog = ngDialog.open({
                template: '/assets/src/offices/form.tpl.html',
                scope: $scope
            });
        };

        $scope.edit = function (id) {
            Offices.findOne(id).success(function (data) {
                $scope.office = data;
                $scope.editing = true;
            });
            $scope.dialog = ngDialog.open({
                template: '/assets/src/offices/form.tpl.html',
                scope: $scope
            });
        };

        $scope.cancelEdit = function () {
            resetOffice();
            $scope.editing = false;
        };

        $scope.findOrganizationWithId = function (id) {
            var el;
            $scope.organizations.forEach(function (e) {
                if(e.id == id) {
                    el = e;
                }
            });
            return el;
        };

        $scope.refresh();

    })
;