angular.module("docstore.documents", [])
    .factory("Documents", function ($http, $upload) {
        return {
            save: function (data) {
                return $http.post("/api/documents", data);
            },
            remove: function (id) {
                return $http.delete("/api/documents/" + id);
            },
            findOne: function (id) {
                return $http.get("/api/documents/" + id);
            },
            findAll: function (customerId) {
                return $http.get("/api/customers/"+customerId+"/documents");
            },
            upload: function (files) {
                return $upload.upload({
                    url: "/api/documents/upload",
                    file: files
                });
            }
        };
    })
    .controller("DocumentsListCtrl", function ($scope, Documents, Customers, toaster, ngDialog) {

        $scope.entity = {};
        $scope.entitys = [];
        $scope.customers = [];
        $scope.selectedCustomer = {};
        $scope.editing = false;
        $scope.dialog = null;

        var reset = function () {
            $scope.entity = {};
            $scope.files = null;
            /*$scope.entity.customerId = $scope.customers[0].id;*/
        };

        $scope.refresh = function () {
            Customers.findAll().success(function (data) {
                $scope.customers = data;
                if(!$scope.selectedCustomer.id) {
                    $scope.selectedCustomer = $scope.customers[0];
                }
                Documents.findAll($scope.selectedCustomer.id).success(function (data) {
                    $scope.entitys = data;
                });
            });
            reset();

        };

        $scope.save = function (entity, form) {
            if (form.$valid) {
                var save = function () {
                    Documents.save(entity).success(function () {
                        $scope.refresh();
                        $scope.editing = false;
                        toaster.pop('success', "Documento salvo com sucesso...");
                        form.submitted = false;
                        $scope.dialog.close();
                    }).error(function (err) {
                        toaster.pop('error', "Erro ao tentar salvar o documento...");
                    });
                };

                if($scope.files !== null) {
                    Documents.upload($scope.files).success(function () {
                        save();
                    });
                } else {
                    save();
                }
            } else {
                form.submitted = true;
            }
        };

        $scope.remove = function (id) {
            Documents.remove(id).success(function () {
                $scope.refresh();
                $scope.editing = false;
                reset();
                toaster.pop('success', 'O documento foi excluido com sucesso!');
            });
        };

        $scope.createNew = function () {
            $scope.entity = {};
            $scope.entity.customerId = $scope.selectedCustomer.id;
            $scope.dialog = ngDialog.open({
                template: '/assets/src/documents/form.tpl.html',
                scope: $scope
            });
        };

        $scope.edit = function (id) {
            Documents.findOne(id).success(function (data) {
                $scope.entity = data;
                $scope.editing = true;
            });
            $scope.dialog = ngDialog.open({
                template: '/assets/src/documents/form.tpl.html',
                scope: $scope
            });
        };

        $scope.cancelEdit = function () {
            reset();
            $scope.editing = false;
        };

        $scope.findCustomerWithId = function (id) {
            var el;
            $scope.customers.forEach(function (e) {
                if(e.id == id) {
                    el = e;
                }
            });
            return el;
        };

        $scope.onFileSelect = function($files) {
            if($files[0].size > 1024*1024*2) {
                toaster.pop('error', "Tamanho do arquivo não pode ser maior de 2mb");
            } else {
                $scope.files = $files;
            }
        };

        $scope.clearFiles = function() {
            $scope.files = null;
        };

        $scope.customerChanged = function() {
            Documents.findAll($scope.selectedCustomer.id).success(function (data) {
                $scope.entitys = data;
            });
        };

        $scope.refresh();

    })
;