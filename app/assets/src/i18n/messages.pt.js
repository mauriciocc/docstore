angular.module('i18n', ['pascalprecht.translate'])
    .config(['$translateProvider', function ($translateProvider) {
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
            'document.due.date.alert': "Faltam 5 dias para o documento 'DAS - 08/2014' vencer",
            'organizations': "Organizações",
            'docstore.title': "DocStore",
            'sign.me.in': "Acessar",
            'create.an.account': "Criar uma conta",
            'email': "Email",
            'password': "Senha",
            'invalid.login.credentials': "Usuário ou senha inválidos, tente novamente...",
            'create.your.account': "Crie sua conta",
            'user.name': "Nome do Usuário",
            'user.display.name': "Nome Completo",
            'password.retype': "Confirmação de senha",
            'register': "Criar conta",
            'already.got.an.account': "Já possui uma conta?",
            'account.creation.success': "Sua conta foi criada com sucesso!",
            'account.creation.error': "Ocorreu um erro na criação de sua conta, por favor verifique os dados informados",
            'required': "* Obrigatório",
            'invalid.mail': "* Email inválido",
            'user.name.validation.pattern': "* O nome deve conter de 4 a 20 caracteres (utilize somente letras minúsculas e números)",
            'only.letters': "* Somente letras",
            'password.dont.match': "* Senha não confere",
            'max.field.limit': "* No máximo {{max}} caracteres",
            'min.field.limit': "* No minímo {{min}} caracteres"
        });
        $translateProvider.preferredLanguage('pt');
    }])
;