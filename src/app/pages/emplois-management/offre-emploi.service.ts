import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OffreEmploi } from '../emplois-management/offre-emploi.model';

@Injectable({
  providedIn: 'root'
})
export class OffreEmploiService {
  private apiUrl = 'http://localhost:8080/auth'; // Assurez-vous que cette URL correspond à votre API backend

  constructor(private http: HttpClient) { }

  getAllOffresEmploi(): Observable<OffreEmploi[]> {
    return this.http.get<OffreEmploi[]>(`${this.apiUrl}`);
  }

  applyToOffreEmploi(id: number, cvFile: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('cv', cvFile); // Attach the CV file

    const headers = new HttpHeaders().append('enctype', 'multipart/form-data');

    return this.http.post(`${this.apiUrl}/${id}/apply`, formData, { headers, responseType: 'text' });
  }
}
