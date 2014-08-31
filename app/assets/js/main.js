var app = angular.module("docstore", ['pascalprecht.translate']);

app.config(['$translateProvider', function ($translateProvider) {
    $translateProvider.preferredLanguage('pt');
}]);
