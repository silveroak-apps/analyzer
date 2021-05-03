'use strict'

var botApp = angular.module('bot', [ 'ui.bootstrap', 'bot.controllers',
		'bot.services' ]);
botApp.constant("CONSTANTS", {
	getAllSignals : "/findallsignals"
});