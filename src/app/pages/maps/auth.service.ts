import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/auth/offres-stage';

  constructor(private http: HttpClient) { }

  // Méthode pour créer une nouvelle offre de stage
  createOffre(offreData: any): Observable<any> {
    return this.http.post(this.baseUrl, offreData);
  }

  // Méthode pour récupérer toutes les offres de stage
  getAllOffres(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  // Méthode pour récupérer une offre de stage par son ID
  getOffreById(id: number): Observable<any> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get(url);
  }

  // Méthode pour mettre à jour une offre de stage
  updateOffre(id: number, updatedOffreData: any): Observable<any> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.put(url, updatedOffreData);
  }

  // Méthode pour supprimer une offre de stage
  deleteOffre(id: number): Observable<any> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete(url);
  }
}
