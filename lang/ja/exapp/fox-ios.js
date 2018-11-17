import { NativeModules } from 'react-native';
const nativeFox = NativeModules.CYZFoxReact;

var FoxIos = {};

FoxIos.activate = function(foxConfig) {
    nativeFox.activate(foxConfig);
};

FoxIos.trackInstall = function(foxTrackOption) {
    if (foxTrackOption) {
        if (foxTrackOption.trackingCompletionHandler) {
            nativeFox.setTrackingCompletionHandler(foxTrackOption.trackingCompletionHandler);
        }
        if (foxTrackOption.deferredDeeplinkHandler) {
            nativeFox.setDeferredDeeplinkHandler(foxTrackOption.deferredDeeplinkHandler);
        }
        nativeFox.trackInstall(foxTrackOption);
    } else {
        nativeFox.trackInstall();
    }
};

FoxIos.trackSession = function() {
    nativeFox.trackSession();
};

FoxIos.trackEvent = function(foxEvent) {
    nativeFox.trackEvent(foxEvent);
};

FoxIos.setUserInfo = function(userInfo) {
    nativeFox.setUserInfo(userInfo);
};

FoxIos.getUserInfo = function(userInfoCallback) {
    nativeFox.getUserInfo(userInfoCallback);
};

module.exports = FoxIos;
