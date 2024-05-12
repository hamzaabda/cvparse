import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) { }

  createOffreEmploi(offreEmploi: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, offreEmploi);
  }

  deleteOffreEmploi(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }

  updateOffreEmploi(id: number, offreEmploi: any): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.put<any>(url, offreEmploi);
  }

  getOffreEmploiById(id: number): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<any>(url);
  }

  getAllOffresEmploi(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }







  
}
