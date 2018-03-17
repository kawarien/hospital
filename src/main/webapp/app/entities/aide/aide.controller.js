(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('AideController', AideController);

    AideController.$inject = ['Aide'];

    function AideController(Aide) {

        var vm = this;

        vm.aides = [];

        loadAll();

        function loadAll() {
            Aide.query(function(result) {
                vm.aides = result;
                vm.searchQuery = null;
            });
        }
    }
})();
