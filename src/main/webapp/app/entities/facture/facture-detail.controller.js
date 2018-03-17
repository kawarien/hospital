(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('FactureDetailController', FactureDetailController);

    FactureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Facture', 'User'];

    function FactureDetailController($scope, $rootScope, $stateParams, previousState, entity, Facture, User) {
        var vm = this;

        vm.facture = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dbHospitalApp:factureUpdate', function(event, result) {
            vm.facture = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
