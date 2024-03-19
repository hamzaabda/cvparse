import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) { }

  register(userData: any) {
    // Envoyer une requête POST vers l'URL d'inscription avec les données utilisateur
    return this.http.post('http://localhost:8080/auth/register', userData);
  }
}
