import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators'; // Import de l'opérateur tap

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) { }

  // Méthode pour obtenir le nombre d'inscriptions
  getRegistrationsCount(token: string): Observable<number> {
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.get<number>(`${this.apiUrl}/registrations/count`, { headers })
      .pipe(
        tap(registrationsCount => console.log('Total registrations count:', registrationsCount))
      );
  }

  // Méthode pour obtenir le nombre de réinitialisations de mot de passe
  getPasswordResetsCount(token: string): Observable<number> {
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.get<number>(`${this.apiUrl}/password-resets/count`, { headers })
      .pipe(
        tap(resetsCount => console.log('Total password resets count:', resetsCount))
      );
  }

  // Méthode pour obtenir le nombre de connexions
  getLoginsCount(token: string): Observable<number> {
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.get<number>(`${this.apiUrl}/login/count`, { headers })
      .pipe(
        tap(loginsCount => console.log('Total logins count:', loginsCount))
      );
  }

  // Ajoutez ici d'autres méthodes si nécessaire pour interagir avec votre API d'authentification
}
