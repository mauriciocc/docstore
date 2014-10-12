angular.module("docstore", [
    "ngRoute",
    "ngCookies",
    "toaster",
    "i18n",
    "ngDialog",
    "angularFileUpload",
    /*"ngTable",*/
    "docstore.login",
    "docstore.login.register",
    "docstore.home",
    "docstore.organizations",
    "docstore.accounts",
    "docstore.offices",
    "docstore.customers",
    "docstore.documents",
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
            .when('/offices', {
                templateUrl: '/assets/src/offices/list.tpl.html',
                controller: 'OfficesListCtrl',
                public: false
            })
            .when('/customers', {
                templateUrl: '/assets/src/customers/list.tpl.html',
                controller: 'CustomersListCtrl',
                public: false
            })
            .when('/documents', {
                templateUrl: '/assets/src/documents/list.tpl.html',
                controller: 'DocumentsListCtrl',
                public: false
            })
            .otherwise({redirectTo:'/login'});
        $locationProvider.html5Mode(true);
    }])
    .directive('confirmationNeeded', function () {
        return {
            priority: 1,
            terminal: true,
            link: function (scope, element, attr) {
                var msg = attr.confirmationNeeded || "Are you sure?";
                var clickAction = attr.ngClick;
                element.bind('click',function () {
                    if ( window.confirm(msg) ) {
                        scope.$eval(clickAction);
                    }
                });
            }
        };
    })
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