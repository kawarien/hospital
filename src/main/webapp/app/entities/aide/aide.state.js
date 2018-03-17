(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('aide', {
            parent: 'entity',
            url: '/aide',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dbHospitalApp.aide.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/aide/aides.html',
                    controller: 'AideController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('aide');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('aide-detail', {
            parent: 'aide',
            url: '/aide/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dbHospitalApp.aide.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/aide/aide-detail.html',
                    controller: 'AideDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('aide');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Aide', function($stateParams, Aide) {
                    return Aide.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'aide',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('aide-detail.edit', {
            parent: 'aide-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/aide/aide-dialog.html',
                    controller: 'AideDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Aide', function(Aide) {
                            return Aide.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('aide.new', {
            parent: 'aide',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/aide/aide-dialog.html',
                    controller: 'AideDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('aide', null, { reload: 'aide' });
                }, function() {
                    $state.go('aide');
                });
            }]
        })
        .state('aide.edit', {
            parent: 'aide',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/aide/aide-dialog.html',
                    controller: 'AideDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Aide', function(Aide) {
                            return Aide.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('aide', null, { reload: 'aide' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('aide.delete', {
            parent: 'aide',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/aide/aide-delete-dialog.html',
                    controller: 'AideDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Aide', function(Aide) {
                            return Aide.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('aide', null, { reload: 'aide' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
