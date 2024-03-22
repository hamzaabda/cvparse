import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class DeconnexionService {

  constructor(private http: HttpClient, private router: Router) { }

  deconnectAndRedirect(): void {
    // Supprimer le token du localStorage
    localStorage.removeItem('token');

    // Effectuer la déconnexion en utilisant le lien http://localhost:8080/auth/logout
    this.http.post('http://localhost:8080/auth/logout', {}).subscribe(
      () => {
        // Déconnexion réussie, rediriger vers la page de login
        this.router.navigate(['/login']);
      },
      error => {
        console.error('Erreur lors de la déconnexion :', error);
        // Rediriger vers la page de login même en cas d'erreur
        this.router.navigate(['/login']);
      }
    );
  }
}
