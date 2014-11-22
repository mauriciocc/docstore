angular.module("docstore", [
    "ngRoute",
    "ngCookies",
    "toaster",
    "i18n",
    "ngDialog",
    "ngSanitize",
    "angularFileUpload",
    'datatables',
    "docstore.login",
    "docstore.login.register",
    "docstore.home",
    "docstore.organizations",
    "docstore.accounts",
    "docstore.offices",
    "docstore.customers",
    "docstore.documents",
    "docstore.notifications",
    "docstore.main"])
    .config(["$httpProvider", function ($httpProvider) {
        $httpProvider.interceptors.push(function ($q, $location) {
            return {
                'responseError': function (response) {
                    if (response.status === 401 || response.status === 403) {
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
            .otherwise({redirectTo: '/login'});
        $locationProvider.html5Mode(true);
    }])
    .directive('moment', function ($compile) {
        return {
            restrict: 'E',
            link: function(scope, element, attrs) {
                element[0].outerHTML = moment(Number(attrs.millis)).fromNow();
                $compile(element.contents())(scope);
            }
        };
    })
    .directive('confirmationNeeded', function () {
        return {
            priority: 1,
            terminal: true,
            link: function (scope, element, attr) {
                var msg = attr.confirmationNeeded || "Are you sure?";
                var clickAction = attr.ngClick;
                element.bind('click', function () {
                    swal({   title: "Tem certeza que deseja excluir este item ?",
                        text: "Após exclusão não será possivel recuperar este registro",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55", confirmButtonText: "Excluir",
                        closeOnConfirm: true
                    }, function (isConfirm) {
                        if (isConfirm) {
                            scope.$eval(clickAction);
                        }
                    });
                });
            }
        };
    })
    .run(function ($rootScope, $location, $cookies, $route, Page, Auth) {
        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            Page.setLoginPage(!!next.loginPage);
            if (!next.public) {
                if (!Auth.isAuthenticated()) {
                    $location.path("/login");
                }
            }
        });

    }).run(function(DTDefaultOptions) {
        var lang = {
            "sEmptyTable": "Nenhum registro encontrado",
            "sInfo": "Mostrando de _START_ até _END_ de _TOTAL_ registros",
            "sInfoEmpty": "Mostrando 0 até 0 de 0 registros",
            "sInfoFiltered": "(Filtrados de _MAX_ registros)",
            "sInfoPostFix": "",
            "sInfoThousands": ".",
            "sLengthMenu": "_MENU_ resultados por página",
            "sLoadingRecords": "Carregando...",
            "sProcessing": "Processando...",
            "sZeroRecords": "Nenhum registro encontrado",
            "sSearch": "Pesquisar",
            "oPaginate": {
                "sNext": "Próximo",
                "sPrevious": "Anterior",
                "sFirst": "Primeiro",
                "sLast": "Último"
            },
            "oAria": {
                "sSortAscending": ": Ordenar colunas de forma ascendente",
                "sSortDescending": ": Ordenar colunas de forma descendente"
            }
        };

        DTDefaultOptions.setLanguage(lang);

    })
;