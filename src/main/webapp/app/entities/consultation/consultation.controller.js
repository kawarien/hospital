(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('ConsultationController', ConsultationController);

    ConsultationController.$inject = ['Consultation'];

    function ConsultationController(Consultation) {

        var vm = this;

        vm.consultations = [];

        loadAll();

        function loadAll() {
            Consultation.query(function(result) {
                vm.consultations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
