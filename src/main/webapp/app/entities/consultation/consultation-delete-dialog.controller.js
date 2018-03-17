(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('ConsultationDeleteController',ConsultationDeleteController);

    ConsultationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Consultation'];

    function ConsultationDeleteController($uibModalInstance, entity, Consultation) {
        var vm = this;

        vm.consultation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Consultation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
