angular.module("docstore.main", [])
    .factory("Page", function ($location) {

        var pageTitle = "docstore.title";
        var loginPage = false;

        return {
            pageTitle: function(){return pageTitle;},
            setPageTitle: function(pageTitle){this.pageTitle = pageTitle;},
            loginPage: function(){return loginPage;},
            setLoginPage: function(loginPage){this.loginPage = loginPage;}
        };
    })
    .controller("MainCtrl", function ($scope, Auth, Page, $location) {
        $scope.pageTitle = function () {
            return Page.pageTitle();
        };
        $scope.loginPage = function () {
            return Page.loginPage;
        };
        $scope.displayHeader = function () {
            return Auth.isAuthenticated();
        };
        $scope.displayMenu = function () {
            return Auth.isAuthenticated();
        };
        $scope.isCurrentPath = function (path) {
            return $location.path() === path;
        };
        $scope.currentUserName = function () {
            var user = Auth.user();
            var name = user.displayName;
            return name ? name : Auth.user.name;
        };
    })
;
