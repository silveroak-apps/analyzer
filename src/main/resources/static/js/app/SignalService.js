'use strict'

angular.module('bot.services', []).factory('SignalService',
		[ "$http", "CONSTANTS", function($http, CONSTANTS) {
			var service = {};
            service.getAllSignals = function() {
                return $http.get(CONSTANTS.getAllSignals);
            }
			return service;
		} ]);