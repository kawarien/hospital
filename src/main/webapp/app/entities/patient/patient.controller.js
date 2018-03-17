(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('PatientController', PatientController);

    PatientController.$inject = ['Patient'];

    function PatientController(Patient) {

        var vm = this;

        vm.patients = [];

        loadAll();

        function loadAll() {
            Patient.query(function(result) {
                vm.patients = result;
                vm.searchQuery = null;
            });
        }
    }
})();
