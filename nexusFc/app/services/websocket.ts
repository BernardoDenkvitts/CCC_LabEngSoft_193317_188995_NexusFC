import SockJS from 'sockjs-client';
import { Client, IFrame } from '@stomp/stompjs';
import apiRequest from './api-request';
import { GlobalStore } from './stores';

const makeClient = () => {
  const token = GlobalStore.get('user')?.token;
  console.log('######## token', token);
  const client = new Client({
    webSocketFactory: (): WebSocket => new SockJS(`${apiRequest.url}ws`),
    connectHeaders: {
      Authorization: `Bearer ${token}`,
    },
    debug: (str: string): void => {
      console.log(str);
    },

    onStompError: (frame: IFrame): void => {
      console.error('Broker error: ' + frame.headers['message']);
      console.error('Details: ' + frame.body);
    },
  });

  client.activate();

  return client;
};

export default makeClient;
