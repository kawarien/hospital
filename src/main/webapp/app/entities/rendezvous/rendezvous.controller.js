(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('RendezvousController', RendezvousController);

    RendezvousController.$inject = ['Rendezvous'];

    function RendezvousController(Rendezvous) {

        var vm = this;

        vm.rendezvous = [];

        loadAll();

        function loadAll() {
            Rendezvous.query(function(result) {
                vm.rendezvous = result;
                vm.searchQuery = null;
            });
        }
    }
})();
