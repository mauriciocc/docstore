app.config(['$translateProvider', function ($translateProvider) {
    $translateProvider.translations('pt', {
        'brand.label': 'DocStore',
        'main.page': 'Página Principal',
        'documents': 'Documentos',
        'customers': 'Clientes',
        'users': 'Usuários',
        'dashboard': 'Dashboard',
        'search.placeholder': 'Pesquisar...',
        'polar.bear': 'Urso Polar',
        'polar.bear.message': 'Tá frio aqui no polo véi...',
        'brown.bear': 'Urso Marrom',
        'brown.bear.message': 'Cara, olha só o salmão que peguei hoje:',
        'you.have.x.messages': "Você tem {{messages}} {{messages == 1 ? 'mensagem' :'mensagens'}}",
        'you.have.x.notifications': "Você tem {{notifications}} {{notifications == 1 ? 'notificação' :'notificações'}}",
        'see.all.messages': 'Ver todas mensagens',
        'my.profile': 'Meu perfil',
        'sign.out': 'Sair',
        'document.due.date.alert': "Faltam 5 dias para o documento 'DAS - 08/2014' vencer"
    });
}]);