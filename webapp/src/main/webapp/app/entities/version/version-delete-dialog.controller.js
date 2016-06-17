(function() {
    'use strict';

    angular
        .module('webappApp')
        .controller('VersionDeleteController',VersionDeleteController);

    VersionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Version'];

    function VersionDeleteController($uibModalInstance, entity, Version) {
        var vm = this;

        vm.version = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Version.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
