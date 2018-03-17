(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('RendezvousDetailController', RendezvousDetailController);

    RendezvousDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Rendezvous', 'User'];

    function RendezvousDetailController($scope, $rootScope, $stateParams, previousState, entity, Rendezvous, User) {
        var vm = this;

        vm.rendezvous = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dbHospitalApp:rendezvousUpdate', function(event, result) {
            vm.rendezvous = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
