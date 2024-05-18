import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  userEmail: string; // Propriété pour stocker l'e-mail de l'utilisateur

  constructor(private http: HttpClient) { }

  loginUser(data): Observable<any> {
    return this.http.post('http://localhost:8080/auth/login', data)
      .pipe(
        tap((response: any) => {
          // Une fois l'utilisateur connecté avec succès, stockez le jeton dans le localStorage ou une autre méthode de stockage
          localStorage.setItem('token', response.token);
          this.userEmail = data.email; // Définir l'e-mail de l'utilisateur
        })
      );
  }

  resetPassword(email: string): Observable<string> {
    const resetPasswordUrl = `http://localhost:8080/auth/reset-password?email=${email}`;
    return this.http.post(resetPasswordUrl, {}, { responseType: 'text' });
  }

 
}
