angular.module('docstore.login.register', [])
    .directive('pwCheck', [function () {
        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, ctrl) {
                var firstPassword = "input[ng-model='" + attrs.pwCheck + "']";
                elem.add(firstPassword).on('keyup', function () {
                    scope.$apply(function () {
                        var v = elem.val()===$(firstPassword).val();
                        ctrl.$setValidity('pwmatch', v);
                    });
                });
            }
        };
    }])
    .controller("RegisterCtrl", function ($scope, $http, $location, $translate, toaster) {
        $scope.formData = {};

        $scope.register = function (userData) {
            if ($scope.registerForm.$valid) {
                $http.post(jsRoutes.controllers.Users.save().url, userData)
                    .success(function (data) {
                        $translate("account.creation.success").then(function (msg) {
                            toaster.pop("success", msg);
                            $location.path("/login");
                        });
                    })
                    .error(function (resp) {
                        if (resp.error) {
                            toaster.pop("error", resp.error);
                        } else {
                            $translate("account.creation.error").then(function (msg) {
                                toaster.pop("error", msg);
                            });
                        }
                    });
            } else {
                $scope.registerForm.submitted = true;
            }
        };
    })
;
