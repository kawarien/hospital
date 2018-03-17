(function() {
    'use strict';

    angular
        .module('dbHospitalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('consultation', {
            parent: 'entity',
            url: '/consultation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dbHospitalApp.consultation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/consultation/consultations.html',
                    controller: 'ConsultationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('consultation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('consultation-detail', {
            parent: 'consultation',
            url: '/consultation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dbHospitalApp.consultation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/consultation/consultation-detail.html',
                    controller: 'ConsultationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('consultation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Consultation', function($stateParams, Consultation) {
                    return Consultation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'consultation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('consultation-detail.edit', {
            parent: 'consultation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/consultation/consultation-dialog.html',
                    controller: 'ConsultationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Consultation', function(Consultation) {
                            return Consultation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('consultation.new', {
            parent: 'consultation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/consultation/consultation-dialog.html',
                    controller: 'ConsultationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                prescription: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('consultation', null, { reload: 'consultation' });
                }, function() {
                    $state.go('consultation');
                });
            }]
        })
        .state('consultation.edit', {
            parent: 'consultation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/consultation/consultation-dialog.html',
                    controller: 'ConsultationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Consultation', function(Consultation) {
                            return Consultation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('consultation', null, { reload: 'consultation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('consultation.delete', {
            parent: 'consultation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/consultation/consultation-delete-dialog.html',
                    controller: 'ConsultationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Consultation', function(Consultation) {
                            return Consultation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('consultation', null, { reload: 'consultation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
