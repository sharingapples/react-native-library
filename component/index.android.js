'use strict';

var {
  NativeModules,
  DeviceEventEmitter,
} = require('react-native');

var NotificationAndroid = NativeModules.NotificationAndroid;

var NotificationComponent = function(){
	this.initialPop = false;
};

NotificationComponent.prototype.register = function(senderID: String){
	NotificationAndroid.register(senderID);
};

NotificationComponent.prototype.notify = function(details: Object){
	NotificationAndroid.notify(details);
};

NotificationComponent.prototype.schedule = function(){

};


module.exports = {
	state: false,
	component: new NotificationComponent() 
};