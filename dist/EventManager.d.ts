import { type EmitterSubscription, NativeEventEmitter, type NativeModule } from 'react-native';
import type { Events, EventsPayload } from './types';
export declare const BEU_INCOMING_CALL_INFO_RECEIVED = "IncomingCallInfoReceived";
export default class EventManager {
    BeUCallSDK: NativeModule;
    beuCallEventEmitter: NativeEventEmitter;
    eventListenerArrayMap: Map<string, Array<(event: any) => void>>;
    listeners: {
        [key: string]: EmitterSubscription;
    };
    constructor(BeUCallSDK: NativeModule);
    setupListeners(): void;
    generateEventListener(eventName: Events): any;
    addEventListener<K extends Events>(eventName: K, handler: (event: EventsPayload[K]) => void): void;
    removeEventListener(eventName: string, handler: any): void;
    removeEventListeners<K extends Events>(type: K): void;
}
