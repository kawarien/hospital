(function() {
    'use strict';
    angular
        .module('dbHospitalApp')
        .factory('Aide', Aide);

    Aide.$inject = ['$resource'];

    function Aide ($resource) {
        var resourceUrl =  'api/aides/:id';

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
