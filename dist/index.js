import { NativeModules, Platform } from 'react-native';
import EventManager from './EventManager';
var LINKING_ERROR = "The package 'react-native-beu-call-sdk' doesn't seem to be linked. Make sure: \n\n" +
    Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
    '- You rebuilt the app after installing the package\n' +
    '- You are not using Expo Go\n';
var BeuCallSdk = NativeModules.BeuCallSdk
    ? NativeModules.BeuCallSdk
    : new Proxy({}, {
        get: function () {
            throw new Error(LINKING_ERROR);
        },
    });
var eventManager = new EventManager(BeuCallSdk);
function isNativeModuleLoaded(module) {
    if (module == null) {
        console.error('Could not load RNOneSignal native module. Make sure native dependencies are properly linked.');
        return false;
    }
    return true;
}
function isValidCallback(handler) {
    var valid = typeof handler === 'function';
    if (!valid) {
        console.error("[BeuCallSdk] invalid listener");
    }
    return valid;
}
export function multiply(a, b) {
    return BeuCallSdk.multiply(a, b);
}
export function addEventListener(event, listener) {
    if (!isNativeModuleLoaded(BeuCallSdk))
        return;
    if (!isValidCallback(listener))
        return;
    eventManager.addEventListener(event, listener);
}
export function removeEventListeners(type) {
    return eventManager.removeEventListeners(type);
}
export function removeEventListener(eventName, handler) {
    return eventManager.removeEventListener(eventName, handler);
}
