export type NativeEvents = {
  receiveIncomingCallPayload: 'RNBeuCallSDKIncomingCallPayload';
};

export type InitialEvents = Array<
  {
    [Event in Events]: {
      name: NativeEvents[Event];
      data: EventsPayload[Event];
    };
  }[Events]
>;

export type Events = keyof NativeEvents;
export type EventsPayload = {
  receiveIncomingCallPayload: {
    type: string;
    action: string;
    answer: string;
    roomUrl: string;
    roomToken?: string;
    callerName: string;
    callerId: string;
    calleeIds: string;
  };
};

export class EventListener {
  remove(): void {
    // TODO: implement
  }
}
