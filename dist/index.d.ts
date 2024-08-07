import type { Events, EventsPayload } from './types';
export declare function multiply(a: number, b: number): Promise<number>;
export declare function addEventListener<K extends Events>(event: K, listener: (event: EventsPayload[K]) => void): void;
export declare function removeEventListeners(type: Events): void;
export declare function removeEventListener<K extends Events>(eventName: K, handler: (event: EventsPayload[K]) => void): void;
