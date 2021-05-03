'use strict'

var module = angular.module('bot.controllers', []);
module.controller("SignalController", [ "$scope", "SignalService",
		function($scope, SignalService) {

			$scope.getSignals = function() {
                SignalService.getAllSignals().then(function(value) {
                	$scope.allSignals= value.data;

					}, function(reason) {
						console.log("error occured"+reason);
					}, function(value) {
						console.log("no callback"+value);
					});
				}, function(value) {
					console.log("no callback" + value);
			};
		} ]);