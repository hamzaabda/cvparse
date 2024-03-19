import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  userEmail: string; // Propriété pour stocker l'email de l'utilisateur

  constructor(private http: HttpClient) { }

  loginUser(data): Observable<any> {
    // Code pour la connexion de l'utilisateur
    // Une fois l'utilisateur connecté, vous pouvez définir son email
    this.userEmail = data.email;
    return this.http.post('http://localhost:8080/auth/login', data);
  }

  resetPassword(email: string): Observable<string> {
    const resetPasswordUrl = `http://localhost:8080/auth/reset-password?email=${email}`;
    return this.http.post(resetPasswordUrl, {}, { responseType: 'text' });
  }
}
