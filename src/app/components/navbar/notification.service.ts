import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = 'http://localhost:8080/auth/notifications';

  constructor(private http: HttpClient) {}

  getNotifications(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }


  markAsRead(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/read`, {});
  }
}
