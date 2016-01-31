'use strict';

var {
  NativeModules,
  DeviceEventEmitter,
} = require('react-native');

var NotificationAndroid = NativeModules.NotificationAndroid;
var _notifHandlers = new Map();

var DEVICE_NOTIF_EVENT = 'remoteNotificationReceived';
var NOTIF_REGISTER_EVENT = 'remoteNotificationsRegistered';

var NotificationComponent = function(){
	this.initialPop = false;
};

NotificationComponent.prototype.register = function(senderID: String){
	NotificationAndroid.register(senderID);
};

NotificationComponent.prototype.notify = function(details: Object){
	NotificationAndroid.notify(details);
};

NotificationComponent.prototype.schedule = function(details: Object){
	NotificationAndroid.schedule(details);
};

NotificationComponent.prototype.addEventListener = function(type: String, handler: Function){
	var listener;
	if(type == 'notification'){
		listener = DeviceEventEmitter.addListener(
			DEVICE_NOTIF_EVENT,
			function(notifyData){
				var data = JSON.parse(notifyData.dataJSON);
				handler(data);
			}
		);
	}else if(type == 'register'){
		listener= DeviceEventEmitter.addListener(
			NOTIF_REGISTER_EVENT,
			function(registrationInfo){
				handler(registrationInfo.deviceToken);
			}
		);
	}

	_notifHandlers.set(handler,listener);
}

NotificationComponent.prototype.removeEventListener = function(type: String, handler: Function){
	var listener = _notifHandlers.get(handler);
	if(!listener){
		return;
	}
	listener.remove();
	_notifHandlers.delete(handler);
}

module.exports = {
	state: false,
	component: new NotificationComponent() 
};