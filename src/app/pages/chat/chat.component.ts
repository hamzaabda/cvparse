import { Component, OnInit } from '@angular/core';
import { trigger, transition, style, animate } from '@angular/animations';
import { WebSocketService } from '../websocket.service';
import { AuthService } from '../login/auth.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
  animations: [
    trigger('messageAnimation', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('0.5s ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ]
})
export class ChatComponent implements OnInit {
  messages: any[] = [];
  messageContent: string = '';
  sender: string;

  constructor(private webSocketService: WebSocketService, private authService: AuthService) { }

  ngOnInit(): void {
    this.sender = this.authService.userEmail;

    this.webSocketService.connect('ws://localhost:8080/chat')
      .subscribe(message => {
        message.content = this.cleanMessage(message.content);
        this.messages.push(message);
      });
  }

  sendMessage() {
    const cleanedContent = this.cleanMessage(this.messageContent);
    const message = {
      sender: this.sender,
      content: cleanedContent,
      timestamp: new Date().toISOString()
    };

    this.webSocketService.sendMessage(message);
    this.messageContent = '';
  }

  cleanMessage(content: string): string {
    return content.replace(/\d+/g, '').trim();
  }

  ngOnDestroy(): void {
    this.webSocketService.closeConnection();
  }
}
