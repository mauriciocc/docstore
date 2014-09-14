angular.module('docstore.login', [])
    .factory("Auth", ["$cookies", "$http", "$q", "$timeout", "$location", function ($cookies, $http, $q, $timeout, $location) {
        var user;
        var organization;
        var token = $cookies["XSRF-TOKEN"];

        if (token) {
            $http.get("/ping").then(
                function (response) {
                    // Token valid, fetch user data
                    return $http.get("/api/users/" + response.data.userId);
                },
                function (response) {
                    token = undefined;
                    $cookies["XSRF-TOKEN"] = undefined;
                    user = undefined;
                    organization = undefined;
                    $location.path('/login');
                    return $q.reject("Token invalid");
                }
            ).then(
                function (response) {
                    user = response.data;
                    $location.path('/home');
                }, function (response) {
                    $location.path('/login');
                }
            );
        }


        return {
            user: function () {
                return user;
            },
            setUser: function (user) {
                this.user = user;
            },
            setOrganization: function (organization) {
                this.organization = organization;
            },
            isAuthenticated: function () {
                return !!user;
            },
            isOrganizationSelected: function () {
                return !!organization;
            },
            doLogin: function (credentials, callback) {
                $http.post("/login", credentials).then(
                    function (response) { // success
                        token = response.data.token;
                        var userId = response.data.userId;
                        return $http.get("/api/users/" + userId); // return another promise to chain `then`
                    }, function (response) { // error
                        return $q.reject("Login failed");
                    }
                ).then(
                    function (response) {
                        callback(true);
                        user = response.data;
                        $location.path('/home');

                    },
                    function (response) {
                        console.log(response);
                        callback(false);
                    }
                );
            },
            doLogout: function () {
                $http.post("/logout").then(function () {
                    user = undefined;
                    organization = undefined;
                    $cookies["XSRF-TOKEN"] = undefined;
                    $location.path("/login");
                });
            },
            ping: function () {
                $http.get("/ping").success(function () {
                    return true;
                }).error(function () {
                    return false;
                });
            }
        };
    }])
    .controller("LoginCtrl", ['$scope', 'toaster', '$translate', 'Auth', function($scope, toaster, $translate, Auth) {

        $scope.credentials = {};

        /**
         * Login using the given credentials as (email,password).
         * The server adds the XSRF-TOKEN cookie which is then picked up by Play.
         */
        $scope.login = function (credentials) {
            Auth.doLogin(credentials, function(success) {
                if(!success) {
                    $translate('invalid.login.credentials').then(function (text) {
                        toaster.pop("warning", text);
                    });
                }
            });

        };

        /**
         * Invalidate the token on the server.
         */
        $scope.logout = function () {
            Auth.doLogout();
        };
    }])

;
