'use strict';

var {
  NativeModules,
  DeviceEventEmitter,
} = require('react-native');

var NotificationAndroid = NativeModules.NotificationAndroid;
var _notifHandlers = new Map();

var DEVICE_NOTIFICATION_EVENT = "remoteNotificationReceived";
var DEVICE_REGISTERED_EVENT = "remoteNotificationsRegistered";

var NotificationsComponent = function(){
	this.initalPop = false;
}


NotificationsComponent.prototype.register = function(senderID: String){
	NotificationAndroid.register(senderID);
};

NotificationsComponent.prototype.notify = function(details: Object){
	NotificationAndroid.notify(details);
};

NotificationsComponent.prototype.schedule = function(time: Integer, details: Object){
	NotificationAndroid.schedule(time,details);
};

NotificationsComponent.prototype.scheduleAfter = function(time: Integer,details Object ){
	NotificationAndroid.scheduleAfter(time,details);
};

NotificationsComponent.addEventListener = function(type: String, handler: Function){
	var listener;

	if(type=='notification'){
		listener = DeviceEventEmitter.addListener(DEVICE_NOTIFICATION_EVENT,
			function(notifiData){
				var data = JSON.parse(notifiData.dataJSON);
				handler(data);
			}
		);
	}else if (type == 'register'){
		listener = DeviceEventEmitter.addListener(DEVICE_REGISTERED_EVENT,
			function(registrationInfo){
				handler(registrationInfo.deviceToken);
			}
		);
	}
	_notifHandlers.set(handler,listener);
};

NotificationsComponent.prototype.removeEventListener = function(type: string, handler Function){
	var listener = _notifHandlers.get(handler);
	if(!listener){
		return;
	}
	listener.remove();
	_notifHandlers.delete(handler);
}

module.exports = {
	state : false,
	component: new NotificationsComponent()
};