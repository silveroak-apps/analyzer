'use strict'

var module = angular.module('bot.controllers', []);
module.controller("UserController", [ "$scope", "UserService",
		function($scope, UserService) {

			$scope.getSignals = function() {
					UserService.getAllSignals().then(function(value) {
						$scope.allUsers= value.data;
					}, function(reason) {
						console.log("error occured");
					}, function(value) {
						console.log("no callback");
					});
				}, function(value) {
					console.log("no callback");
			}
		} ]);