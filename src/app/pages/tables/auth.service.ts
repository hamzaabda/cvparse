import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) { }

  createRecruiter(recruiterData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/recruiters`, recruiterData, { responseType: 'text' });
  }
  // Ajoutez d'autres fonctions pour la mise à jour et la suppression si nécessaire
  updateRecruiter(recruiterId: number, recruiterData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/recruiters/${recruiterId}`, recruiterData);
  }

  deleteRecruiter(recruiterId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/recruiters/${recruiterId}`);
  }
  getAllRecruiters(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/recruiters`);
  }
  getRecruiterById(recruiterId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/recruiters/${recruiterId}`);
  }

  getRecruiter(recruiterId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/recruiters/${recruiterId}`);
  }
   // Nouvelle méthode pour rechercher un recruteur par e-mail
   searchRecruiterByEmail(email: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/recruiters/search?email=${email}`);
  }
}
