(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rendezvous', {
            parent: 'entity',
            url: '/rendezvous',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dbHospitalApp.rendezvous.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rendezvous/rendezvous.html',
                    controller: 'RendezvousController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rendezvous');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rendezvous-detail', {
            parent: 'rendezvous',
            url: '/rendezvous/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dbHospitalApp.rendezvous.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rendezvous/rendezvous-detail.html',
                    controller: 'RendezvousDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rendezvous');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Rendezvous', function($stateParams, Rendezvous) {
                    return Rendezvous.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rendezvous',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rendezvous-detail.edit', {
            parent: 'rendezvous-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rendezvous/rendezvous-dialog.html',
                    controller: 'RendezvousDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rendezvous', function(Rendezvous) {
                            return Rendezvous.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rendezvous.new', {
            parent: 'rendezvous',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rendezvous/rendezvous-dialog.html',
                    controller: 'RendezvousDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rendezvous', null, { reload: 'rendezvous' });
                }, function() {
                    $state.go('rendezvous');
                });
            }]
        })
        .state('rendezvous.edit', {
            parent: 'rendezvous',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rendezvous/rendezvous-dialog.html',
                    controller: 'RendezvousDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rendezvous', function(Rendezvous) {
                            return Rendezvous.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rendezvous', null, { reload: 'rendezvous' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rendezvous.delete', {
            parent: 'rendezvous',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rendezvous/rendezvous-delete-dialog.html',
                    controller: 'RendezvousDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Rendezvous', function(Rendezvous) {
                            return Rendezvous.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rendezvous', null, { reload: 'rendezvous' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
