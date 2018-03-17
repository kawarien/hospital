(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('RendezvousDialogController', RendezvousDialogController);

    RendezvousDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Rendezvous', 'User'];

    function RendezvousDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Rendezvous, User) {
        var vm = this;

        vm.rendezvous = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.rendezvous.id !== null) {
                Rendezvous.update(vm.rendezvous, onSaveSuccess, onSaveError);
            } else {
                Rendezvous.save(vm.rendezvous, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dbHospitalApp:rendezvousUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
