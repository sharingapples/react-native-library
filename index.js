'use strict';

var NotificationComponent = require( './component' );

var AppState = NotificationComponent.state;
var RNNotifications = NotificationComponent.component;

var Platform = require('react-native').Platform;

var Notifications = {
	handler: RNNotifications,
	onRegister: false,
	onError: false,
	onNotification: false,
	loaded: false
};

Notifications.callNative = function(name: String, params: Array){
	if(typeof this.handler[name] == 'function'){
		if(typeof params !== 'array' &&
			typeof params !== 'object'){
			params = [];
		}
		return this.handler[name](...params);
	}else{
		return null;
	}
};

Notifications.configure = function(options: Object){
	if(typeof options.onRegister !== 'undefined'){
		this.onRegister = options.onRegister;
	}

	if(typeof options.onNotification != 'undefined'){
		this.onNotification = options.onNotification;
	}

	if(typeof options.senderID != 'undefined'){
		this.senderID = options.senderID;
	}

	if(this.loaded == false){
		this.callNative('addEventListener', ['register',this._onRegister.bind(this)])
		this.callNative('addEventListener', ['notification', this._onNotification.bind(this)])
	}

	if(options.requestToken != false){
		this.register();
	}
};

Notifications.notify = function(details: Object){
	this.handler.notify(details);
	//todo ios implementation
};

Notifications._onRegister = function(token: String){
	if(this.onRegister !== false){
		this.onRegister(token);
	}
};

Notifications._onNotification = function(data){
	//todo ios implementation

	if(this.onNotification !== false){
		this.onNotification(data);
	}

};

Notifications.register = function(){
	//todo ios

	return this.callNative('register',[this.senderID]);
}

module.exports = Notifications;