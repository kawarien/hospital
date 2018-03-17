(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('AideDeleteController',AideDeleteController);

    AideDeleteController.$inject = ['$uibModalInstance', 'entity', 'Aide'];

    function AideDeleteController($uibModalInstance, entity, Aide) {
        var vm = this;

        vm.aide = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Aide.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
