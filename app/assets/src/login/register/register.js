angular.module('docstore.login.register', ["$http"])
    .controller("RegisterCtrl", function RegisterCtrl($scope, $http) {

        $scope.formData = {};

        $scope.register = function() {
            $http.post(jsRoutes.controllers.Users.save, $scope.formData)
                .success(function (data) {
                    console.log(data);
                });
        }
    })
;
