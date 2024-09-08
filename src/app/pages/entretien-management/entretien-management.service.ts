import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EntretienManagementService {

  private baseUrl = 'http://localhost:8080/auth'; // Remplacez par l'URL de votre backend

  constructor(private http: HttpClient) {}

  getAcceptedCandidateEmails(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/accepted`);
  }

  saveAcceptedEmails(): Observable<string> {
    return this.http.get<string>(`${this.baseUrl}/save`);
  }

  acceptCandidate(email: string): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/accept/${email}`, {});
  }

  rejectCandidate(email: string): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/reject/${email}`, {});
  }
}
