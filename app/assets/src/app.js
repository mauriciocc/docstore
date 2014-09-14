angular.module("docstore", [
    "ngRoute",
    "ngCookies",
    "toaster",
    "i18n",
    /*"ngTable",*/
    "docstore.login",
    "docstore.login.register",
    "docstore.home",
    "docstore.organizations",
    "docstore.accounts",
    "docstore.main"])
    .config(["$httpProvider", function ($httpProvider) {
        $httpProvider.interceptors.push(function($q, $location) {
            return {
                'responseError': function(response) {
                    if(response.status === 401 || response.status === 403) {
                        $location.path('/login');
                    }
                    return $q.reject(response);
                }
            };
        });
    }])
    .config(["$routeProvider", "$locationProvider", function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/login', {
                templateUrl: '/assets/src/login/login.tpl.html',
                controller: 'LoginCtrl',
                public: true,
                loginPage: true
            })
            .when('/register', {
                templateUrl: '/assets/src/login/register/register.tpl.html',
                controller: 'RegisterCtrl',
                public: true,
                loginPage: true
            })
            .when('/home', {
                templateUrl: '/assets/src/home/home.tpl.html',
                controller: 'HomeCtrl',
                public: false
            })
            .when('/organizations', {
                templateUrl: '/assets/src/organizations/organizations-list.tpl.html',
                controller: 'OrganizationListCtrl',
                public: false
            })
            .otherwise({redirectTo:'/login'});
        $locationProvider.html5Mode(true);
    }])
    .run(function ($rootScope, $location, $cookies, $route, Page, Auth) {
        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            Page.setLoginPage(!!next.loginPage);
            if(!next.public) {
                if(!Auth.isAuthenticated()) {
                    $location.path("/login");
                }
            }
        });

    })
;