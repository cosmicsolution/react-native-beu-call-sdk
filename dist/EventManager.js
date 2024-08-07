import { NativeEventEmitter, } from 'react-native';
export var BEU_INCOMING_CALL_INFO_RECEIVED = 'IncomingCallInfoReceived';
var EventManager = /** @class */ (function () {
    function EventManager(BeUCallSDK) {
        this.BeUCallSDK = BeUCallSDK;
        this.beuCallEventEmitter = new NativeEventEmitter(BeUCallSDK);
        this.eventListenerArrayMap = new Map();
        this.listeners = {};
        this.setupListeners();
    }
    EventManager.prototype.setupListeners = function () {
        if (this.BeUCallSDK != null) {
            this.listeners.receiveIncomingCallPayload = this.generateEventListener('receiveIncomingCallPayload');
        }
    };
    EventManager.prototype.generateEventListener = function (eventName) {
        var _this = this;
        var addListenerCallback = function (payload) {
            var handlerArray = _this.eventListenerArrayMap.get(eventName);
            if (handlerArray) {
                if (eventName === 'receiveIncomingCallPayload') {
                    handlerArray.forEach(function (handler) {
                        handler(payload);
                    });
                }
                else {
                    handlerArray.forEach(function (handler) {
                        handler(payload);
                    });
                }
            }
        };
        return this.beuCallEventEmitter.addListener(eventName, addListenerCallback);
    };
    EventManager.prototype.addEventListener = function (eventName, handler) {
        var handlerArray = this.eventListenerArrayMap.get(eventName);
        handlerArray && handlerArray.length > 0
            ? handlerArray.push(handler)
            : this.eventListenerArrayMap.set(eventName, [handler]);
    };
    EventManager.prototype.removeEventListener = function (eventName, handler) {
        var handlerArray = this.eventListenerArrayMap.get(eventName);
        if (!handlerArray) {
            return;
        }
        var index = handlerArray.indexOf(handler);
        if (index !== -1) {
            handlerArray.splice(index, 1);
        }
        if (handlerArray.length === 0) {
            this.eventListenerArrayMap.delete(eventName);
        }
    };
    EventManager.prototype.removeEventListeners = function (type) {
        var handlerArray = this.eventListenerArrayMap.get(type);
        if (!handlerArray)
            return;
        this.eventListenerArrayMap.delete(type);
    };
    return EventManager;
}());
export default EventManager;
