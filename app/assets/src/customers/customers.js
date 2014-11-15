angular.module("docstore.customers", [])
  .factory("Customers", ["$http", function ($http) {
    return {
      save: function (data) {
        if (!data.id || data.id === 0) {
          return $http.post("/api/customers", data);
        } else {
          return $http.put("/api/customers/" + data.id, data);
        }
      },
      remove: function (id) {
        return $http.delete("/api/customers/" + id);
      },
      findOne: function (id) {
        return $http.get("/api/customers/" + id);
      },
      findAll: function () {
        return $http.get("/api/customers");
      }
    };
  }])
  .controller("CustomersListCtrl", function ($scope, Customers, Offices, toaster, ngDialog) {

    $scope.entity = {};
    $scope.entitys = [];
    $scope.offices = [];
    $scope.editing = false;
    $scope.dialog = null;

    var reset = function () {
      $scope.entity = {};
      $scope.entity.organizationId = $scope.offices[0].id;
    };

    $scope.refresh = function () {
      Offices.findAll().success(function (data) {
        $scope.offices = data;
        reset();
      });
      Customers.findAll().success(function (data) {
        $scope.entitys = data;
      });
    };

    $scope.save = function (entity, form) {
      if (form.$valid) {
        Customers.save(entity).success(function () {
          $scope.refresh();
          $scope.editing = false;
          toaster.pop('success', "Cliente salvo com sucesso...");
          form.submitted = false;
          $scope.dialog.close();
        }).error(function (err) {
          toaster.pop('error', "Erro ao tentar salvar o cliente...");
        });
      } else {
        form.submitted = true;
      }
    };

    $scope.remove = function (id) {
      Customers.remove(id).success(function () {
        $scope.refresh();
        $scope.editing = false;
        reset();
        toaster.pop('success', 'O cliente foi excluido com sucesso!');
      });
    };

    $scope.createNew = function () {
      $scope.entity = {};
      $scope.dialog = ngDialog.open({
        template: '/assets/src/customers/form.tpl.html',
        scope: $scope
      });
    };

    $scope.edit = function (id) {
      Customers.findOne(id).success(function (data) {
        $scope.entity = data;
        $scope.editing = true;
      });
      $scope.dialog = ngDialog.open({
        template: '/assets/src/customers/form.tpl.html',
        scope: $scope
      });
    };

    $scope.cancelEdit = function () {
      reset();
      $scope.editing = false;
    };

    $scope.findOfficeWithId = function (id) {
      var el;
      $scope.offices.forEach(function (e) {
        if (e.id == id) {
          el = e;
        }
      });
      return el;
    };

    $scope.refresh();

  })
;