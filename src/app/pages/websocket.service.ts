import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket: WebSocket;
  private subject: Subject<any> = new Subject<any>();

  constructor() { }

  connect(url: string): Observable<any> {
    this.socket = new WebSocket(url);

    this.socket.onmessage = (event) => {
      this.subject.next(JSON.parse(event.data));
    };

    return this.subject.asObservable();
  }

  sendMessage(message: any) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(JSON.stringify(message));
    }
  }

  closeConnection() {
    if (this.socket) {
      this.socket.close();
    }
  }
}
