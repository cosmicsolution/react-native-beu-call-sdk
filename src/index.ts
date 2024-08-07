import { NativeModules, Platform, type NativeModule } from 'react-native';
import EventManager from './EventManager';
import type { Events, EventsPayload } from './types';

const LINKING_ERROR =
  `The package 'react-native-beu-call-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const BeuCallSdk = NativeModules.BeuCallSdk
  ? NativeModules.BeuCallSdk
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );
const eventManager = new EventManager(BeuCallSdk);

function isNativeModuleLoaded(module: NativeModule): boolean {
  if (module == null) {
    console.error(
      'Could not load RNOneSignal native module. Make sure native dependencies are properly linked.'
    );

    return false;
  }

  return true;
}

function isValidCallback(handler: Function) {
  const valid = typeof handler === 'function';
  if (!valid) {
    console.error(`[BeuCallSdk] invalid listener`);
  }
  return valid;
}

export function multiply(a: number, b: number): Promise<number> {
  return BeuCallSdk.multiply(a, b);
}

export function addEventListener<K extends Events>(
  event: K,
  listener: (event: EventsPayload[K]) => void
): void {
  if (!isNativeModuleLoaded(BeuCallSdk)) return;
  if (!isValidCallback(listener)) return;

  eventManager.addEventListener(event, listener);
}

export function removeEventListeners(type: Events): void {
  return eventManager.removeEventListeners(type);
}

export function removeEventListener<K extends Events>(
  eventName: K,
  handler: (event: EventsPayload[K]) => void
) {
  return eventManager.removeEventListener(eventName, handler);
}
