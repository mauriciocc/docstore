angular.module("docstore.categories", [])
    .factory("Categories", ["$http", function ($http) {
        return {
            save: function (data) {
                if (!data.id || data.id === 0) {
                    return $http.post("/api/categories", data);
                } else {
                    return $http.put("/api/categories/"+ data.id, data);
                }
            },
            remove: function (id) {
                return $http.delete("/api/categories/" + id);
            },
            findOne: function (id) {
                return $http.get("/api/categories/" + id);
            },
            findAll: function () {
                return $http.get("/api/categories");
            }
        };
    }])
    .controller("CategoriesListCtrl", function ($scope, Categories, toaster, ngDialog) {

        $scope.entity = {};
        $scope.entities = [];
        $scope.editing = false;
        $scope.dialog = null;

        var reset = function () {
            $scope.entity = {};
        };

        $scope.refresh = function () {
            Categories.findAll().success(function (data) {
                $scope.entities = data;

            });
            reset();
        };

        $scope.save = function (entity, form) {
            if (form.$valid) {
                Categories.save(entity).success(function () {
                    $scope.refresh();
                    $scope.editing = false;
                    toaster.pop('success', "Categoria salva com sucesso...");
                    form.submitted = false;
                    $scope.dialog.close();
                }).error(function (err) {
                    toaster.pop('error', "Erro ao tentar salvar a categoria...");
                });
            } else {
                form.submitted = true;
            }
        };

        $scope.remove = function (id) {
            Categories.remove(id).success(function () {
                $scope.refresh();
                $scope.editing = false;
                reset();
                toaster.pop('success', 'A categoria foi excluida com sucesso!');
            });
        };

        $scope.createNew = function () {
            $scope.entity = {};
            $scope.dialog = ngDialog.open({
                template: '/assets/src/categories/form.tpl.html',
                scope: $scope
            });
        };

        $scope.edit = function (id) {
            Categories.findOne(id).success(function (data) {
                $scope.entity = data;
                $scope.editing = true;
            });
            $scope.dialog = ngDialog.open({
                template: '/assets/src/categories/form.tpl.html',
                scope: $scope
            });
        };

        $scope.cancelEdit = function () {
            reset();
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