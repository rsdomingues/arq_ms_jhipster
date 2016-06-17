(function() {
    'use strict';
    angular
        .module('webappApp')
        .factory('Version', Version);

    Version.$inject = ['$resource', 'DateUtils'];

    function Version ($resource, DateUtils) {
        var resourceUrl =  'service/' + 'api/versions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.releaseDate = DateUtils.convertLocalDateFromServer(data.releaseDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.releaseDate = DateUtils.convertLocalDateToServer(data.releaseDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.releaseDate = DateUtils.convertLocalDateToServer(data.releaseDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
