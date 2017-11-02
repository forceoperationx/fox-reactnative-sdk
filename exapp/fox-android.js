import { NativeModules } from 'react-native';
const foxConfig = NativeModules.CYZFoxConfig;
const fox = NativeModules.CYZFox;
const foxEvent = NativeModules.CYZFoxEvent;

var FoxAndroid = {}

FoxAndroid.activate = function(config) {
  var isDefaultDebug = false;
  if (config.debugModeEnabled != undefined) {
    isDefaultDebug = config.debugModeEnabled;
  }
  foxConfig.activate(config.appId, config.appKey, config.appSalt, isDefaultDebug);
}

FoxAndroid.trackInstall = function(foxTrackOption) {
  if (foxTrackOption != undefined) {
    if (foxTrackOption.redirectURL != undefined) fox.addOptionRedirectUrl(foxTrackOption.redirectURL);
    if (foxTrackOption.buid != undefined) fox.addOptionBuid(foxTrackOption.buid);
    var duration = 0;
    if (foxTrackOption.durationSinceClick != undefined) duration = foxTrackOption.durationSinceClick;
    if (foxTrackOption.deferredDeeplinkHandler != undefined) fox.registerDeeplinkHandler(duration, foxTrackOption.deferredDeeplinkHandler);
    if (foxTrackOption.trackingCompletionHandler != undefined) fox.setTrackingStateListener(foxTrackOption.trackingCompletionHandler);
  }
  fox.trackInstall();
}

FoxAndroid.trackSession = function() {
  fox.trackSession();
}

FoxAndroid.trackEvent = function(e) {
    foxEvent.track(e);
}

FoxAndroid.setUserInfo = function(userInfo) {
    fox.setUserInfo(userInfo);
}

FoxAndroid.getUserInfo = function(userInfoCallback) {
    fox.getUserInfo(userInfoCallback);
}

module.exports = FoxAndroid;
