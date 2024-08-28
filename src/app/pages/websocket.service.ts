import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private socket: WebSocket;
  private messageSubject = new Subject<string>();

  public messages = this.messageSubject.asObservable();

  constructor() {
    this.connect();
  }

  private connect(): void {
    this.socket = new WebSocket('wss://echo.websocket.org');

    this.socket.onmessage = (event: MessageEvent) => {
      this.messageSubject.next(event.data);
    };

    this.socket.onerror = (error: Event) => {
      console.error('WebSocket error:', error);
    };

    this.socket.onclose = () => {
      console.log('WebSocket connection closed');
    };
  }

  public sendMessage(message: string): void {
    if (this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(message);
    }
  }
}
