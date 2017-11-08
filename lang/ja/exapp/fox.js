import { Platform } from 'react-native';
import FoxAndroid from './fox-android.js';
import FoxIos from './fox-ios.js';

var CYZFox = {};

CYZFox.platformSelect = function(obj) {
    var _Obj = Object.assign(obj, obj[Platform.OS]);
    delete _Obj.android;
    delete _Obj.ios;
    console.log(`[RCT-FOX] platformSelect result : ${JSON.stringify(_Obj)}`);
    return _Obj;
};

CYZFox.activate = function(foxConfig) {
    console.log(`[RCT-FOX] activate : ${JSON.stringify(foxConfig)}`);
    const _foxConfig = CYZFox.platformSelect(foxConfig);
    if (Platform.OS === 'ios') {
        FoxIos.activate(_foxConfig);
    } else if (Platform.OS === 'android') {
        FoxAndroid.activate(_foxConfig);
    } else {
        console.log(`[RCT-FOX] Unsupported platform : ${Platform.OS}`);
    }
};

CYZFox.trackInstall = function(foxTrackOption) {
    console.log(`[RCT-FOX] trackInstall : ${JSON.stringify(foxTrackOption)}`);
    const _foxTrackOption = CYZFox.platformSelect(foxTrackOption);
    if (Platform.OS === 'ios') {
        FoxIos.trackInstall(_foxTrackOption);
    } else if (Platform.OS === 'android') {
        FoxAndroid.trackInstall(_foxTrackOption);
    } else {
        console.log(`[RCT-FOX] Unsupported platform : ${Platform.OS}`);
    }
};

CYZFox.trackSession = function() {
    console.log(`[RCT-FOX] trackSession`);
    if (Platform.OS === 'ios') {
        FoxIos.trackSession();
    } else if (Platform.OS === 'android') {
        FoxAndroid.trackSession();
    } else {
        console.log(`[RCT-FOX] Unsupported platform : ${Platform.OS}`);
    }
};

CYZFox.trackEvent = function(foxEvent) {
    console.log(`[RCT-FOX] trackEvent`);
    const _foxEvent = CYZFox.platformSelect(foxEvent);
    if (Platform.OS === 'ios') {
        FoxIos.trackEvent(_foxEvent);
    } else if (Platform.OS === 'android') {
        FoxAndroid.trackEvent(_foxEvent);
    } else {
        console.log(`[RCT-FOX] Unsupported platform : ${Platform.OS}`);
    }
};

CYZFox.setUserInfo = function(userInfo) {
    console.log(`[RCT-FOX] setUserInfo : ${JSON.stringify(userInfo)}`);
    const _userInfo = CYZFox.platformSelect(userInfo);
    if (Platform.OS === 'ios') {
        FoxIos.setUserInfo(_userInfo);
    } else if (Platform.OS === 'android') {
        FoxAndroid.setUserInfo(_userInfo);
    } else {
        console.log(`[RCT-FOX] Unsupported platform : ${Platform.OS}`);
    }
};

CYZFox.getUserInfo = function(userInfoCallback) {
    console.log(`[RCT-FOX] set getUserInfo callback`);
    if (Platform.OS === 'ios') {
        FoxIos.getUserInfo(userInfoCallback);
    } else if (Platform.OS === 'android') {
        FoxAndroid.getUserInfo(userInfoCallback);
    } else {
        console.log(`[RCT-FOX] Unsupported platform : ${Platform.OS}`);
    }
};

module.exports = CYZFox;
