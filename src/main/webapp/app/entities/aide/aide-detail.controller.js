(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .controller('AideDetailController', AideDetailController);

    AideDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Aide', 'User'];

    function AideDetailController($scope, $rootScope, $stateParams, previousState, entity, Aide, User) {
        var vm = this;

        vm.aide = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dbHospitalApp:aideUpdate', function(event, result) {
            vm.aide = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
