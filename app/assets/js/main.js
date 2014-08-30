var app = angular.module("app", ['pascalprecht.translate']);

app.config(['$translateProvider', function ($translateProvider) {
  $translateProvider.translations('pt', {
    'brand.label': 'DocStore',
    'main.page': 'Página Principal',
    'documents': 'Documentos'
  });
 
  $translateProvider.preferredLanguage('pt');
}]);
