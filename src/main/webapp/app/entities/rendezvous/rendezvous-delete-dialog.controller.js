(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('RendezvousDeleteController',RendezvousDeleteController);

    RendezvousDeleteController.$inject = ['$uibModalInstance', 'entity', 'Rendezvous'];

    function RendezvousDeleteController($uibModalInstance, entity, Rendezvous) {
        var vm = this;

        vm.rendezvous = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Rendezvous.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
