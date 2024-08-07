import {
  type EmitterSubscription,
  NativeEventEmitter,
  type NativeModule,
} from 'react-native';
import type { Events, EventsPayload } from './types';

export const BEU_INCOMING_CALL_INFO_RECEIVED = 'IncomingCallInfoReceived';

export default class EventManager {
  BeUCallSDK: NativeModule;
  beuCallEventEmitter: NativeEventEmitter;
  eventListenerArrayMap: Map<string, Array<(event: any) => void>>;
  listeners: { [key: string]: EmitterSubscription };

  constructor(BeUCallSDK: NativeModule) {
    this.BeUCallSDK = BeUCallSDK;
    this.beuCallEventEmitter = new NativeEventEmitter(BeUCallSDK);
    this.eventListenerArrayMap = new Map();
    this.listeners = {};
    this.setupListeners();
  }

  setupListeners() {
    if (this.BeUCallSDK != null) {
      this.listeners.receiveIncomingCallPayload = this.generateEventListener(
        'receiveIncomingCallPayload'
      );
    }
  }

  generateEventListener(eventName: Events): any {
    const addListenerCallback = (payload: Object) => {
      let handlerArray = this.eventListenerArrayMap.get(eventName);
      if (handlerArray) {
        if (eventName === 'receiveIncomingCallPayload') {
          handlerArray.forEach((handler) => {
            handler(payload as EventsPayload['receiveIncomingCallPayload']);
          });
        } else {
          handlerArray.forEach((handler) => {
            handler(payload);
          });
        }
      }
    };

    return this.beuCallEventEmitter.addListener(eventName, addListenerCallback);
  }

  addEventListener<K extends Events>(
    eventName: K,
    handler: (event: EventsPayload[K]) => void
  ) {
    let handlerArray = this.eventListenerArrayMap.get(eventName);
    handlerArray && handlerArray.length > 0
      ? handlerArray.push(handler)
      : this.eventListenerArrayMap.set(eventName, [handler]);
  }

  removeEventListener(eventName: string, handler: any) {
    const handlerArray = this.eventListenerArrayMap.get(eventName);
    if (!handlerArray) {
      return;
    }
    const index = handlerArray.indexOf(handler);
    if (index !== -1) {
      handlerArray.splice(index, 1);
    }
    if (handlerArray.length === 0) {
      this.eventListenerArrayMap.delete(eventName);
    }
  }

  removeEventListeners<K extends Events>(type: K): void {
    const handlerArray = this.eventListenerArrayMap.get(type);
    if (!handlerArray) return;

    this.eventListenerArrayMap.delete(type);
  }
}
