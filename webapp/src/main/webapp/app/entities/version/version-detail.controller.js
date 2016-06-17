(function() {
    'use strict';

    angular
        .module('webappApp')
        .controller('VersionDetailController', VersionDetailController);

    VersionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Version'];

    function VersionDetailController($scope, $rootScope, $stateParams, entity, Version) {
        var vm = this;

        vm.version = entity;

        var unsubscribe = $rootScope.$on('webappApp:versionUpdate', function(event, result) {
            vm.version = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
