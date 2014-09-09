angular.module('docstore.login.register', [])
    .controller("RegisterCtrl", ["$scope", "$http", function RegisterCtrl($scope, $http) {

        $scope.formData = {};

        $scope.register = function() {
            console.log($scope.formData);
            $http.post(jsRoutes.controllers.Users.save().url, $scope.formData)
                .success(function (data) {
                    console.log(data);
                });
        };
    }])
;
