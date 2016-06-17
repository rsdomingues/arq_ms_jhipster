(function() {
    'use strict';

    angular
        .module('webappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('kibana', {
            parent: 'entity',
            url: '/kibana',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webappApp.version.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/kibana/kibana.html',
                    controller: 'VersionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('version');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
