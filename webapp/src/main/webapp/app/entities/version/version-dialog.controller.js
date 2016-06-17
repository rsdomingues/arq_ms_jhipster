(function() {
    'use strict';

    angular
        .module('webappApp')
        .controller('VersionDialogController', VersionDialogController);

    VersionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Version'];

    function VersionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Version) {
        var vm = this;

        vm.version = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.version.id !== null) {
                Version.update(vm.version, onSaveSuccess, onSaveError);
            } else {
                Version.save(vm.version, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('webappApp:versionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.releaseDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
