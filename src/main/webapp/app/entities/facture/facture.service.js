(function() {
    'use strict';
    angular
        .module('dbHospitalApp')
        .factory('Facture', Facture);

    Facture.$inject = ['$resource'];

    function Facture ($resource) {
        var resourceUrl =  'api/factures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
