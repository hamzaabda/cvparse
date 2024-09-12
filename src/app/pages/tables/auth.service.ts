import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth'; // URL du contrôleur Spring Boot

  constructor(private http: HttpClient) { }

  generatePortfolio(portfolioData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/generate`, portfolioData);
  }
}
