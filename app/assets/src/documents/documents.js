angular.module("docstore.documents", [])
    .factory("Documents", function ($http, $upload) {
        return {
            save: function (data) {
                if (!data.id && data.id !== 0) {
                    return $http.post("/api/documents", data);
                } else {
                    return $http.put("/api/documents/" + data.id, data);
                }
            },
            remove: function (id) {
                return $http.delete("/api/documents/" + id);
            },
            findOne: function (id) {
                return $http.get("/api/documents/" + id);
            },
            findAll: function (customerId) {
                return $http.get("/api/customers/" + customerId + "/documents");
            },
            upload: function (files) {
                return $upload.upload({
                    url: "/api/documents/upload",
                    file: files
                });
            }
        };
    })
    .controller("DocumentsListCtrl", function ($scope, $rootScope, Documents, Customers, Categories, toaster, ngDialog, $cookies) {

        $scope.entity = {};
        $scope.entitys = [];
        $scope.customers = [];
        $scope.categories = [];
        $scope.editing = false;
        $scope.dialog = null;
        $scope.selectedCustomer = {};

        var reset = function () {
            $scope.entity = {};
            $scope.files = null;
            $scope.entity.customerId = $scope.customers[0].id;
            $scope.entity.categoryId = $scope.categories[0].id;
        };

        $scope.refresh = function () {
            Customers.findAll().success(function (data) {
                $scope.customers = data;
                if(!$scope.selectedCustomer.id) {
                    $scope.selectedCustomer = data[0];
                } else {
                    for( var i=0, l=data.length; i<l; i++ ) {
                        var item = data[i];
                        if(item.id === $scope.selectedCustomer.id) {
                            $scope.selectedCustomer = item;
                            break;
                        }
                    }
                }
                Categories.findAll().success(function (cats) {
                    $scope.categories = cats;
                    Documents.findAll($scope.selectedCustomer.id).success(function (docs) {
                        reset();
                        $scope.entitys = docs;
                    });
                });
            });
        };

        $scope.save = function (entity, form) {
            if (form.$valid) {
                var isNew = !entity.id || entity.id === 0;
                if (isNew) {
                    if (!$scope.files) {
                        toaster.pop('warning', "É necessário selecionar um arquivo...");
                        return;
                    }
                }
                var save = function () {
                    Documents.save(entity).success(function () {
                        if (isNew) {
                            $rootScope.$broadcast('documents:created', entity);
                        }
                        $scope.refresh();
                        $scope.editing = false;
                        toaster.pop('success', "Documento salvo com sucesso...");
                        form.submitted = false;
                        $scope.dialog.close();
                    }).error(function (err) {
                        toaster.pop('error', "Erro ao tentar salvar o documento...");
                    });
                };

                if ($scope.files !== null) {
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
            $scope.entity.categoryId = $scope.categories[0].id;
            $scope.dialog = ngDialog.open({
                template: '/assets/src/documents/form.tpl.html',
                scope: $scope
            });
        };

        $scope.edit = function (id) {
            Documents.findOne(id).success(function (data) {
                if (data.dueDate) {
                    data.dueDate = new Date(data.dueDate);
                }
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
                if (e.id == id) {
                    el = e;
                }
            });
            return el;
        };

        $scope.onFileSelect = function ($files) {
            if ($files[0].size > 1024 * 1024 * 2) {
                toaster.pop('error', "Tamanho do arquivo não pode ser maior de 2mb");
            } else {
                $scope.files = $files;
            }
        };

        $scope.clearFiles = function () {
            $scope.files = null;
        };

        $scope.customerChanged = function () {
            Documents.findAll($scope.selectedCustomer.id).success(function (data) {
                $scope.entitys = data;
            });
        };

        $scope.fileUrl = function (documentId) {
            return "/api/documents/" + documentId + "/download?key=" + $cookies["XSRF-TOKEN"];
        };

        $scope.refresh();

    })
;