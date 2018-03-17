(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('AideDialogController', AideDialogController);

    AideDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Aide', 'User'];

    function AideDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Aide, User) {
        var vm = this;

        vm.aide = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.aide.id !== null) {
                Aide.update(vm.aide, onSaveSuccess, onSaveError);
            } else {
                Aide.save(vm.aide, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dbHospitalApp:aideUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
