import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reclamation } from '../reclamation-management/reclamation.model'; // Define the Reclamation model based on your backend structure

@Injectable({
  providedIn: 'root',
})
export class ReclamationService {
  private apiUrl = 'http://localhost:8080/auth/reclamations'; // Replace with your actual API URL

  constructor(private http: HttpClient) {}

  getAllReclamations(): Observable<Reclamation[]> {
    return this.http.get<Reclamation[]>(this.apiUrl);
  }

  getReclamationById(id: number): Observable<Reclamation> {
    return this.http.get<Reclamation>(`${this.apiUrl}/${id}`);
  }

  replyToReclamation(id: number, responseMessage: string): Observable<string> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    // Specify responseType as 'text' to handle plain text responses
    return this.http.post<string>(`${this.apiUrl}/${id}/reply`, responseMessage, { headers, responseType: 'text' as 'json' });
  }
  deleteReclamation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}

